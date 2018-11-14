package com.gzq.lib_core.session;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Session配置信息
 * Created by ChenRui on 2018/3/26 0026 14:54.
 */
public final class SessionConfig {
    private Class<?> userTokenClass;
    private Class<?> userClass;
    private Context mApplication;
    private SessionManager mSessionManager;

    private SessionConfig() {
    }

    public Class<?> getUserTokenClass() {
        return userTokenClass;
    }

    public Class<?> getUserClass() {
        return userClass;
    }

    @Nullable
    public Context getApplication() {
        return mApplication;
    }

    public SessionManager getSessionManager() {
        return mSessionManager;
    }

    public static class Builder {

        private final SessionConfig mConfig;

        public Builder() {
            mConfig = new SessionConfig();
            mConfig.userTokenClass = SessionToken.class;
            mConfig.userClass = SessionUserInfo.class;
        }

        public SessionConfig.Builder tokenClass(Class<?> cls) {
            mConfig.userTokenClass = cls;
            return this;
        }

        public SessionConfig.Builder userClass(Class<?> cls) {
            mConfig.userClass = cls;
            return this;
        }

        public SessionConfig.Builder sessionManager(SessionManager sessionManager) {

            mConfig.mSessionManager = sessionManager;
            return this;
        }

        public SessionConfig.Builder withContext(Context context) {
            mConfig.mApplication = context.getApplicationContext();
            return this;
        }

        public SessionConfig build() {
            Context context = mConfig.mApplication;
            if (context == null) {
                throw new NullPointerException("must config context");
            }
            if (mConfig.mSessionManager == null) {
                mConfig.mSessionManager = new PreferencesSessionManager(context);
            }
            return mConfig;
        }
    }


}
