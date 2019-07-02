package com.example.sampleapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class PermissionManager {

    private static final int PERMISSION_REQUEST_CODE = 1;

    /**
     * To check the permission to dial the given phone number in the device
     * @param activity - The corresponding activity
     */
    public static void checkPermissionForDialling(Activity activity, Intent intent){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (activity.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.CALL_PHONE};

                activity.requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }
        activity.startActivity(intent);


    }

    /**
     * To get the permission for read the contacts that are in device storage
     * @param activity - The corresponding activity
     */
    public static void checkPermissionForRetrieveContacts(Activity activity){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.READ_CONTACTS};

                activity.requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

    }

}
