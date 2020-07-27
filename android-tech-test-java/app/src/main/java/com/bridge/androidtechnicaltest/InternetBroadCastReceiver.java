package com.bridge.androidtechnicaltest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bridge.androidtechnicaltest.utils.NetworkUtil;

/**
 * Broadcast receiver to listen to internet changes.
 * Whenver, internet comes back online, it starts an intent service to
 * delete all the Pupils from Backend which are yet to be deleted.
 */
public class InternetBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = InternetBroadCastReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int networkState = NetworkUtil.getConnectionStatus(context);
        if (networkState == NetworkUtil.NOT_CONNECTED) {
            Log.d(TAG, "I am not connected to internet");

        } else if (networkState == NetworkUtil.MOBILE) {
            Log.d(TAG, "I am connected via mobile internet");
            Intent i = new Intent(context, DeleteDataService.class);
            context.startService(i);
        } else if (networkState == NetworkUtil.WIFI) {
            Log.d(TAG, "I am connected via wifi");
            Intent i = new Intent(context, DeleteDataService.class);
            context.startService(i);
        }
    }
}