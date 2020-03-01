package com.awfyn.drbattery.home;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import com.awfyn.drbattery.home.mdoels.InstalledApp;
import com.awfyn.drbattery.home.mdoels.RemainingTime;
import com.awfyn.drbattery.oracle.Oracle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "TAG_HomeViewModel";

    private HomeRepo repository;
    private MediatorLiveData<RemainingTime> remainingTime = new MediatorLiveData<>();
    private MediatorLiveData<RemainingTime> bestRemainingTime = new MediatorLiveData<>();
    private MediatorLiveData<RemainingTime> worstRemainingTime = new MediatorLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new HomeRepo(application);

        setRemainingTime();
        setBestRemainingTime();
        setWorstRemainingTime();
    }

    MediatorLiveData<RemainingTime> getRemainingTime() { return remainingTime; }
    MediatorLiveData<RemainingTime> getBestRemainingTime() { return bestRemainingTime; }
    MediatorLiveData<RemainingTime> getWorstRemainingTime() { return worstRemainingTime; }

    private void setRemainingTime(){
        repository.getRemainingTime(new Oracle.FalsePromise() {
            @Override
            public void healed(float remainingTimeMs) {
                long remainingTimeS = TimeUnit.MILLISECONDS.toSeconds((long) remainingTimeMs);
                RemainingTime time = new RemainingTime();
                time.setH(TimeUnit.SECONDS.toHours(remainingTimeS));
                time.setM(TimeUnit.SECONDS.toMinutes(remainingTimeS - (time.getH() * 3600)));
                remainingTime.postValue(time);
            }

            @Override
            public void died() {
                remainingTime.postValue(new RemainingTime(-1, -1));
            }
        });
    }

    private void setBestRemainingTime() {
        repository.getBestRemainingTime(new Oracle.FalsePromise() {
            @Override
            public void healed(float remainingTimeMs) {
                long remainingTimeS = TimeUnit.MILLISECONDS.toSeconds((long) remainingTimeMs);
                RemainingTime time = new RemainingTime();
                time.setH(TimeUnit.SECONDS.toHours(remainingTimeS));
                time.setM(TimeUnit.SECONDS.toMinutes(remainingTimeS - (time.getH() * 3600)));
                bestRemainingTime.postValue(time);
            }

            @Override
            public void died() {
                remainingTime.postValue(new RemainingTime(-1, -1));
            }
        });
    }

    private void setWorstRemainingTime() {
        repository.getWorstRemainingTime(new Oracle.FalsePromise() {
            @Override
            public void healed(float remainingTimeMs) {
                long remainingTimeS = TimeUnit.MILLISECONDS.toSeconds((long) remainingTimeMs);
                RemainingTime time = new RemainingTime();
                time.setH(TimeUnit.SECONDS.toHours(remainingTimeS));
                time.setM(TimeUnit.SECONDS.toMinutes(remainingTimeS - (time.getH() * 3600)));
                worstRemainingTime.postValue(time);
            }

            @Override
            public void died() {
                remainingTime.postValue(new RemainingTime(-1, -1));
            }
        });
    }

    float getCurrentBatteryLevel() {
        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplication().registerReceiver(null, batteryFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
        if (level != -1 && scale != -1)
            return level * 100 / (float)scale;
        else
            return -1;
    }

    List<InstalledApp> getInstalledApps() {
        List<InstalledApp> res = new ArrayList<>();
        List<PackageInfo> packs = getApplication().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (!(((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                    || p.packageName.equals(getApplication().getPackageName()))) {
                String appName = p.applicationInfo.loadLabel(getApplication().getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getApplication().getPackageManager());
                res.add(new InstalledApp(appName, icon));
            }
        }
        Collections.shuffle(res);
        return res;
    }
}
