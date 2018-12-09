/*
 * Copyright (c) 2017.
 */

package com.gzq.lib_core.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.constant.Constants;
import com.gzq.lib_core.utils.SPUtil;

/**
 * 默认使用{@link SharedPreferences}进行用户信息的保存
 */
public class PreferencesSessionManager extends SessionManager {

    private Gson mGson;
    // 用户信息获取比较频繁，作为一个字段去维护
    private Object mUserInfo;

    public PreferencesSessionManager(Context context) {
        Context mContext = context.getApplicationContext();
        if (mContext == null)
            throw new NullPointerException("请初始化SessionManger");
    }

    @Override
    public boolean isLogin() {
        return getUser() != null;
    }

    @Override
    public void clear() {
        super.clear();
        mUserInfo = null; // 清除本地缓存字段
        SPUtil.remove(Constants.KEY_SESSION_USER);
        SPUtil.remove(Constants.KEY_SESSION_TOKEN);
        notifyUserInfoCleared();
    }

    @Override
    @Nullable
    public <T> T getUser() {
        if (sConfig.getUserClass() == null) return null;
        try {
            if (mUserInfo != null) {
                return (T) mUserInfo;
            }
            String json = (String) SPUtil.get(Constants.KEY_SESSION_USER, "");
            if (TextUtils.isEmpty(json)) return null;
            mGson = Box.getGson();
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
        mGson = Box.getGson();
        if (mGson == null) mGson = new Gson();
        String json = mGson.toJson(user);
        SPUtil.put(Constants.KEY_SESSION_USER, json);
        mUserInfo = user;
        notifyUserInfoChanged();
    }

    @Override
    public <T> void setUserToken(T token) {
        if (token == null) return;
        mGson = Box.getGson();
        if (mGson == null) mGson = new Gson();
        String json = mGson.toJson(token);
        SPUtil.put(Constants.KEY_SESSION_TOKEN, json);
        notifyTokenChanged();
    }

    @Override
    @Nullable
    public <T> T getUserToken() {
        if (sConfig.getUserTokenClass() == null) return null;
        try {
            String json = (String) SPUtil.get(Constants.KEY_SESSION_TOKEN, "");
            if (TextUtils.isEmpty(json)) return null;
            mGson = Box.getGson();
            if (mGson == null) mGson = new Gson();
            return (T) mGson.fromJson(json, sConfig.getUserTokenClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
