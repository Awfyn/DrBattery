package com.awfyn.drbattery.oracle.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.awfyn.drbattery.oracle.workers.RealistOneTime;

import static com.awfyn.drbattery.oracle.workers.RealistOneTime.BATTERY_STATUS;

public class ChangeChargingStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
                IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = context.registerReceiver(null, iFilter);
                if ((batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) : -1) != BatteryManager.BATTERY_STATUS_FULL) {

                    Data realistRawData = new Data.Builder()
                            .putInt(BATTERY_STATUS, BatteryManager.BATTERY_STATUS_CHARGING)
                            .build();
                    OneTimeWorkRequest realist = new OneTimeWorkRequest.Builder(RealistOneTime.class)
                            .setInputData(realistRawData)
                            .build();
                    WorkManager.getInstance(context).enqueue(realist);
                }
            } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                Data realistRawData = new Data.Builder()
                        .putInt(BATTERY_STATUS, BatteryManager.BATTERY_STATUS_DISCHARGING)
                        .build();
                OneTimeWorkRequest realist = new OneTimeWorkRequest.Builder(RealistOneTime.class)
                        .setInputData(realistRawData)
                        .build();
                WorkManager.getInstance(context).enqueue(realist);
            }
        }
    }
}