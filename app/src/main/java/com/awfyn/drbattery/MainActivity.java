package com.awfyn.drbattery;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.awfyn.drbattery.oracle.OracleService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().addFlags(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            getWindow().addFlags(View.SYSTEM_UI_FLAG_FULLSCREEN);
            getWindow().addFlags(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Intent intent = new Intent(this, OracleService.class);
        ContextCompat.startForegroundService(this, intent);

//        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        registerReceiver(new ChargingLimitReceiver(), iFilter);
    }
}
