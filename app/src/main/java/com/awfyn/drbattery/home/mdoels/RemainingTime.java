package com.awfyn.drbattery.home.mdoels;

public class RemainingTime {
    private long h;
    private long m;

    public RemainingTime() {}

    public RemainingTime(long h, long m) {
        this.h = h;
        this.m = m;
    }

    public long getH() { return h; }
    public long getM() { return m; }

    public void setH(long h) { this.h = h; }
    public void setM(long m) { this.m = m; }
}
