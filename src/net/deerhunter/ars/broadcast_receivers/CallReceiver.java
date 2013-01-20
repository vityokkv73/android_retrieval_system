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

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import net.deerhunter.ars.providers.ActivityContract;
import net.deerhunter.ars.utils.ContactHelper;

/**
 * This class listens the changes in phone call state.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class CallReceiver extends BroadcastReceiver {
	private Context mContext;
	private Intent mIntent;
	private static boolean listenerAdded = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		mIntent = intent;

		if (!listenerAdded) {
			listenerAdded = true;
			CallStateListener phoneListener = new CallStateListener();
			TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	/**
	 * This class listens the changes in phone call state and store changes into
	 * the database.
	 * 
	 * @author DeerHunter
	 */
	class CallStateListener extends PhoneStateListener {
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if (mIntent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
						String dialingNumber = mIntent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
						TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
						String myNumber = tMgr.getLine1Number();
						ContentValues newCall = new ContentValues();
						newCall.put(ActivityContract.Calls.CALLER, "Me");
						newCall.put(ActivityContract.Calls.CALLER_PHONE_NUMBER, myNumber);
						newCall.put(ActivityContract.Calls.RECIPIENT,
								ContactHelper.getContactDisplayNameByNumber(mContext, dialingNumber));
						newCall.put(ActivityContract.Calls.RECIPIENT_PHONE_NUMBER, dialingNumber);
						newCall.put(ActivityContract.Calls.TIME, System.currentTimeMillis());
						mContext.getContentResolver().insert(ActivityContract.Calls.CONTENT_URI, newCall);
					}
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					ContentValues newCall = new ContentValues();
					TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
					String myNumber = tMgr.getLine1Number();
					newCall.put(ActivityContract.Calls.CALLER, incomingNumber);
					newCall.put(ActivityContract.Calls.CALLER_PHONE_NUMBER,
							ContactHelper.getContactDisplayNameByNumber(mContext, incomingNumber));
					newCall.put(ActivityContract.Calls.RECIPIENT, "Me");
					newCall.put(ActivityContract.Calls.RECIPIENT_PHONE_NUMBER, myNumber);
					newCall.put(ActivityContract.Calls.TIME, System.currentTimeMillis());
					mContext.getContentResolver().insert(ActivityContract.Calls.CONTENT_URI, newCall);
					break;
			}
		}
	}
}
