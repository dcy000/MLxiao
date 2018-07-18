package com.example.han.referralproject.require2.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/7/17.
 */

public class GetUserXFInfoBean implements Serializable {

    /**
     * tag : true
     * code : 200
     * data : {"vipState":"1","list":null,"currentGroup":null,"xunfeiId":"1a5e2c8b65be998a_100167_test_ltc"}
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
         * vipState : 1
         * list : null
         * currentGroup : null
         * xunfeiId : 1a5e2c8b65be998a_100167_test_ltc
         */

        public String vipState;
        public List<TUserXunfei> list;
        public String currentGroup;
        public String xunfeiId;
    }

    public static class TUserXunfei implements Serializable {
        /**
         * vipState : 1
         * list : null
         * currentGroup : null
         * xunfeiId : 1a5e2c8b65be998a_100167_test_ltc
         */

        public String equipmentId;
        public String groupId;
        public Object groupMap;
        public String regitationState;
        public String userId;
        public String xunfeiId;
    }

}
