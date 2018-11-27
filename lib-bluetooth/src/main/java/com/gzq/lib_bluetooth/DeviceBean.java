package com.gzq.lib_bluetooth;

public class DeviceBean {
    private String name;
    private String address;
    private DeviceType deviceType;

    public DeviceBean() {
    }

    public DeviceBean(String name, String address, DeviceType deviceType) {
        this.name = name;
        this.address = address;
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
