package com.gcml.health.measure.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.gcml.common.utils.UtilsManager;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/30 11:05
 * created by: gzq
 * description: TODO
 */
public class ChannelUtils {
    public static final String CHANNEL_BASE = "gcml_version_normal";

    /**
     * 是否是基础版本
     * @return
     */
    public static boolean isBase() {
        if (getChannelMeta().equals(CHANNEL_BASE)) {
            return true;
        }
        return false;
    }

    private static String getChannelMeta() {
        String channel = null;
        try {
            ApplicationInfo appInfo = UtilsManager.getApplication().getPackageManager()
                    .getApplicationInfo(UtilsManager.getApplication().getPackageName(), PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("com.gcml.version");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
