package com.example.han.referralproject.require2.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 通过eqid和userId查当前用户在当前机器上的 讯飞信息
 * Created by lenovo on 2018/7/18.
 */

public class UserEqIDXFInfoBean implements Serializable {

    /**
     * code : 0
     * data : {"currentEquipmentGroupId":"string","currentGroup":[{"groupId":"string","num":0}],"list":[{"equipmentId":"string","groupId":"string","groupMap":{},"regitationState":"string","userId":0,"xunfeiId":"string"}],"vipState":"string","xunfeiId":"string"}
     * error :
     * message :
     * tag : false
     */

    public int code;
    public DataBean data;
    public Object error;
    public Object message;
    public boolean tag;

    public static class DataBean implements Serializable {
        /**
         * currentEquipmentGroupId : string
         * currentGroup : [{"groupId":"string","num":0}]
         * list : [{"equipmentId":"string","groupId":"string","groupMap":{},"regitationState":"string","userId":0,"xunfeiId":"string"}]
         * vipState : string
         * xunfeiId : string
         */

        public String currentEquipmentGroupId;
        public String vipState;
        public String xunfeiId;
        public List<CurrentGroupBean> currentGroup;
        public List<ListBean> list;

        public static class CurrentGroupBean implements Serializable {
            /**
             * groupId : string
             * num : 0
             */

            public String groupId;
            public int num;
        }

        public static class ListBean implements Serializable {
            /**
             * equipmentId : string
             * groupId : string
             * groupMap : {}
             * regitationState : string
             * userId : 0
             * xunfeiId : string
             */

            public String equipmentId;
            public String groupId;
            public GroupMapBean groupMap;
            public String regitationState;
            public int userId;
            public String xunfeiId;

            public static class GroupMapBean {
            }
        }
    }
}
