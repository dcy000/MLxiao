package com.example.module_child_edu.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gzq on 2018/2/7.
 */

public class RadioEntity {

    @SerializedName("wr")
    private String name;
    @SerializedName("content")
    private String fm;

    private boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFm() {
        return fm;
    }

    public void setFm(String fm) {
        this.fm = fm;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "RadioEntity{" +
                "name='" + name + '\'' +
                ", fm='" + fm + '\'' +
                '}';
    }
}
