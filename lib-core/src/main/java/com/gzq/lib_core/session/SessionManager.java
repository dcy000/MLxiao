/*
 * Copyright (c) 2017.
 */

package com.gzq.lib_core.session;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 会话管理
 */
public abstract class SessionManager {

    protected static SessionConfig sConfig;
    private static SessionManager manager;
    private final Set<SessionStateChangedListener> mSessionStateChangedListeners = new HashSet<>();

    /**
     * 获取默认的会话管理器，默认的为cookie 管理器。
     * 使用之前请使用{@link #init(SessionConfig)} 来进行初始化配置。
     */
    public static SessionManager get() {

        if (sConfig == null) {
            throw new NullPointerException("请初始化Session配置");
        }

        if (manager == null) {
            synchronized (SessionManager.class) {
                if (manager == null || manager.get() == null) {
                    SessionManager sessionManager = sConfig.getSessionManager();
                    if (sessionManager == null) {
                        manager = new PreferencesSessionManager(sConfig.getApplication());
                    } else {
                        manager = sessionManager;
                    }
                }
            }
        }

        return manager;
    }

    /**
     * 初始化会话管理器
     */
    public static void init(SessionConfig config) {
        if (sConfig != null) {
            sConfig = null;
            System.gc();
        }
        sConfig = config;
    }


    SessionManager() {

    }

    /**
     * 是否登录
     */
    public abstract boolean isLogin();


    /**
     * 清除会话信息，即退出登录。
     */
    public void clear() {
    }

    /**
     * 获取当前登录的用户信息，在调用该方法之前请先调用{@link #isLogin()}来判断是否登录
     */
    @Nullable
    public abstract <T> T getUser();

    /**
     * 设置当前用户信息
     */
    public abstract <T> void setUser(T user);

    /**
     * 设置用户授权信息
     *
     * @param token 授权信息
     */
    public abstract <T> void setUserToken(T token);

    /**
     * 获取用户授权信息
     */
    @Nullable
    public abstract <T> T getUserToken();

    /**
     * 添加Session状态改变通知，在不用时候记得移除掉，避免内存泄漏
     */
    public void addSessionStateChangedListener(@NonNull SessionStateChangedListener listener) {
        mSessionStateChangedListeners.add(listener);
    }

    /**
     * 移除Session状态改变通知
     */
    public void removeSessionStateChangedListener(@NonNull SessionStateChangedListener listener) {
        mSessionStateChangedListeners.remove(listener);
    }

    protected void notifyUserInfoChanged() {
        for (SessionStateChangedListener listener : mSessionStateChangedListeners) {
            listener.onUserInfoChanged(this);
        }
    }

    protected void notifyTokenChanged() {
        for (SessionStateChangedListener listener : mSessionStateChangedListeners) {
            listener.onTokenInfoChanged(this);
        }
    }

    protected void notifyUserInfoCleared() {
        for (SessionStateChangedListener listener : mSessionStateChangedListeners) {
            listener.onUserInfoCleared(this);
        }
    }
}
