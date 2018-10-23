package com.gcml.common.data;

import android.content.Context;
import android.text.TextUtils;

import com.gcml.common.utils.data.SPUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/13 11:27
 * created by:gzq
 * description:SharedPreferences清单表
 */
public class UserSpHelper {
    /**
     * 老版存储本机登录账户信息的key
     */
    private static final String KEY_USER_ACCOUNT = "user_accounts";
    /**
     * 新版存储本机登录账户信息的key
     */
    private static final String KEY_USER_ACCOUNT_NEW = "user_accounts_new";
    /**
     * 用户id的key
     */
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EQ_ID = "eq_id";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_REFRESH_TOKEN = "refreshToken";
    /**
     * 讯飞id的key
     */
    private static final String KEY_XUNFEI_ID = "Xunfei_Id";

    /**
     * 人脸识别组id
     */
    private static final String KEY_GROUP_ID = "group_id";
    /**
     * 创建人脸识别组的时候传入的第一个讯飞id
     */
    private static final String KEY_CREATE_GROUP_FIRST_XFID = "group_first_xfid";
    /**
     * 用户身高的key
     */
    private static final String KEY_USER_HEIGHT = "user_height";
    /**
     * 用户名字
     */
    private static final String KEY_USER_NAME = "user_name";
    /**
     * 用户血压测量惯用手 0：左手 1：右手
     */
    private static final String KEY_USER_HYPERTENSIONHAND = "user_hypertensionHand";
    /**
     * 判断有没有做过风险评估
     */
    private static final String KEY_IS_FIRST_RISK_ASSESSMENT = "isFirstRisk";

    /**
     * 获取SP中存储的userid
     *
     * @return
     */
    public static String getUserId() {
        return (String) SPUtil.get(KEY_USER_ID, "");
    }

    public static void setEqId(String eqId) {
        if (TextUtils.isEmpty(eqId)) {
            eqId = "";
        }
        SPUtil.put(KEY_EQ_ID, eqId);
    }

    public static String getEqId() {
        return (String) SPUtil.get(KEY_EQ_ID, "");
    }

