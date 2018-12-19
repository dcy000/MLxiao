package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdExpression {
    /**
     * type : none
     * probability : 0.9999980927
     */

    @SerializedName("type")
    private String type;
    @SerializedName("probability")
    private double probability;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
