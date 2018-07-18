package com.example.han.referralproject.require2.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/7/17.
 */

public class PutXFInfoBean implements Serializable {

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
    public String userId;
    public String xunfeiId;

    public static class GroupMapBean {
    }
}
