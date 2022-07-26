package com.example.smsautomatorplus;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class SMSOp {
    public static void sendSMS(String phoneNo, String msg, Context context, boolean saveSMS) {

        if(saveSMS) {
            try {
                ContentValues cv = new ContentValues();
                cv.put("address", phoneNo);
                cv.put("body", msg);
                Log.i(MainActivity.TAG, "sendSMS: try statement");
                Uri uriSms = Uri.parse("content://sms/sent");
                context.getContentResolver().insert(uriSms, cv);
                context.getContentResolver().notifyChange(uriSms, null);

            } catch (Exception e) {
                Log.i(MainActivity.TAG, "Could not delete SMS from inbox: " + e.getMessage());
            }
        }

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


    public static void deleteSMS(Context context, String message, String number) {
        try {
            Log.i(MainActivity.TAG, "delteSMS: try statement");
            Uri uriSms = Uri.parse("content://sms/sent");
            Cursor c = context.getContentResolver().query(uriSms,
                    new String[] { "_id", "thread_id", "address",
                            "person", "date", "body" }, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    long threadId = c.getLong(1);
                    String address = c.getString(2);
                    String body = c.getString(5);
//                    Log.i(MainActivity.TAG, "Found SMS: " + body);

                    if (message.equals(body) && address.equals(number)) {
                        Log.i(MainActivity.TAG, "Deleting sms with" + threadId);
                        context.getContentResolver().delete(
                                Uri.parse("content://sms/" + id), null, null);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.i(MainActivity.TAG, "Could not delete SMS from inbox: " + e.getMessage());
        }
    }

}
