package com.gcml.module_blutooth_devices.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/11/6.
 */

public class BoShengUserBean implements Serializable{
    private String name;
    //格式:201801012
    private String birthday;
    //格式:男或者女
    private String sex;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
