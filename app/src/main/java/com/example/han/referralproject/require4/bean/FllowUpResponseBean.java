package com.example.han.referralproject.require4.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/2/18.
 */

public class FllowUpResponseBean implements Serializable {

    public boolean tag;
    public int code;
    public DataBean data;
    public Object message;
    public Object error;

    public static class DataBean {
        public Integer examinationNum;
        public Integer hypertensionNum;
        public Integer diabetesNum;
        public String examinationDate;
        public String hypertensionDate;
        public String diabetesDate;
    }
}
