package net.deerhunter.ars.broadcast_receivers;

import static net.deerhunter.ars.inner_structures.ControlConstants.ACTIVATE_PROGRAM;
import static net.deerhunter.ars.inner_structures.ControlConstants.ALARM;
import static net.deerhunter.ars.inner_structures.ControlConstants.LOCATION_UPDATE_SETTINGS;
import static net.deerhunter.ars.inner_structures.ControlConstants.SEND_CONTACTS;
import static net.deerhunter.ars.inner_structures.ControlConstants.SET_ADDRESS;
import static net.deerhunter.ars.inner_structures.ControlConstants.TURN_ON;
import static net.deerhunter.ars.inner_structures.ControlConstants._3G_SETTINGS;
import static net.deerhunter.ars.inner_structures.ControlConstants.SET_PHONE_NUMBER;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.gps.GPSHelper;
import net.deerhunter.ars.inner_structures.ControlConstants;
import net.deerhunter.ars.internet_utils.Network3gHelper;
import net.deerhunter.ars.internet_utils.WifiHelper;
import net.deerhunter.ars.location.LocationManager;
import net.deerhunter.ars.providers.ActivityContract;
import net.deerhunter.ars.utils.ContactHelper;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

/**
 * This class is used for receiving and process an SMS if it has a control
 * sequence. If an SMS has control sequence, this message will be deleted.
 */

