package com.gcml.module_auth_hospital.postinputbean;

import com.gcml.common.utils.UM;
import com.gcml.common.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 注册时post的bean
 */
public class SignUpBean implements Serializable {

    /**
     * address : 杭州
     * age : 0
     * allergy :
     * birthday :
     * bloodType :
     * categoryId : 0
     * doctorId : 0
     * drink :
     * eatingHabits :
     * equipmentId : 123
     * exerciseHabits :
     * faceId :
     * faceUserId :
     * fetation :
     * height : 0
     * hypertensionHand :
     * hypertensionLevel :
     * hypertensionPrimaryState :
     * hypertensionTarget :
     * idNo :
     * medicalHistory :
     * patientId : 0
     * patientName :
     * sex :
     * smoke :
     * source :
     * state : 0
     * tel : 18655212892
     * userPhoto :
     * uuid :
     * waist : 0
     * weight : 0
     * wyyxId :
     * wyyxPwd : 123456
     */

    private Integer serverId=1;
    private String address;
    private Integer age;
    private String allergy;
    private String birthday;
    private String bloodType;
    private Integer categoryId;
    private Integer doctorId;
    private String drink;
    private String eatingHabits;
    @SerializedName("eqid")
    private String equipmentId= Utils.getDeviceId(UM.getApp().getContentResolver());
    private String exerciseHabits;
    private String faceId;
    private String faceUserId;
    private String fetation;
    private Integer height;
    private String hypertensionHand;
    private String hypertensionLevel;
    private String hypertensionPrimaryState;
    private String hypertensionTarget;
    @SerializedName("sfz")
    private String idNo;
    private String medicalHistory;
    private Integer patientId;
    @SerializedName("bname")
    private String patientName;
    private String sex;
    private String smoke;
    private String source;
    private Integer state;
    private String tel;
    private String userPhoto;
    private String uuid;
    private Integer waist;
    private Integer weight;
    private String wyyxId;
    private String wyyxPwd;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getEatingHabits() {
        return eatingHabits;
    }

    public void setEatingHabits(String eatingHabits) {
        this.eatingHabits = eatingHabits;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getExerciseHabits() {
        return exerciseHabits;
    }

    public void setExerciseHabits(String exerciseHabits) {
        this.exerciseHabits = exerciseHabits;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getFaceUserId() {
        return faceUserId;
    }

    public void setFaceUserId(String faceUserId) {
        this.faceUserId = faceUserId;
    }

    public String getFetation() {
        return fetation;
    }

    public void setFetation(String fetation) {
        this.fetation = fetation;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getHypertensionHand() {
        return hypertensionHand;
    }

    public void setHypertensionHand(String hypertensionHand) {
        this.hypertensionHand = hypertensionHand;
    }

    public String getHypertensionLevel() {
        return hypertensionLevel;
    }

    public void setHypertensionLevel(String hypertensionLevel) {
        this.hypertensionLevel = hypertensionLevel;
    }

    public String getHypertensionPrimaryState() {
        return hypertensionPrimaryState;
    }

    public void setHypertensionPrimaryState(String hypertensionPrimaryState) {
        this.hypertensionPrimaryState = hypertensionPrimaryState;
    }

    public String getHypertensionTarget() {
        return hypertensionTarget;
    }

    public void setHypertensionTarget(String hypertensionTarget) {
        this.hypertensionTarget = hypertensionTarget;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
}
