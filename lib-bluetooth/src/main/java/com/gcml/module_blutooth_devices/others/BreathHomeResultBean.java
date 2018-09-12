package com.gcml.module_blutooth_devices.others;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/20 14:32
 * created by:gzq
 * description:呼吸家返回的测量数据
 */
public class BreathHomeResultBean {
    private String actionHead;
    private String deviceType;
    private String imei;
    private String bluetoothVersion;
    private String channelNum;
    private String msgLength;
    private String b1Save;
    private String sendN;
    private String b1SaveTime;
    private String sex;
    private String age;
    private String height;
    private String weight;
    private String pef;//L/min 整型
    private String fev1;//浮点数
    private String fvc;//浮点数
    private String mef75;//浮点数（L/s）
    private String mef50;//浮点数（L/s）
    private String mef25;//浮点数（L/s）
    private String mmef;//浮点数（L/s）
    private String reserve;//预留值
    private String curve;//曲线数据，需要转成byte,再转int。总共600组数据
    private String checkCode;//检验码

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

    public String getB1Save() {
        return b1Save;
    }

    public void setB1Save(String b1Save) {
        this.b1Save = b1Save;
    }

    public String getSendN() {
        return sendN;
    }

    public void setSendN(String sendN) {
        this.sendN = sendN;
    }

    public String getB1SaveTime() {
        return b1SaveTime;
    }

    public void setB1SaveTime(String b1SaveTime) {
        this.b1SaveTime = b1SaveTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPef() {
        return pef;
    }

    public void setPef(String pef) {
        this.pef = pef;
    }

    public String getFev1() {
        return fev1;
    }

    public void setFev1(String fev1) {
        this.fev1 = fev1;
    }

    public String getFvc() {
        return fvc;
    }

    public void setFvc(String fvc) {
        this.fvc = fvc;
    }

    public String getMef75() {
        return mef75;
    }

    public void setMef75(String mef75) {
        this.mef75 = mef75;
    }

    public String getMef50() {
        return mef50;
    }

    public void setMef50(String mef50) {
        this.mef50 = mef50;
    }

    public String getMef25() {
        return mef25;
    }

    public void setMef25(String mef25) {
        this.mef25 = mef25;
    }

    public String getMmef() {
        return mmef;
    }

    public void setMmef(String mmef) {
        this.mmef = mmef;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public String toString() {
        return "BreathHomeResultBean{" +
                "actionHead='" + actionHead + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", imei='" + imei + '\'' +
                ", bluetoothVersion='" + bluetoothVersion + '\'' +
                ", channelNum='" + channelNum + '\'' +
                ", msgLength='" + msgLength + '\'' +
                ", b1Save='" + b1Save + '\'' +
                ", sendN='" + sendN + '\'' +
                ", b1SaveTime='" + b1SaveTime + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", pef='" + pef + '\'' +
                ", pev1='" + fev1 + '\'' +
                ", fvc='" + fvc + '\'' +
                ", mef75='" + mef75 + '\'' +
                ", mef50='" + mef50 + '\'' +
                ", mef25='" + mef25 + '\'' +
                ", mmef='" + mmef + '\'' +
                ", reserve='" + reserve + '\'' +
                ", curve='" + curve + '\'' +
                ", checkCode='" + checkCode + '\'' +
                '}';
    }
}
