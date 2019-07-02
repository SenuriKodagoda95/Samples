package com.example.sampleapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.sampleapplication.model.Contact;

import java.util.ArrayList;

/**
 *
 */
public class ContactManagerUtils {
    static String contactId,name,phoneNumber;

    /**
     *
     * Retrieve contact information(contact id, name and phone number) from device
     *
     * @param context - The corresponding context
     *
     * @param contactModelArrayList - Arraylist that contains contact id, name and phone number
     */
    public static void retrieveContactsFromDevice(Context context, ArrayList contactModelArrayList){

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        //  Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (phones.moveToNext())
        {
            String id = phones.getString(phones.getColumnIndex(projection[0]));
            String name = phones.getString(phones.getColumnIndex(projection[1]));
            String phoneNumber = phones.getString(phones.getColumnIndex(projection[2]));

            Contact contactModel = new Contact();
            contactModel.setId(Long.parseLong(id));
            contactModel.setName(name);
            contactModel.setNumber(phoneNumber);
            contactModelArrayList.add(contactModel);

            Log.d("name>>",id+ " "+name+"  "+phoneNumber);
        }
        phones.close();
    }

    /**
     * @param activity - The corresponding context
     * @param id - The contact id of given phone number
     * @return - The phone number that relevant to phone number
     */
    public static String getPhoneNumberById(Activity activity, String id) {
       // String contactId,name,phoneNumber;
        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
        Cursor phones = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        while (phones.moveToNext()) {
            contactId = phones.getString(phones.getColumnIndex(projection[0]));
            name = phones.getString(phones.getColumnIndex(projection[1]));
            phoneNumber = phones.getString(phones.getColumnIndex(projection[2]));

            ArrayList<String> conId = new ArrayList<>();




            Log.d("name>>", contactId + " " + name + "  " + phoneNumber);
        }
        phones.close();


    return phoneNumber;
    }

}
