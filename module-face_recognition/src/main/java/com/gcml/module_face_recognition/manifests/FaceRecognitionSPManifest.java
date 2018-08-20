package com.gcml.module_face_recognition.manifests;

import android.text.TextUtils;

import com.gcml.lib_utils.data.SPUtil;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/13 11:27
 * created by:gzq
 * description:SharedPreferences清单表
 */
public class FaceRecognitionSPManifest {
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
    private static final String KEY_GROUP_ID="group_id";
    /**
     * 创建人脸识别组的时候传入的第一个讯飞id
     */
    private static final String KEY_CREATE_GROUP_FIRST_XFID="group_first_xfid";
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
     * @param xfid
     */
    public static void setXunfeiId(String xfid) {
        SPUtil.put(KEY_XUNFEI_ID, xfid);
    }

    /**
     * 获取SP中存储的讯飞id
     *
     * @return
     */
    public static String getXunfeiId() {
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
                addAccount(getUserId(), getXunfeiId());
                SPUtil.remove(KEY_USER_ACCOUNT);
                return new String[]{getUserId() + "," + getXunfeiId()};
            }

        }
        return accountsString.substring(0, accountsString.length() - 1).split(";");
    }

    /**
     * 缓存在本机登录的用户信息
     *
     * @param bid
     * @param xfid
     */
    public static void addAccount(String bid, String xfid) {
        if (TextUtils.isEmpty(bid) || TextUtils.isEmpty(xfid)) {
            return;
        }
        String accountsString = (String) SPUtil.get(KEY_USER_ACCOUNT_NEW, "");

        if (TextUtils.isEmpty(accountsString)) {
            SPUtil.put(KEY_USER_ACCOUNT_NEW, bid + "," + xfid + ";");
        } else {
            String[] accountsArray = accountsString.substring(0, accountsString.length() - 1).split(";");
            if (!isContainAccount(accountsArray, bid, xfid)) {
                SPUtil.put(KEY_USER_ACCOUNT_NEW, accountsString + bid + "," + xfid + ";");
            }
        }
    }

    /**
     * 缓存人脸识别组id
     * @param groupid
     */
    public static void setGroupId(String groupid) {
        SPUtil.put(KEY_GROUP_ID,groupid);
    }

    /**
     * 获取人脸识别组id
     * @return
     */
    public static String getGroupId() {
        return (String) SPUtil.get(KEY_GROUP_ID,"");
    }


    /**
     * 讯飞组创建时候传入的xfid
     *
     * @param xfid
     */
    public static void setGroupFirstXfid(String xfid) {
        SPUtil.put(KEY_CREATE_GROUP_FIRST_XFID,xfid);
    }

    /**
     * 获去创建组第一次传入的讯飞id
     * @return
     */
    public static String getGroupFirstXfid() {
        return (String) SPUtil.get(KEY_CREATE_GROUP_FIRST_XFID,"");
    }
    private static boolean isContainAccount(String[] accountsArray, String bid, String xfid) {
        if (TextUtils.isEmpty(bid) || accountsArray == null || TextUtils.isEmpty(xfid)) {
            return false;
        }
        for (String item : accountsArray) {
            if ((bid + "," + xfid).equals(item)) {
                return true;
            }
        }
        return false;
    }

}
