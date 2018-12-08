package com.example.han.referralproject.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.events.AppEvents;
import com.example.han.referralproject.jipush.JPushMessageHelper;
import com.example.han.referralproject.new_music.LibMusicPlayer;
import com.example.module_call.ui.NimAccountHelper;
import com.example.module_call.ui.NimInitHelper;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.session.SessionManager;
import com.gzq.lib_core.session.SessionStateChangedListener;
import com.gzq.lib_core.utils.AppUtils;
import com.gzq.lib_core.utils.Handlers;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.litepal.LitePal;

import cn.beecloud.BeeCloud;
import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;


public class MyApplication implements AppLifecycle {
    private static MyApplication mInstance;
    /**
     * Buggly的AppId
     */
    private static final String BUGGLY_APPID = "8931446044";

    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        //初始化Buggly
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(application);
        userStrategy.setAppChannel(AppUtils.getAppInfo().getPackageName());
        userStrategy.setAppVersion(AppUtils.getAppInfo().getVersionName());
        CrashReport.initCrashReport(application, BUGGLY_APPID, BuildConfig.DEBUG, userStrategy);


        //友盟
        MobclickAgent.setScenarioType(application, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.UMAnalyticsConfig umConfig = new MobclickAgent.UMAnalyticsConfig(
                application,
                "5a604f5d8f4a9d02230001b1",
                "GCML"
        );
        MobclickAgent.startWithConfigure(umConfig);

        //初始化极光
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(application);
        JPushMessageHelper.init();

        //其他
        LibMusicPlayer.init(application);
        //LitePal数据库
        LitePal.initialize(application);
        mInstance = this;
        NimInitHelper.getInstance().init(application, true);

        BeeCloud.setAppIdAndSecret("51bc86ef-06da-4bc0-b34c-e221938b10c9", "4410cd33-2dc5-48ca-ab60-fb7dd5015f8d");


        observerSessions();
    }

    /**
     * 如果Sessions发生变化了则重新登录一次网易云信账号
     * 并且全局只设置一处监听即可
     */
    private void observerSessions() {

        Box.getSessionManager().addSessionStateChangedListener(new SessionStateChangedListener() {
            @Override
            public void onUserInfoChanged(SessionManager sessionManager) {
                UserInfoBean user = sessionManager.getUser();
                Timber.tag("SessionManager").i(user.toString());
                Handlers.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(user.wyyxId) && !TextUtils.isEmpty(user.wyyxPwd)) {
                            //视频通话必须在主线程登录
                            NimAccountHelper.getInstance().login(user.wyyxId, user.wyyxPwd, null);
                        }
                        if (user != null) {
                            //极光设置别名，友盟登陆
                            new JpushAliasUtils(Box.getApp()).setAlias("user_" + user.bid);
                            MobclickAgent.onProfileSignIn(user.bid);
                        }
                    }
                });
            }

            @Override
            public void onTokenInfoChanged(SessionManager sessionManager) {

            }
        });

    }

    @Override
    public void onTerminate(@NonNull Application application) {
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    @Override
    public IEvents provideEvents() {
        return new AppEvents();
    }


    public static MyApplication getInstance() {
        return mInstance;
    }

    private HandlerThread mBgThread = new HandlerThread("speech", Process.THREAD_PRIORITY_AUDIO);

    {
        mBgThread.start();
    }

    private Handler mBgHandler;

    public Handler getBgHandler() {
        return mBgHandler == null ? new Handler(mBgThread.getLooper()) : mBgHandler;
    }
}
