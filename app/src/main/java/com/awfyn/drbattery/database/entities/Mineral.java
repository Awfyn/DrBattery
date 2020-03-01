package com.awfyn.drbattery.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "mineral")
public class Mineral {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private float level;
    @ColumnInfo(name = "time_stamp")
    private long timeStamp;
    private int temperature, voltage, status, health;
    private boolean calculated;

    public Mineral(int id, float level, long timeStamp, int temperature, int voltage, int status, int health, boolean calculated) {
        this.id = id;
        this.level = level;
        this.timeStamp = timeStamp;
        this.temperature = temperature;
        this.voltage = voltage;
        this.status = status;
        this.health = health;
        this.calculated = calculated;
    }

    @Ignore
    public Mineral() {}

    @Ignore
    public Mineral(float level, String timeStamp, int temperature, int voltage, int status, int health, boolean calculated) {
        this.level = level;
        this.timeStamp = Long.parseLong(timeStamp);
        this.temperature = temperature;
        this.voltage = voltage;
        this.status = status;
        this.health = health;
        this.calculated = calculated;
    }

    public int getId() { return id; }
    public float getLevel() { return level; }
    public long getTimeStamp() { return timeStamp; }
    public int getTemperature() { return temperature; }
    public int getVoltage() { return voltage; }
    public int getStatus() { return status; }
    public int getHealth() { return health; }
    public boolean isCalculated() { return calculated; }

    public void setId(int id) { this.id = id; }
    public void setLevel(float level) { this.level = level; }
    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }
    public void setTemperature(int temperature) { this.temperature = temperature; }
    public void setVoltage(int voltage) { this.voltage = voltage; }
    public void setStatus(int status) { this.status = status; }
    public void setHealth(int health) { this.health = health; }
    public void setCalculated(boolean calculated) { this.calculated = calculated; }
}