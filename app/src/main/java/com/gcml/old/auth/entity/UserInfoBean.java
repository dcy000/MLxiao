package com.gcml.old.auth.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserInfoBean implements Serializable {
    public String bid;
    public String categoryid;
    public String doct;
    public String eq;
    public String bname;
    public String sex;
    public String dz;
    public String age;
    public String sfz;
    public String tel;
    public String mh;
    public String eqid;
    public String state;
    public String qyzt;
    public String height;
    public String weight;
    public String bloodType;
    public String eatingHabits;
    public String smoke;
    public String drink;
    public String exerciseHabits;
    @SerializedName("user_photo")
    public String userPhoto;
    public String xfid;
    public String hypertensionHand;
    public String wyyxId;
    public String wyyxPwd;

}
