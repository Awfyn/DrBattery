package com.awfyn.drbattery.oracle;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.awfyn.drbattery.MainActivity;
import com.awfyn.drbattery.R;
import com.awfyn.drbattery.oracle.receivers.ChangeChargingStateReceiver;
import com.awfyn.drbattery.oracle.workers.Realist;

import java.util.concurrent.TimeUnit;

import static com.awfyn.drbattery.App.SERVICE_CHANNEL_ID;

public class OracleService extends Service {
    private static final String TAG  = "TAG_oracleService";

    private final BroadcastReceiver receiver = new ChangeChargingStateReceiver();

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))// TODO: Replace Text
                .setContentText("someText")// TODO: Replace Text
                .setSmallIcon(R.drawable.ic_notification)// TODO: Replace Icon
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
        startForeground(543, notification);

        PeriodicWorkRequest realist = new PeriodicWorkRequest.Builder(Realist.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Miners", ExistingPeriodicWorkPolicy.KEEP, realist);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter powerConnectionFilter = new IntentFilter();
        powerConnectionFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        powerConnectionFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver, powerConnectionFilter);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WorkManager.getInstance(this).cancelUniqueWork("Miner");
        unregisterReceiver(receiver);
    }
}