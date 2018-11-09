package com.example.han.referralproject.single_measure.bean;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/30 15:25
 * created by:gzq
 * description:TODO
 */
public class DeviceBean {

    /**
     * hdUserDeviceId : ba93c8dc-0156-433e-b34d-e8da7198bcf9
     * name : 血压仪
     * equipmentId : test
     * userId : 100206
     */

    private String hdUserDeviceId;
    private String name;
    private String equipmentId;
    private int userId;

    public String getHdUserDeviceId() {
        return hdUserDeviceId;
    }

    public void setHdUserDeviceId(String hdUserDeviceId) {
        this.hdUserDeviceId = hdUserDeviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
