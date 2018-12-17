package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdThresholds {
    /**
     * frr_1e-2 : 0.9
     * frr_1e-3 : 0.3
     * frr_1e-4 : 0.05
     */

    @SerializedName("frr_1e-2")
    private double frr1e2;
    @SerializedName("frr_1e-3")
    private double frr1e3;
    @SerializedName("frr_1e-4")
    private double frr1e4;

    public double getFrr1e2() {
        return frr1e2;
    }

    public void setFrr1e2(double frr1e2) {
        this.frr1e2 = frr1e2;
    }

    public double getFrr1e3() {
        return frr1e3;
    }

    public void setFrr1e3(double frr1e3) {
        this.frr1e3 = frr1e3;
    }

    public double getFrr1e4() {
        return frr1e4;
    }

    public void setFrr1e4(double frr1e4) {
        this.frr1e4 = frr1e4;
    }
}
