package com.gcml.module_blutooth_devices.utils;

import com.gcml.lib_utils.data.SPUtil;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/14 15:38
 * created by:gzq
 * description:TODO
 */
public class SPManifest {
    /**
     * 获取用户的姓名
     * @return
     */
    public static String getUserName(){
        return (String) SPUtil.get(Bluetooth_Constants.SP.KEY_USER_NAME,"");
    }

    /**
     * 获取用户的性别
     * @return
     */
    public static String getUserSex(){
        return (String) SPUtil.get(Bluetooth_Constants.SP.KEY_USER_SEX,"");
    }

    /**
     * 获取用户的年龄
     * @return
     */
    public static String getUserAge(){
        return (String) SPUtil.get(Bluetooth_Constants.SP.KEY_USER_AGE,"");
    }
    /**
     * 获取用户的电话号码
     */
    public static String getUserPhone(){
        return (String) SPUtil.get(Bluetooth_Constants.SP.KEY_USER_PHONE,"");
    }
}
