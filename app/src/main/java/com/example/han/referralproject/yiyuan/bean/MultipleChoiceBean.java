package com.example.han.referralproject.yiyuan.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/5/26.
 */

public class MultipleChoiceBean implements Serializable {
    public String title;
    public String itemName;
    public boolean isSelected;
    public boolean isTitle;
    public boolean isMultipleChoice;


    public MultipleChoiceBean setMultipleChoice(boolean multipleChoice) {
        isMultipleChoice = multipleChoice;
        return this;
    }

    public boolean isTitle() {
        return isTitle;

    }

    public MultipleChoiceBean setTitle(boolean title) {
        isTitle = title;
        return this;

    }

    public String getTitle() {
        return title;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public MultipleChoiceBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public MultipleChoiceBean setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public MultipleChoiceBean setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }
}
