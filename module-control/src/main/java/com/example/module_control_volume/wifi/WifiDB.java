package com.example.module_control_volume.wifi;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
@Database(entities = {WifiEntity.class},version = 2,exportSchema = false)
public abstract class WifiDB extends RoomDatabase {
    public abstract WifiDao wifiDao();
}
