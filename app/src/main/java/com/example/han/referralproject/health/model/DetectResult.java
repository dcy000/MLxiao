package com.example.han.referralproject.health.model;

/**
 * Created by lenovo on 2018/5/26.
 */

public class DetectResult {

    /**
     bloodOxygen (number, optional): 血氧饱和度（%） ,
     bloodSugar (number, optional): 血糖 ,
     cholesterol (number, optional): 胆固醇 ,
     diabetesSymptom (string, optional): 糖尿病症状 1无症状2多饮3多食4多尿5视力模糊6感染 7手脚麻木8下肢浮肿9 体重明显下降 ,
     doctorAdvice (string, optional): 医嘱 ,
     ecg (string, optional): 心电结果 ,
     equipmentId (string, optional): 机器id ,
     healthExaminationType (string, optional): 体检类型 0:健康体检 1：血压体检 2：血糖体检 ,
     healthSymptom (string, optional): 健康症状 1无症状 2头痛 3头晕 4心悸 5胸闷 6胸痛 7慢性咳嗽 8咳痰 9呼吸困难 10多饮 11多尿 12体重下降 13乏力 14关节肿痛15视力模糊16手脚麻木17尿急18尿痛 19便秘 20腹泻21恶心呕吐22眼花 23耳鸣 24乳房胀痛 25其他 ,
     heartRate (integer, optional): 心率 ,
     hiHealthExaminationId (integer, optional): 体检信息id ,
     highPressure (integer, optional): 收缩压（高） ,
     hypertensionSymptom (string, optional): 高血压症状 1无症状　2头痛头晕 3恶心呕吐　4眼花耳鸣 5呼吸困难　6心悸胸闷 7鼻衄出血不止8四肢发麻 9下肢水肿 ,
     lowPressure (integer, optional): 舒张压（低） ,
     pulse (integer, optional): 脉搏 ,
     saltIntake (string, optional): 食盐摄入 ,
     smoke (string, optional): 吸烟 ,
     sportCost (string, optional): 体育锻炼耗时 ,
     sportFrequency (string, optional): 体育锻炼频率 ,
     sportIntension (string, optional): 体育锻炼强度 ,
     sugarTime (integer, optional): 测血糖时间0: 空腹 1：饭后1小时 2：饭后2小时 ,
     temperAture (number, optional): 体温 ,
     uricAcid (number, optional): 血尿酸 ,
     userId (integer, optional): 用户ID ,
     watchState (string, optional): 是否观看过 0:否 1：是 ,
     weight (number, optional): 体重 ,
     wineDrink (string, optional): 饮酒
     */

    private float bloodOxygen;
    private float bloodSugar;
    private float cholesterol;
    private String diabetesSymptom;
    private String doctorAdvice;
    private String ecg;
    private String equipmentId;
    private String healthExaminationType;
    private String healthSymptom;
    private int heartRate;
    private String hiHealthExaminationId;
    private int highPressure;
    private String hypertensionSymptom;
    private int lowPressure;
    private int pulse;
    private String saltIntake;
    private String smoke;
    private String sportCost;
    private String sportFrequency;
    private String sportIntension;
    private int sugarTime;
    private String temperAture;
    private float uricAcid;
    private int userId;
    private String watchState;
    private float weight;
    private String wineDrink;

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

    public String getDiabetesSymptom() {
        return diabetesSymptom;
    }

    public void setDiabetesSymptom(String diabetesSymptom) {
        this.diabetesSymptom = diabetesSymptom;
    }

    public String getDoctorAdvice() {
        return doctorAdvice;
    }

    public void setDoctorAdvice(String doctorAdvice) {
        this.doctorAdvice = doctorAdvice;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getHealthExaminationType() {
        return healthExaminationType;
    }

    public void setHealthExaminationType(String healthExaminationType) {
        this.healthExaminationType = healthExaminationType;
    }

    public String getHealthSymptom() {
        return healthSymptom;
    }

    public void setHealthSymptom(String healthSymptom) {
        this.healthSymptom = healthSymptom;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public String getHiHealthExaminationId() {
        return hiHealthExaminationId;
    }

    public void setHiHealthExaminationId(String hiHealthExaminationId) {
        this.hiHealthExaminationId = hiHealthExaminationId;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(int highPressure) {
        this.highPressure = highPressure;
    }

    public String getHypertensionSymptom() {
        return hypertensionSymptom;
    }

    public void setHypertensionSymptom(String hypertensionSymptom) {
        this.hypertensionSymptom = hypertensionSymptom;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(int lowPressure) {
        this.lowPressure = lowPressure;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public String getSaltIntake() {
        return saltIntake;
    }

    public void setSaltIntake(String saltIntake) {
        this.saltIntake = saltIntake;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getSportCost() {
        return sportCost;
    }

    public void setSportCost(String sportCost) {
        this.sportCost = sportCost;
    }

    public String getSportFrequency() {
        return sportFrequency;
    }

    public void setSportFrequency(String sportFrequency) {
        this.sportFrequency = sportFrequency;
    }

    public String getSportIntension() {
        return sportIntension;
    }

    public void setSportIntension(String sportIntension) {
        this.sportIntension = sportIntension;
    }

    public int getSugarTime() {
        return sugarTime;
    }

    public void setSugarTime(int sugarTime) {
        this.sugarTime = sugarTime;
    }

    public String getTemperAture() {
        return temperAture;
    }

    public void setTemperAture(String temperAture) {
        this.temperAture = temperAture;
    }

    public float getUricAcid() {
        return uricAcid;
    }

    public void setUricAcid(float uricAcid) {
        this.uricAcid = uricAcid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWatchState() {
        return watchState;
    }

    public void setWatchState(String watchState) {
        this.watchState = watchState;
    }

    public float  getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getWineDrink() {
        return wineDrink;
    }

    public void setWineDrink(String wineDrink) {
        this.wineDrink = wineDrink;
    }

    @Override
    public String toString() {
        return "DetectResult{" +
                "bloodOxygen=" + bloodOxygen +
                ", bloodSugar=" + bloodSugar +
                ", cholesterol=" + cholesterol +
                ", diabetesSymptom='" + diabetesSymptom + '\'' +
                ", doctorAdvice='" + doctorAdvice + '\'' +
                ", ecg='" + ecg + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", healthExaminationType='" + healthExaminationType + '\'' +
                ", healthSymptom='" + healthSymptom + '\'' +
                ", heartRate=" + heartRate +
                ", hiHealthExaminationId=" + hiHealthExaminationId +
                ", highPressure=" + highPressure +
                ", hypertensionSymptom='" + hypertensionSymptom + '\'' +
                ", lowPressure=" + lowPressure +
                ", pulse=" + pulse +
                ", saltIntake='" + saltIntake + '\'' +
                ", smoke='" + smoke + '\'' +
                ", sportCost='" + sportCost + '\'' +
                ", sportFrequency='" + sportFrequency + '\'' +
                ", sportIntension='" + sportIntension + '\'' +
                ", sugarTime=" + sugarTime +
                ", temperAture=" + temperAture +
                ", uricAcid=" + uricAcid +
                ", userId=" + userId +
                ", watchState='" + watchState + '\'' +
                ", weight=" + weight +
                ", wineDrink='" + wineDrink + '\'' +
                '}';
    }
}
