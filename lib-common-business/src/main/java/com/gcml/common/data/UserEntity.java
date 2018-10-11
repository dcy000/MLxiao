package com.gcml.common.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "UserOld")
public class UserEntity implements Parcelable {
    @NonNull
    @PrimaryKey
    @SerializedName("bid")
    public String id = "";
    @SerializedName("categoryid")
    public String categoryId;
    @SerializedName("doid")
    public String doctorId;
//    @SerializedName("eq")
//    public String eq;
    @SerializedName("bname")
    public String name;
    @SerializedName("sex")
    public String sex;
    @SerializedName("dz")
    public String address;
    @SerializedName("age")
    public String age;
    @SerializedName("sfz")
    public String idCard;
    @SerializedName("tel")
    public String phone;
    @SerializedName("mh")
    public String deseaseHistory;
    @SerializedName("eqid")
    public String deviceId;
    @SerializedName("state")
    public String state;
//    @SerializedName("qyzt")
//    public String qyzt;
    @SerializedName("height")
    public String height;
    @SerializedName("weight")
    public String weight;
    @SerializedName("bloodType")
    public String bloodType;
    @SerializedName("waist")
    public String waist;
    @SerializedName("eatingHabits")
    public String eatingHabits;
    @SerializedName("smoke")
    public String smokeHabits;
    @SerializedName("drink")
    public String drinkHabits;
    @SerializedName("exerciseHabits")
    public String sportsHabits;
    @SerializedName("userPhoto")
    public String avatar;
    @SerializedName("xfid")
    public String xfid;
    @SerializedName("xfuserid")
    public String xfUserId;
    @SerializedName("allergy")
    public String allergy;
    @SerializedName("fetation")
    public String fetation;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("hypertensionHand")
    public String hypertensionHand;
    @SerializedName("hypertensionPrimaryState")
    public String hypertensionPrimaryState;
    @SerializedName("hypertensionLevel")
    public String hypertensionLevel;
    @SerializedName("hypertensionTarget")
    public String hypertensionTarget;
    @SerializedName("wyyxId")
    public String wyyxId;
    @SerializedName("wyyxPwd")
    public String wyyxPwd; 
    @SerializedName("vipState")
    public String vipState;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.categoryId);
        dest.writeString(this.doctorId);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.address);
        dest.writeString(this.age);
        dest.writeString(this.idCard);
        dest.writeString(this.phone);
        dest.writeString(this.deseaseHistory);
        dest.writeString(this.deviceId);
        dest.writeString(this.state);
//        dest.writeString(this.qyzt);
        dest.writeString(this.height);
        dest.writeString(this.weight);
        dest.writeString(this.waist);
        dest.writeString(this.bloodType);
        dest.writeString(this.eatingHabits);
        dest.writeString(this.smokeHabits);
        dest.writeString(this.drinkHabits);
        dest.writeString(this.sportsHabits);
        dest.writeString(this.avatar);
        dest.writeString(this.xfid);
        dest.writeString(this.xfUserId);
        dest.writeString(this.allergy);
        dest.writeString(this.fetation);
        dest.writeString(this.birthday);
        dest.writeString(this.hypertensionHand);
        dest.writeString(this.hypertensionPrimaryState);
        dest.writeString(this.hypertensionLevel);
        dest.writeString(this.hypertensionTarget);
        dest.writeString(this.wyyxId);
        dest.writeString(this.wyyxPwd);
        dest.writeString(this.vipState);
    }

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        this.id = in.readString();
        this.categoryId = in.readString();
        this.doctorId = in.readString();
        this.name = in.readString();
        this.sex = in.readString();
        this.address = in.readString();
        this.age = in.readString();
        this.idCard = in.readString();
        this.phone = in.readString();
        this.deseaseHistory = in.readString();
        this.deviceId = in.readString();
        this.state = in.readString();
//        this.qyzt = in.readString();
        this.height = in.readString();
        this.weight = in.readString();
        this.waist = in.readString();
        this.bloodType = in.readString();
        this.eatingHabits = in.readString();
        this.smokeHabits = in.readString();
        this.drinkHabits = in.readString();
        this.sportsHabits = in.readString();
        this.avatar = in.readString();
        this.xfid = in.readString();
        this.xfUserId = in.readString();
        this.allergy = in.readString();
        this.fetation = in.readString();
        this.birthday = in.readString();
        this.hypertensionHand = in.readString();
        this.hypertensionPrimaryState = in.readString();
        this.hypertensionLevel = in.readString();
        this.hypertensionTarget = in.readString();
        this.wyyxId = in.readString();
        this.wyyxPwd = in.readString();
        this.vipState = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", age='" + age + '\'' +
                ", idCard='" + idCard + '\'' +
                ", phone='" + phone + '\'' +
                ", deseaseHistory='" + deseaseHistory + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", state='" + state + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", waist='" + waist + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", eatingHabits='" + eatingHabits + '\'' +
                ", smokeHabits='" + smokeHabits + '\'' +
                ", drinkHabits='" + drinkHabits + '\'' +
                ", sportsHabits='" + sportsHabits + '\'' +
                ", avatar='" + avatar + '\'' +
                ", xfid='" + xfid + '\'' +
                ", xfUserId='" + xfUserId + '\'' +
                ", allergy='" + allergy + '\'' +
                ", fetation='" + fetation + '\'' +
                ", birthday='" + birthday + '\'' +
                ", hypertensionHand='" + hypertensionHand + '\'' +
                ", hypertensionPrimaryState='" + hypertensionPrimaryState + '\'' +
                ", hypertensionLevel='" + hypertensionLevel + '\'' +
                ", hypertensionTarget='" + hypertensionTarget + '\'' +
                ", wyyxId='" + wyyxId + '\'' +
                ", wyyxPwd='" + wyyxPwd + '\'' +
                ", vipState='" + vipState + '\'' +
                '}';
    }
}
