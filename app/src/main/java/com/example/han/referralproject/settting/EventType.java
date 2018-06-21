package com.example.han.referralproject.settting;

/**
 * Created by lenovo on 2018/3/9.
 */

public enum EventType {
    clearCache("确认清除本地缓存吗？"), reset("确认恢复出厂设置吗？"), callCustomer("要拨打客服吗?");

    EventType(String description) {
        this.description = description;
    }

    String description;

    public String getValue() {
        return description;
    }
}
