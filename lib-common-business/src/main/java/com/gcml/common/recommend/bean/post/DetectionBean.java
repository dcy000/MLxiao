package com.gcml.common.recommend.bean.post;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/9/21.
 */

public class DetectionBean implements Serializable {

    /**
     * bloodOxygen : 0
     * bloodSugar : 0
     * cholesterol : 0
     * detectionType : string
     * ecg : string
     * eqid : string
     * heartRate : 0
     * highPressure : 0
     * lowPressure : 0
     * offset : 0
     * pulse : 0
     * state : 0
     * sugarTime : 0
     * temperAture : 0
     * time : string
     * uricAcid : 0
     * userid : 0
     * weight : 0
     * yz : string
     * zid : 0
     */

    public Float bloodOxygen;
    public Float bloodSugar;
    public Float cholesterol;
    public String detectionType;
    public String ecg;
    public String eqid;
    public Integer heartRate;
    public Integer highPressure;
    public Integer lowPressure;
    public Integer offset;
    public Integer pulse;
    public Integer state;
    public Integer sugarTime;
    public Float temperAture;
    public String time;
    public Float uricAcid;
    public Integer userid;
    public Float weight;
    public String yz;
    public Integer zid;
}
