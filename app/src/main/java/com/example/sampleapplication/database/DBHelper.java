package com.example.sampleapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sampleapplication.dao.ContactDAO;
import com.example.sampleapplication.model.Contact;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ContactDB.db";


    private static final String TAG = DBHelper.class.getSimpleName();

    private Context mContext;
   private ContactDAO mDao;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        this.mContext = context;


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        mDao = new ContactDAO(mContext);
        mDao.createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        mDao.upgrageTable(sqLiteDatabase);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addContact (Contact contact) {
        mDao = new ContactDAO(mContext);
        mDao.insertContactDetails(contact);

    }

    public void searchContact(String searchContact){
        mDao.search(searchContact);
    }

    public void numberOfRows(int rows){
        mDao.getRows(rows);
    }
    public void getData(String id) {
        mDao.getDetails(id);
    }
    public void getAllContacts() {
        mDao.findAll();
    }
    // code to update the single contact
    public void updateContact(Contact contact) {
        mDao.updateTable(contact);
    }

}
