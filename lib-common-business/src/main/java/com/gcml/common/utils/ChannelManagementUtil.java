package com.gcml.common.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;



/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/22 17:02
 * created by: gzq
 * description: 小E机器人渠道管理
 */
public class ChannelManagementUtil {
    /**
     * 我们自己的版本
     */
    public static final String CHANNEL_BASE = "com.gcml.version";
    /**
     * 西恩的版本
     */
    public static final String CHANNEL_XIEN = "gcml_version_xien";

    /**
     * 自己的基础版
     */
    public static boolean isBase() {
        ApplicationInfo appInfo;
        String channel = null;
        try {
            appInfo = UtilsManager.getApplication().getPackageManager()
                    .getApplicationInfo(UtilsManager.getApplication().getPackageName(), PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("com.gcml.version");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(channel) && channel.equals(CHANNEL_BASE)) {
            return true;
        }
        return false;
    }

    /**
     * 西恩版本
     * @return
     */
    public static boolean isXien(){
        ApplicationInfo appInfo;
        String channel = null;
        try {
            appInfo = UtilsManager.getApplication().getPackageManager()
                    .getApplicationInfo(UtilsManager.getApplication().getPackageName(), PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("com.gcml.version");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(channel) && channel.equals(CHANNEL_XIEN)) {
            return true;
        }
        return false;
    }
}
