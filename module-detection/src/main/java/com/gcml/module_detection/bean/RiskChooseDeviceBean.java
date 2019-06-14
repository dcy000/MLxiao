package com.gcml.module_detection.bean;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/30 11:06
 * created by:gzq
 * description:TODO
 */
public class RiskChooseDeviceBean {
    private int imageNormal;
    private boolean isChoosed;
    private String deviceName;
    private int deviceLevel;

    public int getDeviceLevel() {
        return deviceLevel;
    }

    public void setDeviceLevel(int deviceLevel) {
        this.deviceLevel = deviceLevel;
    }

    public int getImageNormal() {
        return imageNormal;
    }

    public void setImageNormal(int imageNormal) {
        this.imageNormal = imageNormal;
    }

    public boolean getChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public RiskChooseDeviceBean() {
    }

    public RiskChooseDeviceBean(int imageNormal, boolean isChoosed, String deviceName, int deviceLevel) {
        this.imageNormal = imageNormal;
        this.isChoosed = isChoosed;
        this.deviceName = deviceName;
        this.deviceLevel=deviceLevel;
    }

    @Override
    public String toString() {
        return "ChooseDeviceBean{" +
                "imageNormal=" + imageNormal +
                ", isChoosed=" + isChoosed +
                ", deviceName='" + deviceName + '\'' +
                ", deviceLevel=" + deviceLevel +
                '}';
    }
}
