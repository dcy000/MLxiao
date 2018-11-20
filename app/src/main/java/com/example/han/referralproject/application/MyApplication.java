package com.example.han.referralproject.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.jipush.JPushMessageHelper;
import com.example.han.referralproject.new_music.LibMusicPlayer;
import com.example.han.referralproject.new_music.Preferences;
import com.example.han.referralproject.new_music.ScreenUtils;
import com.example.han.referralproject.util.LocalShared;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.utils.AppUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.medlink.danbogh.call2.NimInitHelper;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import org.litepal.LitePal;

import cn.beecloud.BeeCloud;
import cn.jpush.android.api.JPushInterface;


public class MyApplication implements AppLifecycle {
    private static MyApplication mInstance;
    public String userId;
    public String xfid;
    public String telphoneNum;
    public String userName;

    public String nimUserId() {
        return "user_" + userId;
    }

    public String eqid;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        LibMusicPlayer.init(application);
        Preferences.init(application);
        ScreenUtils.init(application);
        MobclickAgent.setScenarioType(application, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.UMAnalyticsConfig umConfig = new MobclickAgent.UMAnalyticsConfig(
                application,
                "5a604f5d8f4a9d02230001b1",
                "GCML"
        );
        MobclickAgent.startWithConfigure(umConfig);
        LitePal.initialize(application);
        mInstance = this;
        LocalShared mShared = LocalShared.getInstance(application);
        userId = mShared.getUserId();
        xfid = mShared.getXunfeiId();
        telphoneNum = mShared.getPhoneNum();
        eqid = mShared.getEqID();


        NimInitHelper.getInstance().init(application, true);
//        initOkHttpUtils();

        BeeCloud.setAppIdAndSecret("51bc86ef-06da-4bc0-b34c-e221938b10c9", "4410cd33-2dc5-48ca-ab60-fb7dd5015f8d");

        //初始化极光
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(application);
        JPushMessageHelper.init();
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

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
