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
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 20.10.12
 * Time: 20:35
 * To change this template use File | Settings | File Templates.
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

    class CallStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mIntent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                        String dialingNumber = mIntent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                        TelephonyManager tMgr =(TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                        String myNumber = tMgr.getLine1Number();
                        ContentValues newCall = new ContentValues();
                        newCall.put(ActivityContract.Calls.CALLER, "Me");
                        newCall.put(ActivityContract.Calls.CALLER_PHONE_NUMBER, myNumber);
                        newCall.put(ActivityContract.Calls.RECIPIENT, ContactHelper.getContactDisplayNameByNumber(mContext, dialingNumber));
                        newCall.put(ActivityContract.Calls.RECIPIENT_PHONE_NUMBER, dialingNumber);
                        newCall.put(ActivityContract.Calls.TIME, System.currentTimeMillis());
                        mContext.getContentResolver().insert(ActivityContract.Calls.CONTENT_URI, newCall);
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    ContentValues newCall = new ContentValues();
                    TelephonyManager tMgr =(TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                    String myNumber = tMgr.getLine1Number();
                    newCall.put(ActivityContract.Calls.CALLER, incomingNumber);
                    newCall.put(ActivityContract.Calls.CALLER_PHONE_NUMBER, ContactHelper.getContactDisplayNameByNumber(mContext, incomingNumber));
                    newCall.put(ActivityContract.Calls.RECIPIENT, "Me");
                    newCall.put(ActivityContract.Calls.RECIPIENT_PHONE_NUMBER, myNumber);
                    newCall.put(ActivityContract.Calls.TIME, System.currentTimeMillis());
                    mContext.getContentResolver().insert(ActivityContract.Calls.CONTENT_URI, newCall);
                    break;
            }
        }
    }
}