public class SMSReceiver extends BroadcastReceiver {
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String CONTROL_TEXT_START = "  ";
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		// ---get the SMS message passed in---
		if (intent.getAction().equals(SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs;
			if (bundle != null) {
				// retrieve the SMS message
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];

				String senderPhoneNumber = null;
				long time = 0;
				String bodyText = "";
				for (int i = 0; i < msgs.length; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					senderPhoneNumber = msgs[i].getOriginatingAddress();
					time = msgs[i].getTimestampMillis();
					bodyText += msgs[i].getMessageBody();
				}
				TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				String myPhoneNumber = tMgr.getLine1Number();

				ContentValues newSMS = new ContentValues();
				newSMS.put(ActivityContract.SMS.SENDER,
						ContactHelper.getContactDisplayNameByNumber(context, senderPhoneNumber));
				newSMS.put(ActivityContract.SMS.RECIPIENT, "Me");
				newSMS.put(ActivityContract.SMS.SENDER_PHONE_NUMBER, senderPhoneNumber);
				newSMS.put(ActivityContract.SMS.RECIPIENT_PHONE_NUMBER, myPhoneNumber);
				newSMS.put(ActivityContract.SMS.TIME, time);
				newSMS.put(ActivityContract.SMS.SMS_BODY, bodyText);
				context.getContentResolver().insert(ActivityContract.SMS.CONTENT_URI, newSMS);
				processSMS(bodyText);
			}
		}
	}

	/**
	 * Process SMS and abort broadcast of this SMS if its text include control
	 * sequence.
	 * 
	 * @param smsText Text of the SMS
	 */
	private void processSMS(String smsText) {
		if (smsText.endsWith(CONTROL_TEXT_START)) { // control SMS
			String htmlAddress = getHTMLAddress(smsText);
			String controlSequence = getControlSequence(smsText);
			String phoneNumber = getPhoneNumber(smsText);
			processControlSequence(controlSequence, htmlAddress, phoneNumber);
			abortBroadcast();
		}
	}

	/**
	 * Returns the phone number found in this string or <code>null</code> if no
	 * phone number is found.
	 * 
	 * @param text Text in which the method will find the phone number.
	 * @return First found phone number in the <code>smsText</code>
	 */
	private String getPhoneNumber(String text) {
		String phoneNumber = null;
		Pattern pattern = Pattern
				.compile("^(.*\\s+)?(((\\+3)?8)?0((39)|(50)|(63)|(66)|(67)|(68)|(9[1-9]))\\d{7})((\\s+.*)|$)");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find())
			phoneNumber = matcher.group(0);
		return phoneNumber;
	}

	/**
	 * Process control sequence and HTML address.
	 * 
	 * @param controlSequence Control sequence for certain action.
	 * @param htmlAddress HTML address that can be set as default address data
	 *            will be sent to.
	 */
	private void processControlSequence(String controlSequence, String htmlAddress, String phoneNumber) {
		if (controlSequence == null)
			return;
		// get digits from the char sequence
		int controlValue = Integer.parseInt(controlSequence);
		int highDigit = controlValue / 100;
		int middleDigit = (controlValue - highDigit * 100) / 10;
		int lowDigit = controlValue % 10;
		switch (highDigit) {
			case ACTIVATE_PROGRAM:
				boolean activated = (middleDigit == 1);
				changeProgramState(activated);
				break;
			case TURN_ON:
				boolean enabled = (lowDigit == 1);
				changeModuleState(middleDigit, enabled);
				break;
			case LOCATION_UPDATE_SETTINGS:
				changeLocationUpdateSettings(middleDigit, lowDigit);
				break;
			case SEND_CONTACTS:
				boolean onlyNew = (middleDigit == ControlConstants.ONLY_NEW_CONTACTS);
				setContactUpdateNecessity(onlyNew);
				break;
			case ALARM:
				makeAlarm();
				break;
			case _3G_SETTINGS:
				set3gSettings(middleDigit);
				break;
			case SET_ADDRESS:
				setHtmlAddress(htmlAddress);
				break;
			case SET_PHONE_NUMBER:
				setPhoneNumber(phoneNumber);
				break;
		}

	}

	/**
	 * Sets a phone number of the real owner of this phone.
	 * 
	 * @param phoneNumber Phone number of the real owner of this phone
	 */
	private void setPhoneNumber(String phoneNumber) {
		if (phoneNumber == null)
			return;
		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		Editor prefsEditor = prefs.edit();
		prefsEditor.putString(context.getString(R.string.ownerPhoneNumber), phoneNumber);
		prefsEditor.apply();
	}

	/**
	 * If <code>htmlAddress != null</code>, method sets HTML address of the
	 * server page where data will be sent to.
	 * 
	 * @param htmlAddress HTML address of the server page where data will be
	 *            sent to
	 */
	private void setHtmlAddress(String htmlAddress) {
		if (htmlAddress != null) {
			SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
			Editor prefsEditor = prefs.edit();
			prefsEditor.putString(context.getString(R.string.serverAddress), htmlAddress);
			prefsEditor.apply();
		}
	}

	/**
	 * Stores 3G mode into the application preferences.
	 * 
	 * @param mode Mode of 3G module
	 */
	private void set3gSettings(int mode) {
		if (mode > ControlConstants.WITHOUT_PHOTO)
			return;
		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		Editor prefsEditor = prefs.edit();
		prefsEditor.putInt(context.getString(R.string._3gModuleSettings), mode);
		prefsEditor.apply();
	}

	/**
	 * Makes a loud alarm.
	 */
	private void makeAlarm() {
		try {
			AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

			MediaPlayer mp = MediaPlayer.create(context, R.raw.loud_music);
			mp.start();
		} catch (Exception e) {}
	}

	/**
	 * Sets the necessity to send a contacts information to the server.
	 * 
	 * @param onlyNew Flag that indicates a necessity to send only new contacts
	 */
	private void setContactUpdateNecessity(boolean onlyNew) {
		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		Editor prefsEditor = prefs.edit();
		prefsEditor.putBoolean(context.getString(R.string.needToSendContacts), true);
		prefsEditor.putBoolean(context.getString(R.string.sendOnlyNewContacts), onlyNew);
		prefsEditor.apply();
	}

	/**
	 * Sets the settings of a location update.
	 * 
	 * @param frequencyIndex Index of the frequency of the location update
	 * @param durationIndex Index of the duration of the location update
	 */
	private void changeLocationUpdateSettings(int frequencyIndex, int durationIndex) {
		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		Editor prefsEditor = prefs.edit();
		int[] frequences = context.getResources().getIntArray(R.array.frequency);
		int[] durations = context.getResources().getIntArray(R.array.duration);
		int frequency = frequences[frequencyIndex];
		prefsEditor.putInt(context.getString(R.string.locationListeningInterval), frequency);
		LocationManager locationManager = LocationManager.getInstance();
		boolean restartNow = (durationIndex == 0);
		if (!restartNow) {
			int duration = durations[durationIndex];
			prefsEditor.putInt(context.getString(R.string.updateLocationPeriod), duration);
		}
		prefsEditor.commit();

		locationManager.stopAllLocationAlarmManager();
		locationManager.startLocationController(restartNow);
	}

	/**
	 * Changes the state of the physical module.
	 * 
	 * @param module Number of the module. Permissible values are
	 *            <code>ControlConstants._3G, ControlConstants.WIFI, ControlConstants.GPS</code>
	 * @param enabled Result state of the module.
	 */
	private void changeModuleState(int module, boolean enabled) {
		switch (module) {
			case ControlConstants._3G:
				try {
					Network3gHelper.change3gState(context, enabled);
				} catch (Exception ex) {} // 3G module can't be enabled
				break;
			case ControlConstants.WIFI:
				WifiHelper.changeWifiState(context, enabled);
				break;
			case ControlConstants.GPS:
				GPSHelper.changeGPSState(context, enabled);
				break;
		}

	}

	/**
	 * Activates or deactivates the program components.
	 * 
	 * @param enabled Result state of the program components.
	 */
	private void changeProgramState(boolean enabled) {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns the control sequence for the given text. Return <code>null</code>
	 * if no control sequence.
	 * 
	 * @param text Text where control sequence will be tried to be found
	 * @return Control sequence
	 */
	private String getControlSequence(String text) {
		String result = null;
		Pattern ctrlSeqPattern = Pattern.compile("^(.*\\s+)?(\\d)*((\\d){3})((\\s+.*)|$)");
		Matcher ctrlSeqMatcher = ctrlSeqPattern.matcher(text);
		if (ctrlSeqMatcher.matches() && ctrlSeqMatcher.groupCount() > 3) {
			result = ctrlSeqMatcher.group(3);
		}
		return result;
	}

	/**
	 * Returns the HTML address for the given text. Return <code>null</code> if
	 * no HTML address in the text.
	 * 
	 * @param text Text where HTML address will be tried to be found
	 * @return HTML address
	 */
	private String getHTMLAddress(String text) {
		String result = null;
		Pattern htmlPattern = Pattern
				.compile("^(.*\\s+)?((((http|https)://)|(www\\.))+(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(/[a-zA-Z0-9\\&amp;%_\\./-~-]*)?)((\\s+)|$)");
		Matcher htmlMatcher = htmlPattern.matcher(text);
		if (htmlMatcher.matches() && htmlMatcher.groupCount() > 2)
			result = htmlMatcher.group(2);
		return result;
	}
}