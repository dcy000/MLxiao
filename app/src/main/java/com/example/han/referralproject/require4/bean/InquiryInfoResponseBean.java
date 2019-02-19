package com.example.han.referralproject.require4.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/2/15.
 */

public class InquiryInfoResponseBean implements Serializable {

    /**
     * tag : true
     * code : 200
     * data : {"height":172,"weight":49,"address":null,"heightModify":null,"weightModify":null,"addressModify":null,"heightModifyDays":null,"weightModifyDays":null,"addressModifyDays":null}
     * message : 成功
     * error : null
     */

    public boolean tag;
    public int code;
    public DataBean data;
    public Object message;
    public Object error;

    public static class DataBean {
        public Integer height;
        public Integer weight;
        public String address;
        public String heightModify;
        public String weightModify;
        public String addressModify;
        public Integer heightModifyDays;
        public Integer weightModifyDays;
        public Integer addressModifyDays;
        public Integer age;
    }
}
