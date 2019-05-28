package com.gcml.auth.face2.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdLocation {
    /**
     * left : 117
     * top : 131
     * width : 172
     * height : 170
     * rotation : 4
     */

    @SerializedName("left")
    private double left;
    @SerializedName("top")
    private double top;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("rotation")
    private int rotation;

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
