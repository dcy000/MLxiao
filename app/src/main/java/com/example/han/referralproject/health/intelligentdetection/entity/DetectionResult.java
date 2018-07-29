package com.example.han.referralproject.health.intelligentdetection.entity;

import com.google.gson.annotations.SerializedName;

public class DetectionResult {
    @SerializedName("date")
    private DetectionData data;
    private String diagnose;
    private String result;

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
}
