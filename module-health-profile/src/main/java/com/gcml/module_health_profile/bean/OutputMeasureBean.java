package com.gcml.module_health_profile.bean;

public class OutputMeasureBean {

    /**
     * anomalyStatus : string
     * name : string
     * reference : string
     * value : string
     */

    private String anomalyStatus;
    private String name;
    private String reference;
    private String value;

    public String getAnomalyStatus() {
        return anomalyStatus;
    }

    public void setAnomalyStatus(String anomalyStatus) {
        this.anomalyStatus = anomalyStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
