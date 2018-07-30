package com.example.han.referralproject.health.intelligentdetection.entity;

import com.example.han.referralproject.util.Utils;

//    TDate {
//        bloodOxygen (number, optional): 血氧（%） ,
//        bloodSugar (number, optional): 血糖 ,
//        cholesterol (number, optional): 胆固醇 ,
//        detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
//        ecg (string, optional): 心电结果 ,
//        eqid (string, optional): 机器id ,
//        heartRate (integer, optional): 心率 ,
//        highPressure (integer, optional): 高压 ,
//        lowPressure (integer, optional): 低压 ,
//        offset (integer, optional): 偏移量状态字 ,
//        pulse (integer, optional): 脉搏 ,
//        state (integer, optional): 是否观看过 ,
//        sugarTime (integer, optional): 测血糖时间 ,
//        temperAture (number, optional): 温度 ,
//        time (string, optional): 测量时间 ,
//        uricAcid (number, optional): 血尿酸 ,
//        userid (integer, optional): 患者id ,
//        weight (number, optional): 体重 ,
//        yz (string, optional): 医嘱 ,
//        zid (integer, optional): 数据信息id
//    }
public class DetectionData {
    private Float bloodOxygen;
    private Float bloodSugar;
    private Float cholesterol;
    private String detectionType;
    private String ecg;
    private String eqid = Utils.getDeviceId();
    private Integer heartRate;
    private Integer highPressure;
    private Integer lowPressure;
    private Integer offset;
    private Integer pulse;
    private Integer state;
    private Integer sugarTime;
    private Float temperAture;
    private String time = String.valueOf(System.currentTimeMillis());
    private Float uricAcid;
    private Integer userid;
    private Float weight;
    private String yz;
    private Integer zid;

    public DetectionData() {

    }

    public float getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(float bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public float getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(float bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(String detectionType) {
        this.detectionType = detectionType;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(int highPressure) {
        this.highPressure = highPressure;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(int lowPressure) {
        this.lowPressure = lowPressure;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSugarTime() {
        return sugarTime;
    }

    public void setSugarTime(int sugarTime) {
        this.sugarTime = sugarTime;
    }

    public float getTemperAture() {
        return temperAture;
    }

    public void setTemperAture(float temperAture) {
        this.temperAture = temperAture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getUricAcid() {
        return uricAcid;
    }

    public void setUricAcid(float uricAcid) {
        this.uricAcid = uricAcid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getYz() {
        return yz;
    }

    public void setYz(String yz) {
        this.yz = yz;
    }

    public int getZid() {
        return zid;
    }

    public void setZid(int zid) {
        this.zid = zid;
    }
}
