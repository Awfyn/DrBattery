package com.awfyn.drbattery.home;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.awfyn.drbattery.database.DrDatabase;
import com.awfyn.drbattery.database.dao_s.OracleDao;
import com.awfyn.drbattery.oracle.Oracle;

class HomeRepo {
    private static final String TAG = "TAG_HomeRepository";

    private Intent batteryStatus;
    private OracleDao oracleDao;
    private Oracle oracle;


    HomeRepo(Application application) {
        DrDatabase database = DrDatabase.getInstance(application);
        oracleDao = database.oracleDao();
        oracle = new Oracle();

        oracleDao.nukeVisions();

        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = application.registerReceiver(null, batteryFilter);
    }

    void getRemainingTime(Oracle.FalsePromise falsePromise){
        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        oracle.getRemainingTime(falsePromise, oracleDao, level * 100 / (float)scale);
    }

    void getBestRemainingTime(Oracle.FalsePromise falsePromise) {
        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        oracle.getBestRemainingTime(falsePromise, oracleDao, level * 100 / (float)scale);
    }

    void getWorstRemainingTime(Oracle.FalsePromise falsePromise) {
        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        oracle.getWorstRemainingTime(falsePromise, oracleDao, level * 100 / (float)scale);
    }
}