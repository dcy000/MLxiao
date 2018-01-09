package com.witspring.model.entity;

import java.io.Serializable;

/**
 * @author Created by wu_zf on 1/2/2018.
 * @email :wuzf1234@gmail.com
 */

public class UserInfo implements Serializable{

    private static final long serialVersionUID = 7587237895952339299L;

    private String nickName;
    private String birthDay;

    public static final int GENDER_NULL = -1;
    public static final int GENDER_MAN =  1;
    public static final int GENDER_WOMEN = 2;
    public static final int AGE_NULL = -1;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
}
