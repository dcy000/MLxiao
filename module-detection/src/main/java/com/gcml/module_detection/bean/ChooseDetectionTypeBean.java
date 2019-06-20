package com.gcml.module_detection.bean;

public class ChooseDetectionTypeBean {
    private int icon;
    private String title;
    private String date;
    private String result;
    private String result2;
    private String unit;
    private boolean isNormal;
    private boolean isNormal2;

    public ChooseDetectionTypeBean() {
    }

    public ChooseDetectionTypeBean(int icon, String title, String date, String result, String unit) {
        this.icon = icon;
        this.title = title;
        this.date = date;
        this.result = result;
        this.unit = unit;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public boolean isNormal2() {
        return isNormal2;
    }

    public void setNormal2(boolean normal2) {
        isNormal2 = normal2;
    }

    public String getResult2() {
        return result2;
    }

    public void setResult2(String result2) {
        this.result2 = result2;
    }
}
