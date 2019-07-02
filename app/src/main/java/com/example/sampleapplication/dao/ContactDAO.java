package com.example.sampleapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sampleapplication.database.DBHelper;
import com.example.sampleapplication.model.Contact;

import java.util.ArrayList;


public class ContactDAO {

    private static final String CONTACTS_TABLE_NAME = "contacts";
    private static final String CONTACTS_COLUMN_ID = "id";
    private static final String CONTACTS_COLUMN_NAME = "name";
    private static final String CONTACTS_COLUMN_PHONE_NUMBER = "phone_number";
    private static final String TAG = ContactDAO.class.getSimpleName();

    private DBHelper mHelper;
    private Context mContext;

    public ContactDAO(Context context) {
        this.mContext = context;
        mHelper = new DBHelper(context);
    }

    public SQLiteDatabase getReadDB(){
        return mHelper.getReadableDatabase();
    }

    /**
     * Creating table in the database
     * @param sqLiteDatabase - SQLiteDatabase
     * @return The created database
     */
    public SQLiteDatabase createTable(SQLiteDatabase sqLiteDatabase){
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE_NAME + "("
                + CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY," + CONTACTS_COLUMN_NAME + " TEXT,"
                + CONTACTS_COLUMN_PHONE_NUMBER + " TEXT" + ")";



        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

        return sqLiteDatabase;
    }
    public void upgrageTable(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);

    }

    /**
     * Insert contact details to the table
     * @param model The ContactModel
     */
    public  void insertContactDetails(Contact model){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACTS_COLUMN_ID, model.getId());
        values.put(CONTACTS_COLUMN_NAME, model.getName()); // Contact Name
        values.put(CONTACTS_COLUMN_PHONE_NUMBER, model.getNumber()); // Contact Phone

        // Inserting Row
        db.insert(CONTACTS_TABLE_NAME, null, values);
       // deleteDuplicate();

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
    public Cursor getDetails(String id){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + CONTACTS_TABLE_NAME+" WHERE "+CONTACTS_COLUMN_ID+"="+CONTACTS_COLUMN_ID, null );
        return res;
    }

    private void deleteDuplicate(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_ID + " NOT IN "+ " (SELECT min("+CONTACTS_COLUMN_ID+") FROM "+CONTACTS_TABLE_NAME +" GROUP BY "+ CONTACTS_COLUMN_NAME+")",null );

    }

    /**
     * update the single contact
     * @param contact - ContactModel
     * @return Updated table
     */
    public int updateTable(Contact contact){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACTS_COLUMN_NAME, contact.getName());
        values.put(CONTACTS_COLUMN_PHONE_NUMBER, contact.getNumber());

        // updating row
        return db.update(CONTACTS_TABLE_NAME, values, CONTACTS_COLUMN_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    public void getRows(int numRows){
        SQLiteDatabase db = mHelper.getReadableDatabase();
       numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
       // return numRows;
    }

    /**
     * Find all contact details that stored in database
     */
    public void findAll() {
        ArrayList<Contact> contacts = null;
        try {
            SQLiteDatabase sqLiteDatabase = mHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + CONTACTS_TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                contacts = new ArrayList<Contact>();
                do {
                    Contact contact = new Contact();
                    //contact.setId(cursor.getInt(0));
                    contact.setName(cursor.getString(1));
                    contact.setNumber(cursor.getString(2));

                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            contacts = null;
        }
//        return contacts;
    }

    /**
     * Search values that are in database according to given string value
     * @param keyword - The string value that is send from user
     * @return ArrayList of selected values that are similar to the keyword
     */
    public ArrayList<Contact> search(String keyword) {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = mHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + CONTACTS_TABLE_NAME + " WHERE " + CONTACTS_COLUMN_NAME + " LIKE ?", new String[] { "%" + keyword + "%" });
            if (cursor.moveToFirst()) {
               // contacts = new ArrayList<Contact>();
                do {
                    Contact contact = new Contact();
                  //  contact.setId(cursor.getInt(0));
                    contact.setName(cursor.getString(1));
                    contact.setNumber(cursor.getString(2));
                    contacts.add(contact);

                } while (cursor.moveToNext());

            }

            cursor.close();

        } catch (Exception e) {
            contacts = null;
        }
        Log.d(TAG, "search: "+ contacts.toString());
        return contacts;
    }

}
