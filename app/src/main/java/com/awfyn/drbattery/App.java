package com.awfyn.drbattery;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.awfyn.drbattery.oracle.receivers.BootReceiver;

public class App extends Application {
    public static final String SERVICE_CHANNEL_ID = "awfynDrBatteryServiceChannelId";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        registerBootCompleteReceiver();
    }

    private void registerBootCompleteReceiver() {
        IntentFilter bootCompleteFilter = new IntentFilter();
        bootCompleteFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(new BootReceiver(), bootCompleteFilter);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    SERVICE_CHANNEL_ID,
                    getString(R.string.app_name) + "someText",// TODO: Replace Text
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
