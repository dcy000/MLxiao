package com.gcml.module_blutooth_devices.breathhome;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/18 15:28
 * created by:gzq
 * description:TODO
 */
public class BreathHomeRequestConnectBean {
    private String actionHead;
    private String deviceType;
    private String imei;
    private String bluetoothVersion;
    private String channelNum;
    private String msgLength;
    private String checkCode;

    public String getActionHead() {
        return actionHead;
    }

    public void setActionHead(String actionHead) {
        this.actionHead = actionHead;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBluetoothVersion() {
        return bluetoothVersion;
    }

    public void setBluetoothVersion(String bluetoothVersion) {
        this.bluetoothVersion = bluetoothVersion;
    }

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum;
    }

    public String getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(String msgLength) {
        this.msgLength = msgLength;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public String toString() {
        return "BreathHomeRequestConnectBean{" +
                "actionHead='" + actionHead + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", imei='" + imei + '\'' +
                ", bluetoothVersion='" + bluetoothVersion + '\'' +
                ", channelNum='" + channelNum + '\'' +
                ", msgLength='" + msgLength + '\'' +
                ", checkCode='" + checkCode + '\'' +
                '}';
    }
}
