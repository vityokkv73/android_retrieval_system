/*
 * This file is part of Android retrieval system project.
 * 
 * Android retrieval system is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. 
 * 
 * Android retrieval system is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Android retrieval system. If not, see <http://www.gnu.org/licenses/>.
 */

package net.deerhunter.ars.broadcast_receivers;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.location.LocationManager;
import net.deerhunter.ars.services.ImageStorageController;
import net.deerhunter.ars.services.SentSMSControllerService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * Listens an intent of phone booting.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class BootReceiver extends BroadcastReceiver {
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		checkPhoneNumber();
		startSentSMSController();
		startImageStorageController();
		LocationManager locationManager = LocationManager.getInstance();
		locationManager.stopAllLocationAlarmManager();
		locationManager.startLocationController(false);
	}

	/**
	 * Checks the phone number and if it doesn't match the previous phone
	 * number, send an SMS to the real owner.
	 */
	private void checkPhoneNumber() {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String line1Number = telephonyManager.getLine1Number();
		System.out.println("line1Number = " + line1Number);
		if (line1Number == null)
			return;
		final SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		String lastPhoneNumber = prefs.getString(context.getString(R.string.lastPhoneNumber), null);
		System.out.println("lastPhoneNumber = " + lastPhoneNumber);
		boolean isNewPhoneNumberDelivered = prefs.getBoolean(context.getString(R.string.phoneNumberDelivered), false);
		System.out.println("isNewPhoneNumberDelivered = " + isNewPhoneNumberDelivered);
		if (!isNewPhoneNumberDelivered || !line1Number.equals(lastPhoneNumber))
			sendNewPhoneNumber(line1Number);
	}

	/**
	 * Sends new phone number of this phone to its owner. If some problem
	 * occurs, an SMS will be sent after the next phone booting.
	 * 
	 * @param phoneNumber New phone number that will be sent to the owner
	 */
	@SuppressLint("CommitPrefEdits")
	private void sendNewPhoneNumber(final String phoneNumber) {
		final SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		final Editor prefsEditor = prefs.edit();
		String ownerPhoneNumber = prefs.getString(context.getString(R.string.ownerPhoneNumber), null);
		if (ownerPhoneNumber == null)
			return;

		final String SMS_SENT = "SMS_SENT";
		final String SMS_DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_SENT), 0);
		PendingIntent deliveredPIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_DELIVERED), 0);

		Context appContext = ArsApplication.getInstance().getApplicationContext();

		// ---when the SMS has been sent---
		appContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					case SmsManager.RESULT_ERROR_NO_SERVICE:
					case SmsManager.RESULT_ERROR_NULL_PDU:
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						prefsEditor.putBoolean(context.getString(R.string.phoneNumberDelivered), false);
						prefsEditor.commit();
						System.out.println("ERROR INTENT:" + intent.toString() + "   " + intent.getAction() + "    "
								+ intent.getData());
						break;
				}
			}
		}, new IntentFilter(SMS_SENT));

		// ---when the SMS has been delivered---
		appContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
					case Activity.RESULT_OK:
						prefsEditor.putBoolean(context.getString(R.string.phoneNumberDelivered), true);
						prefsEditor.putString(context.getString(R.string.lastPhoneNumber), phoneNumber);
						prefsEditor.commit();
						System.out.println("RESULT_OK INTENT:" + intent.toString() + "   " + intent.getAction()
								+ "    " + intent.getData());
						break;
					case Activity.RESULT_CANCELED:
						prefsEditor.putBoolean(context.getString(R.string.phoneNumberDelivered), false);
						prefsEditor.commit();
						System.out.println("RESULT_CANCELED INTENT:" + intent.toString() + "   " + intent.getAction()
								+ "    " + intent.getData());
						break;
				}
			}
		}, new IntentFilter(SMS_DELIVERED));

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(ownerPhoneNumber, null, context.getString(R.string.smsBody) + " " + phoneNumber,
				sentPIntent, deliveredPIntent);
	}

	/**
	 * Starts the service that listens all changes in image storage.
	 */
	private void startImageStorageController() {
		Intent imageStorageService = new Intent(context, ImageStorageController.class);
		context.startService(imageStorageService);
	}

	/**
	 * Starts the service that receives all the sent SMS.
	 */
	private void startSentSMSController() {
		Intent sentSMSIntent = new Intent(context, SentSMSControllerService.class);
		context.startService(sentSMSIntent);
	}
}
