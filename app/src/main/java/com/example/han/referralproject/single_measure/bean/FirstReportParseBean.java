package com.example.han.referralproject.single_measure.bean;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/1 16:24
 * created by:gzq
 * description:TODO
 */
public class FirstReportParseBean {
    private String factorName;
    private String reference;
    private String anomalyStatus;
    private String factorCode;
    private String fatherCode;
    private String hdFactorRecordId;
    private String hdHealthSurveyId;
    private int score;
    private int userId;
    private String value;

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAnomalyStatus() {
        return anomalyStatus;
    }

    public void setAnomalyStatus(String anomalyStatus) {
        this.anomalyStatus = anomalyStatus;
    }

    public String getFactorCode() {
        return factorCode;
    }

    public void setFactorCode(String factorCode) {
        this.factorCode = factorCode;
    }

    public String getFatherCode() {
        return fatherCode;
    }

    public void setFatherCode(String fatherCode) {
        this.fatherCode = fatherCode;
    }

    public String getHdFactorRecordId() {
        return hdFactorRecordId;
    }

    public void setHdFactorRecordId(String hdFactorRecordId) {
        this.hdFactorRecordId = hdFactorRecordId;
    }

    public String getHdHealthSurveyId() {
        return hdHealthSurveyId;
    }

    public void setHdHealthSurveyId(String hdHealthSurveyId) {
        this.hdHealthSurveyId = hdHealthSurveyId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
