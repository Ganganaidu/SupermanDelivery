package com.supermandelivery.recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.supermandelivery.otpactivities.OTPConfirmActivity;

/**
 * Created by Office on 3/19/2016.
 */
public class MySMSReceiver extends BroadcastReceiver {

    public MySMSReceiver() {
    }

    public MySMSReceiver(SMSReceiveCallBack callback) {
        this.smsReceiveCallBack = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "";
        if (Build.VERSION.SDK_INT >= 19) { //KITKAT
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            //String format = intent.getStringExtra("format");
            SmsMessage sms = msgs[0];
            message = sms.getMessageBody();
        } else {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage shortMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
            message = shortMessage.getDisplayMessageBody();
        }
        Log.d("MySMSReceiver", "SMS message text: " + message);
        smsReceiveCallBack.onSmsReceived(message);
    }

    //in your AlarmReceiver class:
    private SMSReceiveCallBack smsReceiveCallBack;

    public interface SMSReceiveCallBack {
        void onSmsReceived(String msg);
    }
}
