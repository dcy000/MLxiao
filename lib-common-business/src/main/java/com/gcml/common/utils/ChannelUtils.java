package com.gcml.common.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/30 11:05
 * created by: gzq
 * description: TODO
 */
public class ChannelUtils {
    public static final String CHANNEL_BASE = "gcml_version_normal";
    public static final String CHANNEL_APP_COMBINE = "gcml_version_combine";
    public static final String CHANNEL_JGYS = "gcml_version_jgys";
    public static final String CHANNEL_XA = "gcml_version_xa";

    /**
     * 基础版本
     *
     * @return
     */
    public static boolean isBase() {
        if (TextUtils.equals(getChannelMeta(), CHANNEL_BASE)) return true;
        return false;
    }

    /**
     * 健管演示
     *
     * @return
     */
    public static boolean isJGYS() {
        if (TextUtils.equals(getChannelMeta(), CHANNEL_JGYS)) return true;
        return false;
    }

    /**
     * 合版
     *
     * @return
     */
    public static boolean isAppCombine() {
        if (TextUtils.equals(getChannelMeta(), CHANNEL_APP_COMBINE)) return true;
        return false;
    }

    public static boolean isXiongAn() {
        if (TextUtils.equals(getChannelMeta(), CHANNEL_XA)) return true;
        return false;
    }

    public static String getChannelMeta() {
        String channel = null;
        try {
            ApplicationInfo appInfo = UM.getApp().getPackageManager()
                    .getApplicationInfo(UM.getApp().getPackageName(), PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("com.gcml.version");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