    public static void setUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            userId = "";
        }
        SPUtil.put(KEY_USER_ID, userId);
    }

    public static String getToken() {
        return (String) SPUtil.get(KEY_TOKEN, "");
    }

    public static void setToken(String token) {
        if (TextUtils.isEmpty(token)) {
            token = "";
        }
        SPUtil.put(KEY_TOKEN, token);
    }

    public static String getRefreshToken() {
        return (String) SPUtil.get(KEY_REFRESH_TOKEN, "");
    }

    public static void setRefreshToken(String refreshToken) {
        if (TextUtils.isEmpty(refreshToken)) {
            refreshToken = "";
        }
        SPUtil.put(KEY_REFRESH_TOKEN, refreshToken);
    }

    /**
     * 存储讯飞id到sp中
     *
     * @param faceId 人脸 Id
     */
    public static void setFaceId(String faceId) {
        if (TextUtils.isEmpty(faceId)) {
            faceId = "";
        }
        SPUtil.put(KEY_XUNFEI_ID, faceId);
    }

    public static String produceFaceId() {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return simple.format(date) + str;
    }

    /**
     * 获取SP中存储的讯飞id
     *
     * @return
     */
    public static String getFaceId() {
        return (String) SPUtil.get(KEY_XUNFEI_ID, "");
    }

    /**
     * 获取所有本机登录的账户信息
     *
     * @return 格式：userid1,xfid1;userid2,xfid2;...
     */
    public static String[] getAccounts() {
        String accountsString = (String) SPUtil.get(KEY_USER_ACCOUNT_NEW, "");
        if (TextUtils.isEmpty(accountsString)) {
            String old_accountsString = (String) SPUtil.get(KEY_USER_ACCOUNT, "");
            if (TextUtils.isEmpty(old_accountsString)) {
                return null;
            } else {
                addAccount(getUserId(), getFaceId());
                SPUtil.remove(KEY_USER_ACCOUNT);
                return new String[]{getUserId() + "," + getFaceId()};
            }

        }
        return accountsString.substring(0, accountsString.length() - 1).split(";");
    }

    /**
     * 缓存在本机登录的用户信息
     *
     * @param bid
     * @param faceId
     */
    public static void addAccount(String bid, String faceId) {
        if (TextUtils.isEmpty(bid) || TextUtils.isEmpty(faceId)) {
            return;
        }
        String accountsString = (String) SPUtil.get(KEY_USER_ACCOUNT_NEW, "");

        if (TextUtils.isEmpty(accountsString)) {
            SPUtil.put(KEY_USER_ACCOUNT_NEW, bid + "," + faceId + ";");
        } else {
            String[] accountsArray = accountsString.substring(0, accountsString.length() - 1).split(";");
            if (!isContainAccount(accountsArray, bid, faceId)) {
                SPUtil.put(KEY_USER_ACCOUNT_NEW, accountsString + bid + "," + faceId + ";");
            }
        }
    }

    /**
     * 缓存人脸识别组id
     *
     * @param groupid
     */
    public static void setGroupId(String groupid) {
        SPUtil.put(KEY_GROUP_ID, groupid);
    }

    /**
     * 获取人脸识别组id
     *
     * @return
     */
    public static String getGroupId() {
        return (String) SPUtil.get(KEY_GROUP_ID, "");
    }

    /**
     * 讯飞组创建时候传入的xfid
     *
     * @param faceId
     */
    public static void setGroupFirstFaceId(String faceId) {
        SPUtil.put(KEY_CREATE_GROUP_FIRST_XFID, faceId);
    }

    /**
     * 获去创建组第一次传入的讯飞id
     *
     * @return
     */
    public static String getGroupFirstFaceId() {
        return (String) SPUtil.get(KEY_CREATE_GROUP_FIRST_XFID, "");
    }

    private static boolean isContainAccount(String[] accountsArray, String bid, String faceId) {
        if (TextUtils.isEmpty(bid) || accountsArray == null || TextUtils.isEmpty(faceId)) {
            return false;
        }
        for (String item : accountsArray) {
            if ((bid + "," + faceId).equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取存储在SP中的用户身高 单位cm
     *
     * @return
     */
    public static String getUserHeight() {
        return (String) SPUtil.get(KEY_USER_HEIGHT, "");
    }

    /**
     * 获取SP中存储的username
     *
     * @return
     */
    public static String getUserName() {
        return (String) SPUtil.get(KEY_USER_NAME, "");
    }

    /**
     * 获取存在SP中的惯用手
     *
     * @return
     */
    public static String getUserHypertensionHand() {
        return (String) SPUtil.get(KEY_USER_HYPERTENSIONHAND, "");
    }

    /**
     * 保存惯用手到SP中
     *
     * @param hypertensionHand
     */
    public static void setUserHypertensionHand(String hypertensionHand) {
        SPUtil.put(KEY_USER_HYPERTENSIONHAND, hypertensionHand);
    }

    /**
     * 记录有没有做过风险评估的报告
     *
     * @param isDo
     */
    public static void setRiskAssessmentState(boolean isDo) {
        String userId = getUserId();
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        SPUtil.put(userId + KEY_IS_FIRST_RISK_ASSESSMENT, isDo);
    }

    /**
     * 获取存在SP中的是否做过风险评估的标志
     * @return
     */
    public static boolean getRiskAssessmentState() {
        String userId = getUserId();
        if (TextUtils.isEmpty(userId)) {
            return false;
        }
        return (boolean) SPUtil.get(userId + KEY_IS_FIRST_RISK_ASSESSMENT, false);
    }

    public static void clear(Context context) {
        SPUtil.clear();
        if (context != null) {
            context.getSharedPreferences("com.iflytek.setting", Context.MODE_PRIVATE)
                    .edit().clear().apply();
            context.getSharedPreferences("doctor_message", Context.MODE_PRIVATE)
                    .edit().clear().apply();
        }
    }
}
