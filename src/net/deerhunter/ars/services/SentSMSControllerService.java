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

package net.deerhunter.ars.services;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
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
 * This service class is used to control sent SMSes and save them into database.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
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
		// create and register the observer to all sent SMSes
		sentSMSObserver = new SentSMSObserver(handler);
		this.getContentResolver().registerContentObserver(Uri.parse("content://sms/sent"), true, sentSMSObserver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (sentSMSObserver != null) {
			this.getContentResolver().unregisterContentObserver(sentSMSObserver);
		}
		super.onDestroy();
	}

	/**
	 * Observer is used to listen all sent SMSes.
	 * 
	 * @author DeerHunter
	 */
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
			saveSmsAndID();

			super.onChange(selfChange);
		}

		/**
		 * Method saves new sent SMSes into the database and update last SMS id.
		 */
		private void saveSmsAndID() {
			SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
			int lastSentSMSid = prefs.getInt(getString(R.string.lastSentSMSid), 0);
			SharedPreferences.Editor prefsEditor = prefs.edit();

			Uri sentSMSUri = Uri.parse("content://sms/sent");

			Cursor cursor = getContentResolver().query(sentSMSUri, null, "_id > ?",
					new String[] { String.valueOf(lastSentSMSid) }, null);

			if (cursor != null && cursor.moveToFirst()) {

				do {
					putSMSIntoDB(cursor);
					prefsEditor.putInt(getString(R.string.lastSentSMSid), cursor.getInt(cursor.getColumnIndex("_id")));
					prefsEditor.commit();
				} while (cursor.moveToNext());
			}
			if (cursor != null)
				cursor.close();
		}

		/**
		 * Method saves new sent SMSes into the database.
		 * 
		 * @param cursor Cursor of the sent SMSes
		 */
		private void putSMSIntoDB(Cursor cursor) {
			TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String myPhoneNumber = tMgr.getLine1Number();

			String recipientPhoneNumber = cursor.getString(cursor.getColumnIndex("address"));
			long time = cursor.getLong(cursor.getColumnIndex("date"));
			String bodyText = cursor.getString(cursor.getColumnIndex("body"));

			ContentValues newSMS = new ContentValues();
			newSMS.put(ActivityContract.SMS.SENDER, "Me");
			newSMS.put(ActivityContract.SMS.RECIPIENT,
					ContactHelper.getContactDisplayNameByNumber(getApplicationContext(), recipientPhoneNumber));
			newSMS.put(ActivityContract.SMS.SENDER_PHONE_NUMBER, myPhoneNumber);
			newSMS.put(ActivityContract.SMS.RECIPIENT_PHONE_NUMBER, recipientPhoneNumber);
			newSMS.put(ActivityContract.SMS.TIME, time);
			newSMS.put(ActivityContract.SMS.SMS_BODY, bodyText);
			getContentResolver().insert(ActivityContract.SMS.CONTENT_URI, newSMS);
		}
	}

}
