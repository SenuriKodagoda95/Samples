package com.example.sampleapplication.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.sampleapplication.database.DBHelper;
import com.example.sampleapplication.model.Contact;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ContactIntentService extends IntentService {


    public static final String KEY_CONTACT_TYPE = "type";
    public static final String KEY_CONTACT_ID = "contact_id";
    public static final int DATA_TYPE_CONTECT_ID = 0;
    public static final int DATA_TYPE_CONTECT_NUMBER = 1;
    public static final int DATA_TYPE_CONTECT_LIST = 2;

    public static final String KEY_CONTACT_NUMBER = "number" ;
    private static final String TAG = "ContactIntentService";
    public static final String RESULT = "result";
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.sampleapplication.action.FOO";
    private static final String ACTION_BAZ = "com.example.sampleapplication.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.sampleapplication.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.sampleapplication.extra.PARAM2";

    private DBHelper mydb;

    String[] PROJECTION = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};


    public ContactIntentService() {
        super("ContactIntentService");
    }

    ArrayList<Contact> mContactModelArrayList = null;
    ArrayList<Contact> mContactModelList = null;
    String[] mIndex;
    String mIn;

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ContactIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ContactIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContactModelList = new ArrayList<>();
        mContactModelArrayList = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent == null)return;

        int type = intent.getIntExtra(KEY_CONTACT_TYPE, -1);

        switch (type){
            case DATA_TYPE_CONTECT_ID:
                getContactsById(intent);
                break;
            case DATA_TYPE_CONTECT_NUMBER:
                getContactIdByNumber(intent);
                break;
            case DATA_TYPE_CONTECT_LIST:
                getContactsList();
                break;
        }

        getContactsList();


        Log.d(TAG, "Service started");


    }

    private void getContactsList() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        getList(phones);
    }

    private void getList(Cursor phones) {

        mydb  = new DBHelper(ContactIntentService.this);


        while (phones.moveToNext())
        {
            long id = phones.getLong(phones.getColumnIndex(PROJECTION[0]));
            String name = phones.getString(phones.getColumnIndex(PROJECTION[1]));
            String phoneNumber = phones.getString(phones.getColumnIndex(PROJECTION[2]));


            long i = phones.getLong(phones.getColumnIndex(PROJECTION[0]));

            DatabaseUtils.dumpCursor(phones);

            Contact contact = new Contact();
            contact.setId(i);
            contact.setName(name);
            contact.setNumber(phoneNumber);
            mContactModelArrayList.add(contact);

            insertToDatabase(contact);
            Log.d(TAG, contact.toString());
        }

       phones.close();

        EventBus.getDefault().post((mContactModelArrayList));
    }

    private void insertToDatabase(Contact model){
        Log.d(TAG, "getList: " + "inserting");

        mydb.addContact(model);


        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");


        mydb.getAllContacts();
        for (Contact cn : mContactModelArrayList) {
            String log = "Id: " + cn.getId() + " ,Name: " + cn.getName() + " ,Phone: " +
                    cn.getNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }


    }

    private void getContactIdByNumber(Intent intent) {

        String number = intent.getStringExtra("number");
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?", new String[] { String.valueOf(number) },
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        readCursor(cursor);


    }

    private void readCursor(Cursor cursor) {
        if (cursor == null)return;

        if (cursor.moveToFirst()){
            mIndex = new String[]{cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))};

            Contact contact = new Contact();

            contact.setId(Long.parseLong(mIndex[0]));
            contact.setName(mIndex[1]);
            contact.setNumber(mIndex[2]);
            mContactModelList.add(contact);

            Log.d("index>>",mIndex[0]+" "+mIndex[1]+" "+ mIndex[2]);
            EventBus.getDefault().post(mContactModelList);
        }
        cursor.close();

    }

    private void getContactsById(Intent intent) {

        String cid = intent.getStringExtra("contact_id");

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, ContactsContract.Contacts._ID + " = ?", new String[] { String.valueOf(cid) },
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        readCursor(cursor);
    }





    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
