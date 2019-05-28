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
    @SerializedName(value = "bid", alternate = {"patientId"})
    public String id = "";
    @SerializedName("categoryid")
    public String categoryId;
    @SerializedName(value = "doid", alternate = {"doctorId"})
    public String doctorId;
    //    @SerializedName("eq")
//    public String eq;
    @SerializedName(value = "bname", alternate = {"patientName"})
    public String name;
    @SerializedName("sex")
    public String sex;
    @SerializedName("dz")
    public String address;
    @SerializedName("age")
    public String age;
    @SerializedName(value = "sfz", alternate = {"idNo"})
    public String idCard;
    @SerializedName("tel")
    public String phone;
    @SerializedName("mh")
    public String deseaseHistory;
    @SerializedName(value = "eqid", alternate = {"equipmentId"})
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
    @SerializedName(value = "xfid", alternate = {"faceId"})
    public String xfid;
    @SerializedName(value = "xfuserid", alternate = "faceUserId")
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
    private String medicalHistory;
    private String source;
    private String uuid;

    //手环信息
    public String watchCode;
    public UserEntity() {

    }

    protected UserEntity(Parcel in) {
        id = in.readString();
        categoryId = in.readString();
        doctorId = in.readString();
        name = in.readString();
        sex = in.readString();
        address = in.readString();
        age = in.readString();
        idCard = in.readString();
        phone = in.readString();
        deseaseHistory = in.readString();
        deviceId = in.readString();
        state = in.readString();
        height = in.readString();
        weight = in.readString();
        bloodType = in.readString();
        waist = in.readString();
        eatingHabits = in.readString();
        smokeHabits = in.readString();
        drinkHabits = in.readString();
        sportsHabits = in.readString();
        avatar = in.readString();
        xfid = in.readString();
        xfUserId = in.readString();
        allergy = in.readString();
        fetation = in.readString();
        birthday = in.readString();
        hypertensionHand = in.readString();
        hypertensionPrimaryState = in.readString();
        hypertensionLevel = in.readString();
        hypertensionTarget = in.readString();
        wyyxId = in.readString();
        wyyxPwd = in.readString();
        vipState = in.readString();
        medicalHistory = in.readString();
        source = in.readString();
        uuid = in.readString();
        watchCode = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(categoryId);
        dest.writeString(doctorId);
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(address);
        dest.writeString(age);
        dest.writeString(idCard);
        dest.writeString(phone);
        dest.writeString(deseaseHistory);
        dest.writeString(deviceId);
        dest.writeString(state);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(bloodType);
        dest.writeString(waist);
        dest.writeString(eatingHabits);
        dest.writeString(smokeHabits);
        dest.writeString(drinkHabits);
        dest.writeString(sportsHabits);
        dest.writeString(avatar);
        dest.writeString(xfid);
        dest.writeString(xfUserId);
        dest.writeString(allergy);
        dest.writeString(fetation);
        dest.writeString(birthday);
        dest.writeString(hypertensionHand);
        dest.writeString(hypertensionPrimaryState);
        dest.writeString(hypertensionLevel);
        dest.writeString(hypertensionTarget);
        dest.writeString(wyyxId);
        dest.writeString(wyyxPwd);
        dest.writeString(vipState);
        dest.writeString(medicalHistory);
        dest.writeString(source);
        dest.writeString(uuid);
        dest.writeString(watchCode);
    }
}
