package com.example.han.referralproject.yisuotang.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/6/8.
 */

public class WalletResultBean implements Serializable {
    /**
     * tag : true
     * code : 200
     * data : {"uid":23685,"whitepoint":"888.00","count":0,"mywallet":"0.02"}
     * message : 成功
     */

    public boolean tag;
    public int code;
    public DataBean data;
    public String message;

    public static class DataBean implements Serializable {
        /**
         * uid : 23685
         * whitepoint : 888.00
         * count : 0
         * mywallet : 0.02
         */

        public int uid;
        public String whitepoint;
        public int count;
        public String mywallet;
    }

}
