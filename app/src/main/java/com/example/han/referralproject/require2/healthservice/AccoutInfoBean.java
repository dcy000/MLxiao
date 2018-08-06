package com.example.han.referralproject.require2.healthservice;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/8/6.
 */

public class AccoutInfoBean implements Serializable {


    /**
     * tag : true
     * code : 200
     * data : {"orgId":"38740","userName":"APP测试","userId":"9041010649","taskCount":"0","orgName":"瓦寺前中心卫生室","level":"5","gxfwCodes":" ","fregioncode":"421124101201"}
     * message : 成功
     * error : null
     */

    public boolean tag;
    public int code;
    public DataBean data;
    public String message;
    public Object error;

    public static class DataBean implements Serializable{
        /**
         * orgId : 38740
         * userName : APP测试
         * userId : 9041010649
         * taskCount : 0
         * orgName : 瓦寺前中心卫生室
         * level : 5
         * gxfwCodes :
         * fregioncode : 421124101201
         */

        public String orgId;
        public String userName;
        public String userId;
        public String taskCount;
        public String orgName;
        public String level;
        public String gxfwCodes;
        public String fregioncode;
    }
}
