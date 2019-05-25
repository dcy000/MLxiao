package com.gcml.module_health_profile.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/2/28.
 */

public class WarnBean implements Serializable {

    /**
     * warningId : 4b75a879-c5a7-4859-b0a4-ce59cbc55519
     * userId : 130288
     * userName : 孙高烽
     * userPhoto : http://oyptcv2pb.bkt.clouddn.com/20180919180711_130288.jpg
     * equipmentId : 350183475190643
     * equipmentType : 0
     * warningType : 1
     * warningAddress : 浙江省 杭州市 萧山区 建设一路 靠近建设一路(地铁站)
     * warningContent :
     * warningTime : 1551060096000
     * dealStatus : 1
     * dealTime : 1551173482000
     * dealContent :
     * feedback :
     */

    public String warningId;
    public int userId;
    public String userName;
    public String userPhoto;
    public String equipmentId;
    public String equipmentType;
    public String warningType;
    public String warningAddress;
    public String warningContent;
    public long warningTime;
    public String dealStatus;
    public long dealTime;
    public String dealContent;
    public String feedback;
    public String handlerName;
}
