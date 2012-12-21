package net.deerhunter.ars.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import net.deerhunter.ars.providers.ActivityContract;
import net.deerhunter.ars.utils.ContactHelper;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 17.10.12
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */

public class SMSReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        //---get the SMS message passed in---
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
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    senderPhoneNumber = msgs[i].getOriginatingAddress();
                    time = msgs[i].getTimestampMillis();
                    bodyText += msgs[i].getMessageBody();
                }
                TelephonyManager tMgr =(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String myPhoneNumber = tMgr.getLine1Number();
                
                ContentValues newSMS = new ContentValues();
                newSMS.put(ActivityContract.SMS.SENDER, ContactHelper.getContactDisplayNameByNumber(context, senderPhoneNumber));
                newSMS.put(ActivityContract.SMS.RECIPIENT, "Me");
                newSMS.put(ActivityContract.SMS.SENDER_PHONE_NUMBER, senderPhoneNumber);
                newSMS.put(ActivityContract.SMS.RECIPIENT_PHONE_NUMBER, myPhoneNumber);
                newSMS.put(ActivityContract.SMS.TIME, time);
                newSMS.put(ActivityContract.SMS.SMS_BODY, bodyText);
                context.getContentResolver().insert(ActivityContract.SMS.CONTENT_URI, newSMS);
            }
        }
    }
}
