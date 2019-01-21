package com.gcml.common;

/**
 * CC组件通信KEY
 * Created by lenovo on 2019/1/17.
 */

public interface IConstant {
    /**
     * 医生登录 组件
     */
    String KEY_HOSPITAL_DOCTOR_SIGN = "com.gcml.doctor.sing.in";
    String KEY_HOSPITAL_USER_SIGN = "com.gcml.auth";
    String KEY_HOSPITAL_USER_LOGIN = "com.gcml.auth.signin";
    String KEY_HOSPITAL_USER_REGISTER = "com.gcml.auth.signup";

    /**
     * 问诊建档首页
     */
    String KEY_INUIRY_ENTRY = "com.gcml.inquiry.entry";
    String KEY_INUIRY_DETECTION = "com.gcml.inquiry.detection";

    /**
     * 建档模块 首页 入口
     */
    String KEY_HEALTH_FILE_ENTRY = "com.gcml.health.file";
}
