package com.gcml.common.data;

import android.text.TextUtils;

import com.gcml.lib_utils.data.SPUtil;

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
     * 获取SP中存储的userid
     *
     * @return
     */
    public static String getUserId() {
        return (String) SPUtil.get(KEY_USER_ID, "");
    }

    /**
     * 存储讯飞id到sp中
     *
     * @param faceId 人脸 Id
     */
    public static void setFaceId(String faceId) {
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

}
