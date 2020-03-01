package com.awfyn.drbattery.home.mdoels;

import android.graphics.drawable.Drawable;

public class InstalledApp {
    private String name;
    private Drawable icon;

    public InstalledApp(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() { return name; }
    public Drawable getIcon() { return icon; }

    public void setName(String name) { this.name = name; }
    public void setIcon(Drawable icon) { this.icon = icon; }
}