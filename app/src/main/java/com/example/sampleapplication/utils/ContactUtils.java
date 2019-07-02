package com.example.sampleapplication.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.sampleapplication.model.Contact;
import com.example.sampleapplication.service.ContactIntentService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class ContactUtils  {

    private static final String TAG = ContactUtils.class.getSimpleName();

    private Context context;

    public void setContext(Context context){
        this.context = context;
    }
    public ContactUtils() {


        //EventBus.getDefault().register(this);
    }

    /**
     *
     * Retrieve contact information(contact id, name and phone number) from device
     *
     * @param context - The corresponding activity
     *
     * @param contactModelArrayList - Arraylist that contains contact id, name and phone number
     */
    public static void retrieveContactsFromDevice(Context context, ArrayList contactModelArrayList) {


        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        //    Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (phones.moveToNext()) {
            String id = phones.getString(phones.getColumnIndex(projection[0]));
            String name = phones.getString(phones.getColumnIndex(projection[1]));
            String phoneNumber = phones.getString(phones.getColumnIndex(projection[2]));


            Contact contactModel = new Contact();
            contactModel.setId(Long.parseLong(id));
            contactModel.setName(name);
            contactModel.setNumber(phoneNumber);
            contactModelArrayList.add(contactModel);

            //    Log.d("name>>",id+ " "+name+"  "+phoneNumber);
            //  }
            //  phones.close();
        }


    }

    public String getNumberUsingContactId(Context context, String contactId){



        context.startService(new Intent(context, ContactIntentService.class));
        String phNumber = new String();

        Intent i = new Intent(context, ContactIntentService.class);
        i.putExtra("id",contactId);
        context.startService(i);

        String cContactIdString = ContactsContract.Contacts._ID;
        Uri cCONTACT_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String number = ContactsContract.CommonDataKinds.Phone.NUMBER;

        String selection = cContactIdString + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(contactId)};

        Cursor cursor = context.getContentResolver().query(cCONTACT_CONTENT_URI, null, selection, selectionArgs, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            while ((cursor != null) && (cursor.isAfterLast() == false)) {
                if (cursor.getColumnIndex(cContactIdString) >= 0) {
                    if (contactId.equals(cursor.getString(cursor.getColumnIndex(cContactIdString)))) {
                        phNumber = cursor.getString(cursor.getColumnIndex(number));
                        break;
                    }
                }
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return phNumber;
    }


    public static void contactIdByPhoneNumber(Context ctx, String phoneNumber) {

        Intent i = new Intent(ctx,ContactIntentService.class);
        i.putExtra(ContactIntentService.KEY_CONTACT_TYPE, ContactIntentService.DATA_TYPE_CONTECT_NUMBER);
        i.putExtra(ContactIntentService.KEY_CONTACT_NUMBER,phoneNumber);
        ctx.startService(i);
        ctx.startService(new Intent(ctx, ContactIntentService.class));

    }

    public static  String phoneNumberByContactId(Context ctx, String id){

        String number = "0713414713";
        Intent i = new Intent(ctx,ContactIntentService.class);
        i.putExtra("phone_number",number);
        ctx.startService(i);


        ctx.startService(new Intent(ctx, ContactIntentService.class));
        if (id != null && id.length() > 0){
            ContentResolver contentResolver = ctx.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(id));

            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = contentResolver.query(uri, projection, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                }
                cursor.close();
            }
        }

        return number;
    }
    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    /**
     * @param context - The corresponding context
     * @param contactId - The id that is given by the user
     * @return The contact details that includes id,name and number
     */
    public static void getContactDetailsById(Context context,String contactId){
        Intent i = new Intent(context,ContactIntentService.class);
        i.putExtra(ContactIntentService.KEY_CONTACT_TYPE, ContactIntentService.DATA_TYPE_CONTECT_ID);
        i.putExtra(ContactIntentService.KEY_CONTACT_ID,contactId);
        context.startService(i);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArrayList<Contact> models) {
        Log.d(TAG, "onEvent: " + models.size());

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent2(Contact models) {
        Log.d(TAG, "onEvent_ID: " + models.getId());

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3(ArrayList<Contact> model) {
        Log.d(TAG, "onEvent_details: " + model.size());

    }

}
