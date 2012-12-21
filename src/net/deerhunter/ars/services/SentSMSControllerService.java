package net.deerhunter.ars.services;

import net.deerhunter.ars.providers.ActivityContract;
import net.deerhunter.ars.utils.ContactHelper;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;

/**
 * Created with IntelliJ IDEA. User: DeerHunter Date: 21.10.12 Time: 14:27 To
 * change this template use File | Settings | File Templates.
 */
public class SentSMSControllerService extends Service {
	private static Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	private SentSMSObserver sentSMSObserver = null;

	public void onCreate() {
		super.onCreate();
			sentSMSObserver = new SentSMSObserver(handler);
			this.getContentResolver().registerContentObserver(
					Uri.parse("content://sms/sent"), true, sentSMSObserver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		return Service.START_STICKY;

	}

	@Override
	public void onDestroy() {
		if (sentSMSObserver != null) {
			this.getContentResolver()
					.unregisterContentObserver(sentSMSObserver);
		}
		super.onDestroy();

	}

	public class SentSMSObserver extends ContentObserver {
		public SentSMSObserver(Handler handler) {
			super(handler);
		}

		@Override
		public boolean deliverSelfNotifications() {
			return true;
		}

		@Override
		public void onChange(boolean selfChange) {
			SharedPreferences prefs = getSharedPreferences(getApplication()
					.getPackageName(), MODE_PRIVATE);
			int lastSentSMSid = prefs.getInt("lastSentSMSid", 0);
			SharedPreferences.Editor prefEditor = prefs.edit();

			Uri sentSMSUri = Uri.parse("content://sms/sent");

			Cursor cursor = getContentResolver().query(sentSMSUri, null,
					"_id > ?", new String[] { String.valueOf(lastSentSMSid) },
					null);

			if (cursor.moveToFirst()) {

				do {
					putSMSIntoDB(cursor);
					prefEditor.putInt("lastSentSMSid", cursor.getInt(cursor.getColumnIndex("_id")));
					prefEditor.commit();
				} while (cursor.moveToNext());
			}

			super.onChange(selfChange);

		}

		private void putSMSIntoDB(Cursor cursor) {
			TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String myPhoneNumber = tMgr.getLine1Number();
			
			String recipientPhoneNumber = cursor.getString(cursor.getColumnIndex("address"));
			long time = cursor.getLong(cursor.getColumnIndex("date"));
			String bodyText = cursor.getString(cursor.getColumnIndex("body"));

			ContentValues newSMS = new ContentValues();
			newSMS.put(ActivityContract.SMS.SENDER, "Me");
			newSMS.put(ActivityContract.SMS.RECIPIENT, ContactHelper
					.getContactDisplayNameByNumber(getApplicationContext(), recipientPhoneNumber));
			newSMS.put(ActivityContract.SMS.SENDER_PHONE_NUMBER,
					myPhoneNumber);
			newSMS.put(ActivityContract.SMS.RECIPIENT_PHONE_NUMBER,
					recipientPhoneNumber);
			newSMS.put(ActivityContract.SMS.TIME, time);
			newSMS.put(ActivityContract.SMS.SMS_BODY, bodyText);
			getContentResolver().insert(ActivityContract.SMS.CONTENT_URI,
					newSMS);
		}
	}

}
