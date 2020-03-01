package com.awfyn.drbattery.oracle.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.awfyn.drbattery.oracle.OracleService;

public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = "TAG_BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: ");
            Intent service = new Intent(context, OracleService.class);
            service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextCompat.startForegroundService(context, service);
        }
    }
}