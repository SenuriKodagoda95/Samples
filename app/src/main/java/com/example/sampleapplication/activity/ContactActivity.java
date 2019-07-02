package com.example.sampleapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sampleapplication.model.Contact;
import com.example.sampleapplication.service.ContactIntentService;
import com.example.sampleapplication.adapter.ContactListAdapter;
import com.example.sampleapplication.utils.ContactUtils;
import com.example.sampleapplication.adapter.CustomAdapter;
import com.example.sampleapplication.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity  {
    private static final String TAG = ContactActivity.class.getSimpleName();
    private RecyclerView mListView;
    private CustomAdapter mCustomAdapter;
    private ContactListAdapter listAdapter;
    private ArrayList<Contact> mContactModelArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        startService(new Intent(ContactActivity.this, ContactIntentService.class));

        mListView = (RecyclerView) findViewById(R.id.view);

        mContactModelArrayList = new ArrayList<>();

        ContactUtils.retrieveContactsFromDevice(ContactActivity.this,mContactModelArrayList);
       // mCustomAdapter = new CustomAdapter(ContactActivity.this,mContactModelArrayList);
        listAdapter = new ContactListAdapter(mContactModelArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(mLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setAdapter(listAdapter);
      //  mListView.setAdapter(mCustomAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(ContactActivity.this);
    }



    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(ContactActivity.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArrayList<Contact> models) {
        Log.d(TAG, "onEvent: " + models.size());

    }

    // Unregistering
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(ContactActivity.this);
    }


}