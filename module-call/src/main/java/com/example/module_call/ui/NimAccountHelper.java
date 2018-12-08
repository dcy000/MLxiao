package com.example.module_call.ui;

import android.text.TextUtils;

import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.KVUtils;
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

/**
 * Created by lenovo on 2017/10/20.
 */

public class NimAccountHelper {
    private static final String KEY_NIM_ACCOUNT = "key_nim_account";
    private static final String KEY_NIM_TOKEN = "key_nim_token";

    private NimAccountHelper() {
    }

    public static NimAccountHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final NimAccountHelper INSTANCE = new NimAccountHelper();
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
        login(account, DataUtils.md5(password), callback);
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
                if (param == null) {
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
        return (String) KVUtils.get(KEY_NIM_ACCOUNT, "");
    }

    public String getToken() {
        return (String) KVUtils.get(KEY_NIM_TOKEN, "");
    }

    public void setAccount(String account) {
        mAccount = account;
        KVUtils.put(KEY_NIM_ACCOUNT, account);
    }

    public void setToken(String token) {
        mToken = token;
        KVUtils.put(KEY_NIM_TOKEN, token);
    }

    public static void removeUserInfo() {
        KVUtils.remove(KEY_NIM_TOKEN);
        KVUtils.remove(KEY_NIM_ACCOUNT);
    }
}
