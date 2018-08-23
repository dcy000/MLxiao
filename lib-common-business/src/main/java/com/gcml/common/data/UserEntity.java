package com.gcml.common.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "UserOld")
public class UserEntity implements Parcelable {
    @PrimaryKey
    @SerializedName("bid")
    private int id;
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("doct")
    private String doctorId;
    @SerializedName("eq")
    private String eq;
    @SerializedName("bname")
    private String name;
    @SerializedName("sex")
    private String sex;
    @SerializedName("dz")
    private String address;
    @SerializedName("age")
    private String age;
    @SerializedName("sfz")
    private String idCard;
    @SerializedName("tel")
    private String phone;
    @SerializedName("mh")
    private String deseaseHistory;
    @SerializedName("eqid")
    private String deviceId;
    @SerializedName("state")
    private int state;
    @SerializedName("qyzt")
    private String qyzt;
    @SerializedName("height")
    private int height;
    @SerializedName("weight")
    private int weight;
    @SerializedName("blood_type")
    private String bloodType;
    @SerializedName("eating_habits")
    private String eatingHabits;
    @SerializedName("smoke")
    private String smokeHabits;
    @SerializedName("drink")
    private String drinkHabits;
    @SerializedName("exercise_habits")
    private String sportsHabits;
    @SerializedName("user_photo")
    private String avatar;
    @SerializedName("xfid")
    private String xfid;
    @SerializedName("hypertensionHand")
    private String hypertensionHand;
    @SerializedName("wyyxId")
    private String wyyxId;
    @SerializedName("wyyxPwd")
    private String wyyxPwd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeseaseHistory() {
        return deseaseHistory;
    }

    public void setDeseaseHistory(String deseaseHistory) {
        this.deseaseHistory = deseaseHistory;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getQyzt() {
        return qyzt;
    }

    public void setQyzt(String qyzt) {
        this.qyzt = qyzt;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getEatingHabits() {
        return eatingHabits;
    }

    public void setEatingHabits(String eatingHabits) {
        this.eatingHabits = eatingHabits;
    }

    public String getSmokeHabits() {
        return smokeHabits;
    }

    public void setSmokeHabits(String smokeHabits) {
        this.smokeHabits = smokeHabits;
    }

    public String getDrinkHabits() {
        return drinkHabits;
    }

    public void setDrinkHabits(String drinkHabits) {
        this.drinkHabits = drinkHabits;
    }

    public String getSportsHabits() {
        return sportsHabits;
    }

    public void setSportsHabits(String sportsHabits) {
        this.sportsHabits = sportsHabits;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getXfid() {
        return xfid;
    }

    public void setXfid(String xfid) {
        this.xfid = xfid;
    }

    public String getHypertensionHand() {
        return hypertensionHand;
    }

    public void setHypertensionHand(String hypertensionHand) {
        this.hypertensionHand = hypertensionHand;
    }

    public String getWyyxId() {
        return wyyxId;
    }

    public void setWyyxId(String wyyxId) {
        this.wyyxId = wyyxId;
    }

    public String getWyyxPwd() {
        return wyyxPwd;
    }

    public void setWyyxPwd(String wyyxPwd) {
        this.wyyxPwd = wyyxPwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                categoryId == that.categoryId &&
                state == that.state &&
                height == that.height &&
                weight == that.weight &&
                Objects.equals(doctorId, that.doctorId) &&
                Objects.equals(eq, that.eq) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(address, that.address) &&
                Objects.equals(age, that.age) &&
                Objects.equals(idCard, that.idCard) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(deseaseHistory, that.deseaseHistory) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(qyzt, that.qyzt) &&
                Objects.equals(bloodType, that.bloodType) &&
                Objects.equals(eatingHabits, that.eatingHabits) &&
                Objects.equals(smokeHabits, that.smokeHabits) &&
                Objects.equals(drinkHabits, that.drinkHabits) &&
                Objects.equals(sportsHabits, that.sportsHabits) &&
                Objects.equals(avatar, that.avatar) &&
                Objects.equals(xfid, that.xfid) &&
                Objects.equals(hypertensionHand, that.hypertensionHand) &&
                Objects.equals(wyyxId, that.wyyxId) &&
                Objects.equals(wyyxPwd, that.wyyxPwd);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, categoryId, doctorId, eq, name, sex, address, age, idCard, phone, deseaseHistory, deviceId, state, qyzt, height, weight, bloodType, eatingHabits, smokeHabits, drinkHabits, sportsHabits, avatar, xfid, hypertensionHand, wyyxId, wyyxPwd);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.categoryId);
        dest.writeString(this.doctorId);
        dest.writeString(this.eq);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.address);
        dest.writeString(this.age);
        dest.writeString(this.idCard);
        dest.writeString(this.phone);
        dest.writeString(this.deseaseHistory);
        dest.writeString(this.deviceId);
        dest.writeInt(this.state);
        dest.writeString(this.qyzt);
        dest.writeInt(this.height);
        dest.writeInt(this.weight);
        dest.writeString(this.bloodType);
        dest.writeString(this.eatingHabits);
        dest.writeString(this.smokeHabits);
        dest.writeString(this.drinkHabits);
        dest.writeString(this.sportsHabits);
        dest.writeString(this.avatar);
        dest.writeString(this.xfid);
        dest.writeString(this.hypertensionHand);
        dest.writeString(this.wyyxId);
        dest.writeString(this.wyyxPwd);
    }

    public UserEntity() {
    }

    protected UserEntity(Parcel in) {
        this.id = in.readInt();
        this.categoryId = in.readInt();
        this.doctorId = in.readString();
        this.eq = in.readString();
        this.name = in.readString();
        this.sex = in.readString();
        this.address = in.readString();
        this.age = in.readString();
        this.idCard = in.readString();
        this.phone = in.readString();
        this.deseaseHistory = in.readString();
        this.deviceId = in.readString();
        this.state = in.readInt();
        this.qyzt = in.readString();
        this.height = in.readInt();
        this.weight = in.readInt();
        this.bloodType = in.readString();
        this.eatingHabits = in.readString();
        this.smokeHabits = in.readString();
        this.drinkHabits = in.readString();
        this.sportsHabits = in.readString();
        this.avatar = in.readString();
        this.xfid = in.readString();
        this.hypertensionHand = in.readString();
        this.wyyxId = in.readString();
        this.wyyxPwd = in.readString();
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
}
