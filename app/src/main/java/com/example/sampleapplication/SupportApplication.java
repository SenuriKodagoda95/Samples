package com.example.sampleapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sampleapplication.utils.ContactUtils;

import org.greenrobot.eventbus.EventBus;

public class SupportApplication extends Application {

    static Activity activity;

    private static final String TAG =SupportApplication.class.getSimpleName();
    public static SupportApplication INSTANCE;
    private EventBus eventBus;
    private static SupportApplication mInstance;
    private ContactUtils contactUtils;

    public static SupportApplication getInstance(Context context){
        if(mInstance == null) {
            mInstance = new SupportApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        contactUtils = new ContactUtils();

        eventBus = EventBus.getDefault();

        eventBus.register(contactUtils);
    }


    public EventBus getEventBus() {
        return eventBus;
    }




}
