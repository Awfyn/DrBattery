package com.awfyn.drbattery.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "vision")
public class Vision {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int status;
    @ColumnInfo(name = "battery_level")
    private float batteryLevel;
    @ColumnInfo(name = "battery_life_ms_per_level")
    private float batteryLifeMsPerLevel;

    public Vision(int id, int status, float batteryLevel, float batteryLifeMsPerLevel) {
        this.id = id;
        this.status = status;
        this.batteryLevel = batteryLevel;
        this.batteryLifeMsPerLevel = batteryLifeMsPerLevel;
    }

    @Ignore
    public Vision() {}

    @Ignore
    public Vision(int status, float batteryLevel, int batteryLifeMsPerLevel) {
        this.status = status;
        this.batteryLevel = batteryLevel;
        this.batteryLifeMsPerLevel = batteryLifeMsPerLevel;
    }

    public int getId() { return id; }
    public int getStatus() { return status; }
    public float getBatteryLevel() { return batteryLevel; }
    public float getBatteryLifeMsPerLevel() { return batteryLifeMsPerLevel; }

    public void setId(int id) { this.id = id; }
    public void setStatus(int status) { this.status = status; }
    public void setBatteryLevel(int batteryLevel) { this.batteryLevel = batteryLevel; }
    public void setBatteryLifeMsPerLevel(float batteryLifeMsPerLevel) { this.batteryLifeMsPerLevel = batteryLifeMsPerLevel; }
}
