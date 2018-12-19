package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdAngle {
    /**
     * pitch : -1.240876436
     * yaw : 0.2984000444
     * roll : 2.373161793
     */

    @SerializedName("pitch")
    private double pitch;
    @SerializedName("yaw")
    private double yaw;
    @SerializedName("roll")
    private double roll;

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }
}
