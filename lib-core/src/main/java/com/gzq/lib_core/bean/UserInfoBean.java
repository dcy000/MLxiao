package com.gzq.lib_core.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "user")
public class UserInfoBean implements Parcelable {
    @NonNull
    @PrimaryKey
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
    public String doid;
    public String wyyxPwd;
    @Ignore
    public String password;

    public UserInfoBean() {

    }


    protected UserInfoBean(Parcel in) {
        bid = in.readString();
        categoryid = in.readString();
        doct = in.readString();
        eq = in.readString();
        bname = in.readString();
        sex = in.readString();
        dz = in.readString();
        age = in.readString();
        sfz = in.readString();
        tel = in.readString();
        mh = in.readString();
        eqid = in.readString();
        state = in.readString();
        qyzt = in.readString();
        height = in.readString();
        weight = in.readString();
        bloodType = in.readString();
        eatingHabits = in.readString();
        smoke = in.readString();
        drink = in.readString();
        exerciseHabits = in.readString();
        userPhoto = in.readString();
        xfid = in.readString();
        hypertensionPrimaryState = in.readString();
        wyyxId = in.readString();
        doid = in.readString();
        wyyxPwd = in.readString();
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel in) {
            return new UserInfoBean(in);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "bid=" + bid +
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
                ", doid='" + doid + '\'' +
                ", wyyxPwd='" + wyyxPwd + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bid);
        dest.writeString(categoryid);
        dest.writeString(doct);
        dest.writeString(eq);
        dest.writeString(bname);
        dest.writeString(sex);
        dest.writeString(dz);
        dest.writeString(age);
        dest.writeString(sfz);
        dest.writeString(tel);
        dest.writeString(mh);
        dest.writeString(eqid);
        dest.writeString(state);
        dest.writeString(qyzt);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(bloodType);
        dest.writeString(eatingHabits);
        dest.writeString(smoke);
        dest.writeString(drink);
        dest.writeString(exerciseHabits);
        dest.writeString(userPhoto);
        dest.writeString(xfid);
        dest.writeString(hypertensionPrimaryState);
        dest.writeString(wyyxId);
        dest.writeString(doid);
        dest.writeString(wyyxPwd);
    }
}
