package com.gcml.health.assistant.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AbnormalEntity implements Parcelable {


    /**
     * seq : 0
     * createdBy : admin
     * createdOn : 1560911531000
     * modifiedBy : admin
     * modifiedOn : 1560911531000
     * deletionState : 0
     * id : 1
     * userId : 130434
     * doctorId : 10001
     * equipmentId : 652c1ec4cfbcc990
     * type : 0
     * value1 : 59
     * value2 : 141
     * detectionResult : 血压偏低
     * detectionTime : 2019-06-19
     */

    @SerializedName("deletionState")
    private String deletionState;
    @SerializedName("id")
    private int id;
    @SerializedName("userId")
    private int userId;
    @SerializedName("doctorId")
    private int doctorId;
    @SerializedName("equipmentId")
    private String equipmentId;
    @SerializedName("type")
    private String type;
    @SerializedName("value1")
    private String value1;
    @SerializedName("value2")
    private String value2;
    @SerializedName("detectionResult")
    private String detectionResult;
    @SerializedName("detectionTime")
    private String detectionTime;

    protected AbnormalEntity(Parcel in) {
        deletionState = in.readString();
        id = in.readInt();
        userId = in.readInt();
        doctorId = in.readInt();
        equipmentId = in.readString();
        type = in.readString();
        value1 = in.readString();
        value2 = in.readString();
        detectionResult = in.readString();
        detectionTime = in.readString();
    }

    public static final Creator<AbnormalEntity> CREATOR = new Creator<AbnormalEntity>() {
        @Override
        public AbnormalEntity createFromParcel(Parcel in) {
            return new AbnormalEntity(in);
        }

        @Override
        public AbnormalEntity[] newArray(int size) {
            return new AbnormalEntity[size];
        }
    };

    public String getDeletionState() {
        return deletionState;
    }

    public void setDeletionState(String deletionState) {
        this.deletionState = deletionState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getDetectionResult() {
        return detectionResult;
    }

    public void setDetectionResult(String detectionResult) {
        this.detectionResult = detectionResult;
    }

    public String getDetectionTime() {
        return detectionTime;
    }

    public void setDetectionTime(String detectionTime) {
        this.detectionTime = detectionTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deletionState);
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(doctorId);
        dest.writeString(equipmentId);
        dest.writeString(type);
        dest.writeString(value1);
        dest.writeString(value2);
        dest.writeString(detectionResult);
        dest.writeString(detectionTime);
    }

    public String getLabel() {
        type = type == null ? "" : type;
        //检测数据类型 -1低血压 0高血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸
        switch (type) {
            case "0":
                return "血压";
//                return "(mmHg)";
            case "1":
                return "血糖";
//                return "(mmol/L)";
            case "2":
                return "心电";
//                return "";
            case "3":
                return "体重";
//                return "(kg)";
            case "4":
                return "体温";
//                return "(℃)";
            case "6":
                return "血氧";
//                return "(%)";
            case "7":
                return "胆固醇";
//                return "(mmol/L)";
            case "8":
                return "血尿酸";
//                return "(μmol/L)";
        }
        return "";
    }

    public String getValue() {
        if ("2".equals(type)) {
            return "1".equals(value1) ? "正常" : "异常";
        }

        float v;
        if ("0".equals(type)) {
            try {
                v = value2 == null ? 0f : Float.parseFloat(value2);
            } catch (NumberFormatException e) {
                v = 0f;
                e.printStackTrace();
            }
            String value = String.format("%.0f", v) + "/";

            try {
                v = value1 == null ? 0f : Float.parseFloat(value1);
            } catch (NumberFormatException e) {
                v = 0f;
                e.printStackTrace();
            }

            value += String.format("%.0f", v);
            return value;
        }

        try {
            v = value1 == null ? 0f : Float.parseFloat(value1);
        } catch (NumberFormatException e) {
            v = 0f;
            e.printStackTrace();
        }

        String s = "<font color=\"#303133\">" + String.format("%.1f", v) + "</font>";
        return s;
    }

    public String getUnit() {
        type = type == null ? "" : type;
        //检测数据类型 -1低血压 0高血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸
        switch (type) {
            case "0":
//                return "血压";
                return "(mmHg)";
            case "1":
//                return "血糖";
                return "(mmol/L)";
            case "2":
//                return "心电";
                return "";
            case "3":
//                return "体重";
                return "(kg)";
            case "4":
//                return "体温";
                return "(℃)";
            case "6":
//                return "血氧";
                return "(%)";
            case "7":
//                return "胆固醇";
                return "(mmol/L)";
            case "8":
//                return "血尿酸";
                return "(μmol/L)";
        }
        return "";
    }


}
