package com.gcml.common.data;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * Created by lenovo on 2017/10/13.
 */

public class EatModel {
    private String eatType;

    @DrawableRes
    private int imgRes;

    @ColorRes
    private int selectedColor;

    @DrawableRes
    private int bgSelected;

    @DrawableRes
    private int bg;

    private boolean selected;

    public EatModel(String eatType, int imgRes, int selectedColor, int bgSelected, int bg) {
        this.eatType = eatType;
        this.imgRes = imgRes;
        this.selectedColor = selectedColor;
        this.bgSelected = bgSelected;
        this.bg = bg;
    }

    public String getEatType() {
        return eatType;
    }

    public void setEatType(String eatType) {
        this.eatType = eatType;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
