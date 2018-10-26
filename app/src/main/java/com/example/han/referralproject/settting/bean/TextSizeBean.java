package com.example.han.referralproject.settting.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/3/23.
 */

public class TextSizeBean implements Serializable{
    public boolean check;
    public String name;
    public String size;

    public TextSizeBean(boolean check, String name, String size) {
        this.check = check;
        this.name = name;
        this.size = size;
    }
}
