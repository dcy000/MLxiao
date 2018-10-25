package com.gcml.auth.face.debug.model.entity;

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
    private int left;
    @SerializedName("top")
    private int top;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("rotation")
    private int rotation;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
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
