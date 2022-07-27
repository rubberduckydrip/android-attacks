package com.example.contactsretriever;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class BaseClass {
    String str;
    String TAG = "BaseClass";

    public BaseClass() {
        Log.i(TAG, "This is a default constructor!");
    }

    @SuppressLint("Range")
    public void downloadContacts(Context context){

        HashMap<String, String> contactList = new HashMap();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.i(TAG, "Name: " + name);
//                        Log.i(TAG, "Phone Number: " + phoneNo);
                        contactList.put(phoneNo, name);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        for (Map.Entry<String, String> entry : contactList.entrySet()) {
            String phoneNo = entry.getKey();
            String name = entry.getValue();
            Log.i(TAG, "PhoneNo: " + phoneNo);
            Log.i(TAG, "Name : " + name);
        }
    }
}