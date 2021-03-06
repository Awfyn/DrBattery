package com.awfyn.drbattery.oracle.workers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.awfyn.drbattery.MainActivity;
import com.awfyn.drbattery.R;
import com.awfyn.drbattery.database.DrDatabase;
import com.awfyn.drbattery.database.dao_s.OracleDao;
import com.awfyn.drbattery.database.entities.Mineral;

import static com.awfyn.drbattery.App.SERVICE_CHANNEL_ID;

public class Realist extends Worker {
    private static final String TAG = "TAG_COLLECTOR";

    private DrDatabase database;
    private OracleDao dao;

    public Realist(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(context, SERVICE_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))// TODO: Replace Text
                .setContentText("someText")// TODO: Replace Text
                .setSmallIcon(R.drawable.ic_notification)// TODO: Replace Icon
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();

        NotificationManagerCompat.from(context).notify(543, notification);

        database = DrDatabase.getInstance(context);
        dao = database.oracleDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        Mineral mineral = new Mineral();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, iFilter);

        int status = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) : -1;
        if (status == BatteryManager.BATTERY_STATUS_FULL) {
            database = null;
            dao = null;
            return Result.success();
        }

        mineral.setStatus(status);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
        mineral.setLevel(level * 100 / (float)scale);

        int voltage = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) : -1;
        mineral.setVoltage(voltage);

        int temperature = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) : -1;
        mineral.setTemperature(temperature);

        int health = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) : -1;
        mineral.setHealth(health);

        mineral.setTimeStamp(System.currentTimeMillis());
        mineral.setCalculated(false);

        dao.insertMineral(mineral);

        database = null;
        dao = null;
        return Result.success();
    }
}
