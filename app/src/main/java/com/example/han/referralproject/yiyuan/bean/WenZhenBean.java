package com.example.han.referralproject.yiyuan.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/5/27.
 */

public class WenZhenBean implements Serializable {

    /**
     * address : string
     * allergicHistory : string
     * createdBy : string
     * createdOn : 2019-02-18T07:29:02.345Z
     * deletionState : string
     * description : string
     * diseasesHistory : string
     * equipmentId : string
     * height : string
     * hiUserInquiryId : string
     * highPressure : 0
     * hypertensionState : string
     * income : string
     * lastMensesTime : 2019-02-18T07:29:02.345Z
     * lowPressure : 0
     * modifiedBy : string
     * modifiedOn : 2019-02-18T07:29:02.345Z
     * pregnantState : string
     * receptionDate : 2019-02-18T07:29:02.345Z
     * receptionStatus : string
     * seq : 0
     * temperAture : 0
     * userId : 0
     * weekDrinkState : string
     * weight : string
     * wineType : string
     */

    public String address;
    public String allergicHistory;
    public String createdBy;
    public String createdOn;
    public String deletionState;
    public String description;
    public String diseasesHistory;
    public String equipmentId;
    public String height;
    public String hiUserInquiryId;
    public String highPressure;
    public String hypertensionState;
    public String income;//0,1,2
    public String lastMensesTime;
    public String lowPressure;
    public String modifiedBy;
    public String modifiedOn;
    public String pregnantState;
    public String receptionDate;
    public String receptionStatus;//0否,1是
    public int seq;
    public Float temperAture;//温度
    public String userId;
    public String weekDrinkState;
    public String weight;
    public String wineType;
}
