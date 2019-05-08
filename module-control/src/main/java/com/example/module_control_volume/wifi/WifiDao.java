package com.example.module_control_volume.wifi;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WifiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWifiCache(WifiEntity cacheEntity);

    @Query("SELECT * FROM WifiCache WHERE `BSSID`=:bssid")
    WifiEntity queryByKey(String bssid);

    @Query("SELECT * FROM wificache")
    List<WifiEntity> getAllWifiCache();
}
