package com.gzq.lib_core.session;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.gzq.lib_core.constant.Constants;
import com.gzq.lib_core.utils.KVUtils;

/**
 * 使用腾讯{@link com.tencent.mmkv.MMKV}进行保存，工具类{@link KVUtils}
 */
public class MmkvSessionManager extends SessionManager {
    private Gson mGson;
    // 用户信息获取比较频繁，作为一个字段去维护
    private Object mUserInfo;


    public MmkvSessionManager(Context context) {
        Context mContext = context.getApplicationContext();
        if (mContext == null) {
            throw new NullPointerException("请初始化SessionManger");
        }
        KVUtils.init(mContext);
    }

    @Override
    public boolean isLogin() {
        return getUser() != null;
    }

    @Nullable
    @Override
    public <T> T getUser() {
        if (sConfig.getUserClass() == null) return null;
        try {
            if (mUserInfo != null) {
                return (T) mUserInfo;
            }
            String json = (String) KVUtils.get(Constants.KEY_SESSION_USER, null);
            if (TextUtils.isEmpty(json)) return null;
            if (mGson == null) mGson = new Gson();
            return (T) mGson.fromJson(json, sConfig.getUserClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> void setUser(T user) {
        if (user == null) return;
        if (mGson == null) mGson = new Gson();
        String json = mGson.toJson(user);
        KVUtils.put(Constants.KEY_SESSION_USER, json);
        mUserInfo = user;
        notifyUserInfoChanged();
    }

    @Override
    public <T> void setUserToken(T token) {
        if (token == null) return;
        String json = mGson.toJson(token);
        KVUtils.put(Constants.KEY_SESSION_TOKEN, json);
        notifyTokenChanged();
    }

    @Nullable
    @Override
    public <T> T getUserToken() {
        if (sConfig.getUserTokenClass() == null) return null;
        try {
            String json = (String) KVUtils.get(Constants.KEY_SESSION_TOKEN, null);
            if (TextUtils.isEmpty(json)) return null;
            return (T) mGson.fromJson(json, sConfig.getUserTokenClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
