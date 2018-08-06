package com.example.han.referralproject.require2.healthservice;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/8/6.
 */

public class AccoutInfoBean implements Serializable {

    /**
     * code : 0
     * data : {"fregioncode":"string","gxfwCodes":"string","level":"string","orgId":"string","orgName":"string","taskCount":"string","userId":"string","userName":"string"}
     * error : {}
     * message : {}
     * tag : false
     */

    public int code;
    public DataBean data;
    public ErrorBean error;
    public MessageBean message;
    public boolean tag;

    public static class DataBean implements Serializable {
        /**
         * fregioncode : string
         * gxfwCodes : string
         * level : string
         * orgId : string
         * orgName : string
         * taskCount : string
         * userId : string
         * userName : string
         */

        public String fregioncode;
        public String gxfwCodes;
        public String level;
        public String orgId;
        public String orgName;
        public String taskCount;
        public String userId;
        public String userName;
    }

    public static class ErrorBean {
    }

    public static class MessageBean {
    }
}
