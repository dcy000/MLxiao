package com.gcml.health.measure.first_diagnosis.bean;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/30 15:58
 * created by:gzq
 * description:TODO
 */
public class PostDeviceBean {

    /**
     * equipmentId : string
     * name : string
     * userId : 0
     */

    private String equipmentId;
    private String name;
    private String userId;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
