package com.gcml.common.recommend.bean.post;

import com.gcml.common.utils.device.DeviceUtils;

import java.io.Serializable;

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
public class DetectionData implements Serializable {
    private Float bloodOxygen;
    private Float bloodSugar;
    private Float cholesterol;
    private String detectionType;
    private String ecg;
    private String eqid = DeviceUtils.getIMEI();
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
    private Boolean weightOver;
    private String yz;
    private Integer zid;
    private String result;
    private String resultUrl;
    private String breathHome;
    private String ecgDataString;
    private String ecgTips;
    private Integer ecgFlag;
    private Boolean isInit;

    public DetectionData() {

    }

    public Float getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(Float bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public Float getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Float bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Float cholesterol) {
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

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(Integer highPressure) {
        this.highPressure = highPressure;
    }

    public Integer getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(Integer lowPressure) {
        this.lowPressure = lowPressure;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getSugarTime() {
        return sugarTime;
    }

    public void setSugarTime(Integer sugarTime) {
        this.sugarTime = sugarTime;
    }

    public Float getTemperAture() {
        return temperAture;
    }

    public void setTemperAture(Float temperAture) {
        this.temperAture = temperAture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getUricAcid() {
        return uricAcid;
    }

    public void setUricAcid(Float uricAcid) {
        this.uricAcid = uricAcid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean isWeightOver() {
        return weightOver;
    }

    public void setWeightOver(boolean weightOver) {
        this.weightOver = weightOver;
    }

    public String getYz() {
        return yz;
    }

    public void setYz(String yz) {
        this.yz = yz;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public String getBreathHome() {
        return breathHome;
    }

    public void setBreathHome(String breathHome) {
        this.breathHome = breathHome;
    }

    public String getEcgDataString() {
        return ecgDataString;
    }

    public void setEcgDataString(String ecgDataString) {
        this.ecgDataString = ecgDataString;
    }

    public String getEcgTips() {
        return ecgTips;
    }

    public void setEcgTips(String ecgTips) {
        this.ecgTips = ecgTips;
    }

    public int getEcgFlag() {
        return ecgFlag;
    }

    public void setEcgFlag(int ecgFlag) {
        this.ecgFlag = ecgFlag;
    }

    public Boolean isInit() {
        return isInit;
    }

    public void setInit(Boolean init) {
        isInit = init;
    }
}
