package com.gcml.call;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;

import java.security.MessageDigest;

/**
 * Created by afirez on 2017/10/20.
 */

public class CallAuthHelper {

    private CallAuthHelper() {}

    public static CallAuthHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CallAuthHelper INSTANCE = new CallAuthHelper();
    }

    private String mAccount;
    private String mToken;
    private StatusBarNotificationConfig mNotificationConfig;

    public boolean isLogined() {
        String account = getAccount();
        String token = getToken();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            return true;
        }
        return false;
    }

    public void loginWithPassword(String account, String password, RequestCallback<LoginInfo> callback) {
        login(account, md5(password), callback);
    }

    public static String md5(String text) {
        if (text == null || text.trim().length() == 0) {
            throw new IllegalArgumentException("text == null or empty");
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return bytesToHexString(md5.digest(text.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String bytesToHexString(byte[] data) {
        if (data == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int length = data.length;
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(0xFF & data[i]);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    public AbortableFuture<LoginInfo> login(final String account, final String token, final RequestCallback<LoginInfo> callback) {
        LoginInfo info = new LoginInfo(account, token);
        AuthService service = NIMClient.getService(AuthService.class);
        AbortableFuture<LoginInfo> future = service.login(info);
//        future.setCallback(new RequestCallbackWrapper<LoginInfo>() {
//
//            @Override
//            public void onResult(int code, LoginInfo result, Throwable exception) {
//
//            }
//        });
        future.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                if (param == null){
                    return;
                }
                setAccount(param.getAccount());
                setToken(param.getToken());
                if (callback != null) {
                    callback.onSuccess(param);
                }
            }

            @Override
            public void onFailed(int code) {
                if (callback != null) {
                    callback.onFailed(code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                if (callback != null) {
                    callback.onException(exception);
                }
            }
        });
        return future;
    }

    public LoginInfo loginInfo() {
        String account = getAccount();
        String token = getToken();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            return new LoginInfo(account, token);
        }
        return null;
    }

    public void logout() {
        NIMClient.getService(AuthService.class).logout();
    }

    public void observeOnlineStatus(Observer<StatusCode> observer, boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                observer, register);
    }

    public void observeLoginSyncDataStatus(Observer<LoginSyncStatus> observer, boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(observer, register);
    }

    public String getAccount() {
        if (!TextUtils.isEmpty(mAccount)) {
            return mAccount;
        }
        Application app = CallApp.INSTANCE.getApp();
        SharedPreferences preferences = app.getSharedPreferences("config-call", Context.MODE_PRIVATE);
        return preferences.getString("account", "");
    }

    public String getToken() {
        if (!TextUtils.isEmpty(mToken)) {
            return mToken;
        }
        Application app = CallApp.INSTANCE.getApp();
        SharedPreferences preferences = app.getSharedPreferences("config-call", Context.MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    public void setAccount(String account) {
        mAccount = account;
        Application app = CallApp.INSTANCE.getApp();
        SharedPreferences preferences = app.getSharedPreferences("config-call", Context.MODE_PRIVATE);
        preferences.edit().putString("account", account).apply();
    }

    public void setToken(String token) {
        mToken = token;
        Application app = CallApp.INSTANCE.getApp();
        SharedPreferences preferences = app.getSharedPreferences("config-call", Context.MODE_PRIVATE);
        preferences.edit().putString("account", token).apply();
    }

    public void removeUserInfo() {
        mAccount = "";
        mToken = "";
        Application app = CallApp.INSTANCE.getApp();
        SharedPreferences preferences = app.getSharedPreferences("config-call", Context.MODE_PRIVATE);
        preferences.edit()
                .putString("account", "")
                .putString("token", "")
                .apply();
    }
}
