package com.example.han.referralproject.yisuotang.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/6/8.
 */

public class YSTOrderResultBean implements Serializable {

    /**
     * tag : true
     * message : 订单创建成功，进入待支付阶段
     * data : {"orderid":1000000002}
     */

    public boolean tag;
    public String message;
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * orderid : 1000000002
         */

        public int orderid;
    }
}
