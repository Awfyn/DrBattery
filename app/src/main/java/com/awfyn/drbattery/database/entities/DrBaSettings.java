package com.awfyn.drbattery.database.entities;

//@Entity(tableName = "dr_battery_settings")
public class DrBaSettings {

//    @PrimaryKey(autoGenerate = true)
    private int id;
//    @ColumnInfo(name = "foreground_service")
    private boolean foregroundService;
//    @ColumnInfo(name = "battery_max_level")
    private int batteryMaxChargeLevel;


    public DrBaSettings(int id, boolean foregroundService, int batteryMaxChargeLevel) {
        this.id = id;
        this.foregroundService = foregroundService;
        this.batteryMaxChargeLevel = batteryMaxChargeLevel;
    }

    public int getId() { return id; }
    public boolean isForegroundService() { return foregroundService; }
    public int getBatteryMaxChargeLevel() { return batteryMaxChargeLevel; }

    public void setId(int id) { this.id = id; }
    public void setForegroundService(boolean foregroundService) { this.foregroundService = foregroundService; }
    public void setBatteryMaxChargeLevel(int batteryMaxChargeLevel) { this.batteryMaxChargeLevel = batteryMaxChargeLevel; }
}
