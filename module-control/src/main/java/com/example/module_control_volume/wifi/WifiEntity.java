package com.example.module_control_volume.wifi;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "WifiCache")
public class WifiEntity {
    @PrimaryKey
    @NonNull
    public String BSSID;
    @NonNull
    public String SSID;

    @NonNull
    public int frequency;
    public String password;
    public String capabilities;
}
