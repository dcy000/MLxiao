package com.example.han.referralproject.yiyuan.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/7/17.
 */

public class HealthDetectQualificationBean implements Serializable {

    /**
     * tag : true
     * code : 200
     * data : {"qualification":false,"lastRecordDate":"2018-07-17","lastRecordId":"839eb7dc-4ea5-4fc7-b863-6b236aad3c64","nextRecordDate":"2018年01月-12月"}
     * message : 成功
     * error : null
     */

    public boolean tag;
    public int code;
    public DataBean data;
    public String message;
    public Object error;

    public static class DataBean {
        /**
         * qualification : false
         * lastRecordDate : 2018-07-17
         * lastRecordId : 839eb7dc-4ea5-4fc7-b863-6b236aad3c64
         * nextRecordDate : 2018年01月-12月
         */

        public boolean yearFreeState;
        public boolean qualification;
        public String lastRecordDate;
        public String lastRecordId;
        public String nextRecordDate;
        public String currentDate;
    }

}
