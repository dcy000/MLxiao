package com.gcml.module_health_record.others;

import android.text.TextUtils;

import com.gcml.lib_utils.data.SPUtil;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/13 11:27
 * created by:gzq
 * description:SharedPreferences清单表
 */
public class HealthRecordSPManifest {
    /**
     * 用户id的key
     */
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME="user_name";

    /**
     * 获取SP中存储的userid
     *
     * @return
     */
    public static String getUserId() {
        return (String) SPUtil.get(KEY_USER_ID, "");
    }

    /**
     * 获取SP中存储的username
     * @return
     */
    public static String getUserName(){
        return (String) SPUtil.get(KEY_USER_NAME,"");
    }
}
