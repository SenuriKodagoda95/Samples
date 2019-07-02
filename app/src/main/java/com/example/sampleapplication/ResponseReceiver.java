package com.example.sampleapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.sampleapplication.service.ContactIntentService;

public class ResponseReceiver extends BroadcastReceiver {
    public static final String ACTION_RESP =
            "com.mamlambo.intent.action.MESSAGE_PROCESSED";

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, ContactIntentService.class);
        String[] input = intent.getStringArrayExtra(ContactIntentService.RESULT);

    }
}
