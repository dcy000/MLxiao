package com.example.han.referralproject.personal.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/6/5.
 */

public class BanlanceAndIntegralResultBean implements Serializable {

    /**
     * code : 1
     * message : 成功
     * data : {"mywallet":"1.00","whitepoint":"666.00"}
     */

    public String code;
    public String message;
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * mywallet : 1.00
         * whitepoint : 666.00
         */

        public String mywallet;
        public String whitepoint;
    }
}
