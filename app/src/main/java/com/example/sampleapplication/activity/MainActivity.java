package com.example.sampleapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sampleapplication.adapter.SearchListAdapter;
import com.example.sampleapplication.dao.ContactDAO;
import com.example.sampleapplication.model.Contact;
import com.example.sampleapplication.service.ContactIntentService;
import com.example.sampleapplication.utils.ContactUtils;
import com.example.sampleapplication.PermissionManager;
import com.example.sampleapplication.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName() ;
    private EditText mPhoneNumber;
    private Button mBtnCall,mBtnContacts,mSearch;
    private int i = 0;
    MyReceiver myReceiver;
    private ContactUtils utils;
    private ContactDAO dao;
    private RecyclerView recyclerView;
    private SearchListAdapter listAdapter;
    private ArrayList<Contact> mContactModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = new ContactDAO(MainActivity.this);
        utils = new ContactUtils();
//        Log.d(TAG, "onE: " + mPhoneNumber.getText().toString());

       // startService(new Intent(MainActivity.this, ContactIntentService.class));

        mContactModelArrayList = new ArrayList<>();

        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mBtnCall = (Button) findViewById(R.id.btnCall);
        mBtnContacts = (Button) findViewById(R.id.btnContact);
        mSearch = (Button) findViewById(R.id.button);
        recyclerView = (RecyclerView) findViewById(R.id.rView);
        mBtnCall.setOnClickListener(this);
        mBtnContacts.setOnClickListener(this);
        mSearch.setOnClickListener(this);

        listAdapter = new SearchListAdapter(mContactModelArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listAdapter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnCall:
                dialNumber();
//                getDetails();
                break;
            case R.id.btnContact:
                retrieveContacts();
                break;
            case R.id.button:
            Intent in  = new Intent(MainActivity.this,SearchActivity.class);
            startActivity(in);
                break;
                default:

        }


        //startActivity(callIntent);
    }





    private  void dialNumber(){
        utils.contactIdByPhoneNumber(MainActivity.this,mPhoneNumber.getText().toString());

        String number = mPhoneNumber.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        PermissionManager.checkPermissionForDialling(MainActivity.this,callIntent);

    }
    private void retrieveContacts(){
        dao = new ContactDAO(MainActivity.this);

        String number = mPhoneNumber.getText().toString();

//        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
////        startActivity(intent);
        mContactModelArrayList.clear();

        mContactModelArrayList.addAll(dao.search(number));
        listAdapter = new SearchListAdapter(mContactModelArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listAdapter);

    }

    private void getDetails(){
        String id  = mPhoneNumber.getText().toString();
        ContactUtils.getContactDetailsById(MainActivity.this,id);
        Intent i = new Intent(MainActivity.this, ContactIntentService.class);
        i.putExtra("contact_id",id);
        startService(i);


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(MainActivity.this);
//        myReceiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ContactIntentService.RESULT);
//        registerReceiver(myReceiver, intentFilter);
        String id  = mPhoneNumber.getText().toString();
        Intent i = new Intent(MainActivity.this,ContactIntentService.class);
        i.putExtra("contact_id",id);
        startService(i);
    }



    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(MainActivity.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String models) {


        Log.d(TAG, "onEvent2: " + models);

    }

    // Unregistering
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MainActivity.this);
    }
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            int datapassed = arg1.getIntExtra("DATAPASSED", 0);
            String orgData = arg1.getStringExtra("DATA_BACK");

            Toast.makeText(MainActivity.this,
                    "Triggered by Service!\n"
                            + "Data passed: " + String.valueOf(datapassed) + "\n"
                            + "original Data: " + orgData,
                    Toast.LENGTH_LONG).show();

        }

    }
}