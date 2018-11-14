package com.example.han.referralproject.bean;

import com.google.gson.annotations.SerializedName;

public class DetectionResult {
    @SerializedName("date")
    private DetectionData data;
    private String diagnose;
    private String result;
    private Integer score;

    public DetectionResult() {

    }

    public DetectionData getData() {
        return data;
    }

    public void setData(DetectionData data) {
        this.data = data;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
