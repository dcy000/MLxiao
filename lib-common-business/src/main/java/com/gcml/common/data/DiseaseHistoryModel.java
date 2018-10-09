package com.gcml.common.data;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * Created by lenovo on 2017/10/13.
 */

public class DiseaseHistoryModel {
    private String name;
    private boolean selected;

    @ColorRes
    private int textColorSelected;

    @ColorRes
    private int textColorUnselected;

    @DrawableRes
    private int bgSelected;

    @DrawableRes
    private int bg;

    public DiseaseHistoryModel(
            String name,
            boolean selected,
            int textColorSelected,
            int textColorUnselected,
            int bgSelected,
            int bg) {
        this.name = name;
        this.selected = selected;
        this.textColorSelected = textColorSelected;
        this.textColorUnselected = textColorUnselected;
        this.bgSelected = bgSelected;
        this.bg = bg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getTextColorSelected() {
        return textColorSelected;
    }

    public void setTextColorSelected(int textColorSelected) {
        this.textColorSelected = textColorSelected;
    }

    public int getBgSelected() {
        return bgSelected;
    }

    public void setBgSelected(int bgSelected) {
        this.bgSelected = bgSelected;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getTextColorUnselected() {
        return textColorUnselected;
    }

    public void setTextColorUnselected(int textColorUnselected) {
        this.textColorUnselected = textColorUnselected;
    }
}
