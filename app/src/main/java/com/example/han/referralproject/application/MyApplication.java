
package com.example.han.referralproject.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.new_music.LibMusicPlayer;
import com.example.han.referralproject.new_music.Preferences;
import com.example.han.referralproject.util.LocalShared;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.gcml.common.app.lifecycle.App;
import com.gcml.common.app.lifecycle.AppDelegate;
import com.gcml.common.data.UserSpHelper;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.service.ProcessUtils;
import com.gcml.lib_utils.ui.UiUtils;
import com.gcml.lib_video_ksyplayer.KSYPlayer;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.medlink.danbogh.call2.NimInitHelper;
import com.medlink.danbogh.wakeup.WakeupHelper;
import com.umeng.analytics.MobclickAgent;

import org.litepal.LitePal;

import cn.beecloud.BeeCloud;
import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {
    private static MyApplication mInstance;

    /**
     * @see UserSpHelper#getUserId()
     */
    @Deprecated
    public String userId;

    /**
     * @see UserSpHelper#getFaceId()
     */
    @Deprecated
    public String xfid;
    @Deprecated
    public String telphoneNum;
    @Deprecated
    public String userName;
    @Deprecated
    public String hypertensionHand;
    @Deprecated
    public String nimUserId() {
        return "user_" + UserSpHelper.getUserId();
    }

    @Deprecated
    public String eqid;
    public static final int PLAN_ID_KSY = 1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        AppDelegate.INSTANCE.attachBaseContext(this, base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppDelegate.INSTANCE.onTerminate(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDelegate.INSTANCE.onCreate(this);
        String curProcessName = ProcessUtils.getCurProcessName(this);
        UtilsManager.init(this);
        UiUtils.init(this, 1920, 1200);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        EHSharedPreferences.initUNITContext(this);
//        LeakCanary.install(this);
        LibMusicPlayer.init(this);
        Preferences.init(this);
        //初始化蓝牙连接库
        BluetoothClientManager.init(this);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.UMAnalyticsConfig umConfig = new MobclickAgent.UMAnalyticsConfig(
                this,
                "5a604f5d8f4a9d02230001b1",
                "GCML"
        );
        MobclickAgent.startWithConfigure(umConfig);
        //友盟崩溃信息收集开关
        MobclickAgent.setCatchUncaughtExceptions(false);
        LitePal.initialize(this);
        mInstance = this;
        LocalShared mShared = LocalShared.getInstance(this);
        xfid = mShared.getXunfeiId();
        telphoneNum = mShared.getPhoneNum();
        eqid = mShared.getEqID();

        WakeupHelper.init(this);

        BeeCloud.setAppIdAndSecret("51bc86ef-06da-4bc0-b34c-e221938b10c9", "4410cd33-2dc5-48ca-ab60-fb7dd5015f8d");

        //初始化极光
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        NimInitHelper.getInstance().init(this, true);
        initVideoPlay();
    }

    private void initVideoPlay() {
        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_KSY, KSYPlayer.class.getName(), "Ksyplayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_KSY);
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        PlayerLibrary.init(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UiUtils.compatWithOrientation(newConfig);
//        String curProcessName = ProcessUtils.getCurProcessName(this);
//        if (!TextUtils.isEmpty(curProcessName)) {
//            if (curProcessName.equals("com.example.han.referralproject:pushcore")) {
//                //启动音量控制悬浮按钮
//                VolumeControlFloatwindow.init(this.getApplicationContext());
//            }
//        }
    }


    public static MyApplication getInstance() {
        return mInstance;
    }


    @Deprecated
    private HandlerThread mBgThread = new HandlerThread("speech", Process.THREAD_PRIORITY_AUDIO);

    {
        mBgThread.start();
    }

    private Handler mBgHandler;

    public Handler getBgHandler() {
        return mBgHandler == null ? new Handler(mBgThread.getLooper()) : mBgHandler;
    }

}
