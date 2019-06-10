package com.gcml.module_inquiry.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/5/27.
 */

public class WenZhenReultBean implements Serializable {


    /**
     * code : 0
     * data : {"address":"string","bloodType":"string","createdBy":"string","createdOn":"2019-02-15T03:45:36.317Z","deletionState":"string","description":"string","disabilitySituation":"string","diseasesHistory":"string","diseasesKinsfolk":"string","doctorId":0,"educationalLevel":"string","equipmentId":"string","exposureHistory":"string","geneticHistory":"string","height":"string","hiHealthRecordId":"string","kinsfolkDiseasesType":"string","kitchenExhaust":"string","kitchenFuel":"string","livestockBar":"string","maritalStatus":"string","medicalPayments":"string","medicationAllergy":"string","modifiedBy":"string","modifiedOn":"2019-02-15T03:45:36.318Z","orgName":"string","professionType":"string","rhBlood":"string","seq":0,"toiletPosition":"string","userId":0,"waterEnvironment":"string","weight":"string"}
     * error : {}
     * message : {}
     * tag : false
     */

    public int code;
    public DataBean data;
    public Object error;
    public Object message;
    public boolean tag;

    public static class DataBean {
        public String address;
        public String bloodType;
        public String createdBy;
        public String createdOn;
        public String deletionState;
        public String description;
        public String disabilitySituation;
        public String diseasesHistory;
        public String diseasesKinsfolk;
        public int doctorId;
        public String educationalLevel;
        public String equipmentId;
        public String exposureHistory;
        public String geneticHistory;
        public String height;
        public String hiHealthRecordId;
        public String kinsfolkDiseasesType;
        public String kitchenExhaust;
        public String kitchenFuel;
        public String livestockBar;
        public String maritalStatus;
        public String medicalPayments;
        public String medicationAllergy;
        public String modifiedBy;
        public String modifiedOn;
        public String orgName;
        public String professionType;
        public String rhBlood;
        public int seq;
        public String toiletPosition;
        public int userId;
        public String waterEnvironment;
        public String weight;

        public String receptionDate;
        public String receptionStatus;
    }
}
