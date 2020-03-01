package com.awfyn.drbattery.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.awfyn.drbattery.database.dao_s.OracleDao;
import com.awfyn.drbattery.database.entities.Mineral;
import com.awfyn.drbattery.database.entities.Vision;

@Database(entities = {Mineral.class, Vision.class}, version = 1)
public abstract class DrDatabase extends RoomDatabase {
    private static final String DR_DATABASE_NAME = "dr_database";

    private static DrDatabase instance;
    public abstract OracleDao oracleDao();
    //TODO: Create Dao(s)

    public static synchronized DrDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DrDatabase.class, DR_DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()//TODO: Remove
                    .build();
        }
        return instance;
    }

    private static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE vision");
            database.execSQL("CREATE TABLE vision (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, status INTEGER NOT NULL, battery_level REAL NOT NULL, battery_life_ms_per_level REAL NOT NULL)");
        }
    };
}
