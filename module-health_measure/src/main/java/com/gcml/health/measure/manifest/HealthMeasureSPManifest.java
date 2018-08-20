package com.gcml.health.measure.manifest;

import com.gcml.lib_utils.data.SPUtil;

/**
 * Created by gzq on 2018/8/19.
 */

public class HealthMeasureSPManifest {
    /**
     * 用户id的key
     */
    private static final String KEY_USER_ID = "user_id";
    /**
     * 获取SP中存储的userid
     *
     * @return
     */
    public static String getUserId() {
        return (String) SPUtil.get(KEY_USER_ID, "");
    }
}
