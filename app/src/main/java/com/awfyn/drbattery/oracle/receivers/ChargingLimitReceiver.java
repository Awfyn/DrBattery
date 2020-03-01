package com.awfyn.drbattery.oracle.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;

public class ChargingLimitReceiver extends BroadcastReceiver {
    private static final String TAG = "TAG_ChargeLimit";

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent != null ? intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = intent != null ? intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
        int currentBatteryLevel = level * 100 / scale;

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);
        int status = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) : -1;

        if (currentBatteryLevel == 80 && status == BatteryManager.BATTERY_STATUS_CHARGING) {
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
