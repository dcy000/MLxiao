package com.example.han.referralproject.bluetooth_devices.base;

public class DiscoverDevicesSetting {
    private int discoverType;
    private String targetMac;
    private String targetName;

    public DiscoverDevicesSetting() {
    }

    public DiscoverDevicesSetting(int discoverType, String targetMac, String targetName) {
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
