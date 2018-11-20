package com.example.han.referralproject.bean;

import java.io.Serializable;

public class UserInfoBean implements Serializable{
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
    public String userPhoto;
    public String xfid;
    public String hypertensionPrimaryState;
    public String wyyxId;

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "bid='" + bid + '\'' +
                ", categoryid='" + categoryid + '\'' +
                ", doct='" + doct + '\'' +
                ", eq='" + eq + '\'' +
                ", bname='" + bname + '\'' +
                ", sex='" + sex + '\'' +
                ", dz='" + dz + '\'' +
                ", age='" + age + '\'' +
                ", sfz='" + sfz + '\'' +
                ", tel='" + tel + '\'' +
                ", mh='" + mh + '\'' +
                ", eqid='" + eqid + '\'' +
                ", state='" + state + '\'' +
                ", qyzt='" + qyzt + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", eatingHabits='" + eatingHabits + '\'' +
                ", smoke='" + smoke + '\'' +
                ", drink='" + drink + '\'' +
                ", exerciseHabits='" + exerciseHabits + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", xfid='" + xfid + '\'' +
                ", hypertensionPrimaryState='" + hypertensionPrimaryState + '\'' +
                ", wyyxId='" + wyyxId + '\'' +
                '}';
    }
}
