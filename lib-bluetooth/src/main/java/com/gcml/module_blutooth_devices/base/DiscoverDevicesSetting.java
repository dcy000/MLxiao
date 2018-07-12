package com.gcml.module_blutooth_devices.base;

import android.support.annotation.NonNull;

public class DiscoverDevicesSetting {
    private int discoverType;
    private String targetMac;
    private String targetName;

    public DiscoverDevicesSetting() {
    }

    public DiscoverDevicesSetting(int discoverType, @NonNull String targetName) {
        this.discoverType = discoverType;
        this.targetName = targetName;
    }

    public DiscoverDevicesSetting(int discoverType, @NonNull String targetMac, @NonNull String targetName) {
        this.discoverType = discoverType;
        this.targetMac = targetMac;
        this.targetName = targetName;
    }


    public int getDiscoverType() {
        return discoverType;
    }

    public void setDiscoverType(int discoverType) {
        this.discoverType = discoverType;
    }

    public String getTargetMac() {
        return targetMac;
    }

    public void setTargetMac(String targetMac) {
        this.targetMac = targetMac;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
