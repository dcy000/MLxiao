package com.example.han.referralproject.bean;

import android.text.TextUtils;

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
    public String blood_type;
    public String eating_habits;
    public String smoke;
    public String drink;
    public String exercise_habits;
    public String user_photo;
    public String xfid;

    public boolean isFullInfo() {
        return !TextUtils.isEmpty(bid)
//                && !TextUtils.isEmpty(categoryid)
//                && !TextUtils.isEmpty(doct)
//                && !TextUtils.isEmpty(eq)
                && !TextUtils.isEmpty(bname)
                && !TextUtils.isEmpty(sex)
                && !TextUtils.isEmpty(dz)
//                && !TextUtils.isEmpty(age)
                && !TextUtils.isEmpty(sfz)
                && !TextUtils.isEmpty(tel)
                && !TextUtils.isEmpty(mh)
//                && !TextUtils.isEmpty(eqid)
//                && !TextUtils.isEmpty(state)
//                && !TextUtils.isEmpty(qyzt)
                && !TextUtils.isEmpty(height)
                && !TextUtils.isEmpty(weight)
                && !TextUtils.isEmpty(blood_type)
                && !TextUtils.isEmpty(eating_habits)
                && !TextUtils.isEmpty(smoke)
                && !TextUtils.isEmpty(drink)
//                && !TextUtils.isEmpty(user_photo)
//                && !TextUtils.isEmpty(xfid)
                && !TextUtils.isEmpty(exercise_habits);
    }
}
