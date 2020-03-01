package com.awfyn.drbattery.database.dao_s;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.awfyn.drbattery.database.entities.Mineral;
import com.awfyn.drbattery.database.entities.Vision;

import java.util.List;

@Dao
public interface OracleDao {
    @Insert
    void insertMineral(Mineral mineral);

    @Update
    void reInsertMineral(Mineral mineral);

    @Query("SELECT * FROM mineral WHERE calculated = 0")
    List<Mineral> getNotCalculatedMinerals();

    @Query("SELECT COUNT(*) FROM mineral")
    int getMineralsCount();

    @Query("SELECT * FROM mineral WHERE id = (SELECT MAX(id) FROM mineral)")
    Mineral getLastMineral();

    @Query("SELECT * FROM mineral WHERE id = :mineralId")
    Mineral getMineralById(int mineralId);

    @Query("SELECT * FROM mineral WHERE status = :status")
    List<Mineral> getMineralsByStatus(int status);

    @Insert
    void sketchVision(Vision vision);

    @Query("SELECT MAX(battery_life_ms_per_level) FROM vision")
    float getBestVision();

    @Query("SELECT MIN(battery_life_ms_per_level) FROM vision")
    float getWorstVision();

    @Query("SELECT * FROM vision WHERE status = 3")
    List<Vision> getDischargingForeSeens();

    @Query("SELECT COUNT(*) FROM Vision WHERE status = :status")
    int getForeSeensCountByStatus(int status);

    @Query("DELETE FROM vision")
    void nukeVisions();
}