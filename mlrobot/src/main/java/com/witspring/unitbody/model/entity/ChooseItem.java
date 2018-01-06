package com.witspring.unitbody.model.entity;

import com.witspring.model.entity.Result;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 用于单选或者多选的checkbox组件的vo
 * Created by chenqiang on 16/3/8.
 */
public class ChooseItem implements Serializable {

    private static final long serialVersionUID = -7694864156086659714L;
    private boolean checked;// 是否被选中
    private String name;//显示的名称
    private boolean isDisable;//是否不可操作
    private int exclusive;//是否具有排他性。0是没有，1是有（如以上都不是）

    public ChooseItem(boolean checked, String name, boolean isDisable) {
        this.checked = checked;
        this.name = name;
        this.isDisable = isDisable;
    }

    public ChooseItem(boolean checked, String name, boolean isDisable, int exclusive) {
        this.checked = checked;
        this.name = name;
        this.isDisable = isDisable;
        this.exclusive = exclusive;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public int getExclusive() {
        return exclusive;
    }

    public void setExclusive(int exclusive) {
        this.exclusive = exclusive;
    }
}

