package com.awfyn.drbattery.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.awfyn.drbattery.R;
import com.awfyn.drbattery.home.mdoels.InstalledApp;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAG_HomeFragment";
    private HomeViewModel viewModel;

    private ImageView ivBatteryLevel;

    // Remaining Times
    private MaterialTextView tvRemainingTime, tvBestRemainingTime, tvWorstRemainingTime;

    // BotButtons
    private ImageView ivTopUsageApps, ivOptimize, ivMode;

    // User Apps
    private ImageView tApp, trApp, rApp, rbApp, bApp, blApp, lApp, ltApp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        ivBatteryLevel = view.findViewById(R.id.homeBatteryLevel_gif);

        tvRemainingTime = view.findViewById(R.id.homeBatteryLevel_text);
        tvBestRemainingTime = view.findViewById(R.id.homeRemainingTime_best);
        tvWorstRemainingTime = view.findViewById(R.id.homeRemainingTime_worst);

        ivTopUsageApps = view.findViewById(R.id.homeButtons_topUsageApps);
        ivOptimize = view.findViewById(R.id.homeButtons_optimize);
        ivMode = view.findViewById(R.id.homeButtons_mode);

        tApp = view.findViewById(R.id.home_userApp_top);
        trApp = view.findViewById(R.id.home_userApp_topRight);
        rApp = view.findViewById(R.id.home_userApp_right);
        rbApp = view.findViewById(R.id.home_userApp_rightBot);
        bApp = view.findViewById(R.id.home_userApp_bot);
        blApp = view.findViewById(R.id.home_userApp_botLeft);
        lApp = view.findViewById(R.id.home_userApp_left);
        ltApp = view.findViewById(R.id.home_userApp_leftTop);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        viewModel.getRemainingTime().observe(getViewLifecycleOwner(), rt -> {
            if (rt.getH() == -1 || rt.getM() == -1)
                tvRemainingTime.setText("نیاز به کارکرد بیشتر");// TODO: Replace
            else
                tvRemainingTime.setText(rt.getH() + " ساعت و " + rt.getM() + " دقیقه");
        });
        viewModel.getBestRemainingTime().observe(getViewLifecycleOwner(), rt -> {
            if (rt.getH() == 0 && rt.getM() == 0)
                tvBestRemainingTime.setText("نیاز به کارکرد بیشتر");// TODO: Replace
            else
                tvBestRemainingTime.setText(rt.getH() + " ساعت و " + rt.getM() + " دقیقه");
        });
        viewModel.getWorstRemainingTime().observe(getViewLifecycleOwner(), rt -> {
            if (rt.getH() == 0 && rt.getM() == 0)
                tvWorstRemainingTime.setText("نیاز به کارکرد بیشتر");// TODO: Replace
            else
                tvWorstRemainingTime.setText(rt.getH() + " ساعت و " + rt.getM() + " دقیقه");
        });

        ivTopUsageApps.setOnClickListener(v -> startActivity(new Intent("android.intent.action.POWER_USAGE_SUMMARY")));
        ivOptimize.setOnClickListener(v -> {});
        ivMode.setOnClickListener(v -> {});

        List<InstalledApp> installedApps = viewModel.getInstalledApps();
        try {
            tApp.setImageDrawable(installedApps.get(0).getIcon());
        } catch (Exception ignore) {}
        try {
            bApp.setImageDrawable(installedApps.get(1).getIcon());
        } catch (Exception ignore) {}
        try {
            rApp.setImageDrawable(installedApps.get(2).getIcon());
        } catch (Exception ignore) {}
        try {
            lApp.setImageDrawable(installedApps.get(3).getIcon());
        } catch (Exception ignore) {}
        try {
            trApp.setImageDrawable(installedApps.get(4).getIcon());
        } catch (Exception ignore) {}
        try {
            ltApp.setImageDrawable(installedApps.get(5).getIcon());
        } catch (Exception ignore) {}
        try {
            rbApp.setImageDrawable(installedApps.get(6).getIcon());
        } catch (Exception ignore) {}
        try {
            blApp.setImageDrawable(installedApps.get(7).getIcon());
        } catch (Exception ignore) {}


//        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();// Importance > 100
//        Log.d(TAG, "onActivityCreated: " + runningAppProcessInfos.size());

//        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        final List list = getContext().getPackageManager().queryIntentActivities(intent, 0);// System = true
//        Log.d(TAG, "onActivityCreated: " + list.size());
    }

    @Override
    public void onResume() {
        super.onResume();

        float batteryLevel = viewModel.getCurrentBatteryLevel();
        if (batteryLevel <= 10)
            Glide.with(this).asGif().load(R.raw.home_battery_level_gif_10).into(ivBatteryLevel);
        else if (batteryLevel <= 40)
            Glide.with(this).asGif().load(R.raw.home_battery_level_gif_40).into(ivBatteryLevel);
        else if (batteryLevel <= 90)
            Glide.with(this).asGif().load(R.raw.home_battery_level_gif_70).into(ivBatteryLevel);
        else if (batteryLevel <= 100)
            Glide.with(this).asGif().load(R.raw.home_battery_level_gif_100).into(ivBatteryLevel);
    }
}
