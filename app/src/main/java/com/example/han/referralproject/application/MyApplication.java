package com.example.han.referralproject.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.new_music.LibMusicPlayer;
import com.example.han.referralproject.new_music.Preferences;
import com.example.han.referralproject.new_music.ScreenUtils;
import com.example.han.referralproject.new_music.ToastUtils;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.gcml.lib_video_ksyplayer.KSYPlayer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.medlink.danbogh.call2.NimInitHelper;
import com.medlink.danbogh.utils.JPushMessageHelper;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.UiUtils;
import com.medlink.danbogh.wakeup.WakeupHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import cn.beecloud.BeeCloud;
import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {
    private static MyApplication mInstance;
    public String userId;
    public String xfid;
    public String telphoneNum;
    public String userName;
    public static UserInfoBean account;
    public static Bitmap bitmap;

    public String nimUserId() {
        return "user_" + userId;
    }

    public String eqid;
    /**
     * 内核金山云
     */
    public static final int PLAN_ID_KSY = 1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        initBugly();
//        LeakCanary.install(this);
        LibMusicPlayer.init(this);
        Preferences.init(this);
        ScreenUtils.init(this);
        ToastUtils.init(this);
        ToastTool.init(this);
//        NoCrash.init(this);
//        NoCrash.getInstance().install();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.UMAnalyticsConfig umConfig = new MobclickAgent.UMAnalyticsConfig(
                this,
                "5a604f5d8f4a9d02230001b1",
                "GCML"
        );
        MobclickAgent.startWithConfigure(umConfig);
        UiUtils.init(this, 1920, 1200);
        UiUtils.compat(this, 1920);
        T.init(this);
        LitePal.initialize(this);
        mInstance = this;
        LocalShared mShared = LocalShared.getInstance(this);
        userId = mShared.getUserId();
        xfid = mShared.getXunfeiId();
        telphoneNum = mShared.getPhoneNum();
        eqid = mShared.getEqID();

        WakeupHelper.init(this);
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

        SpeechUtility.createUtility(this, builder.toString());


        NimInitHelper.getInstance().init(this, true);
//        initOkHttpUtils();

        BeeCloud.setAppIdAndSecret("51bc86ef-06da-4bc0-b34c-e221938b10c9", "4410cd33-2dc5-48ca-ab60-fb7dd5015f8d");

        //初始化极光
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        JPushMessageHelper.init();
        //初始化日志库
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        registerActivityLifecycleCallbacks(callback);
        EHSharedPreferences.initUNITContext(this);
        initVideoPlay(this);

    }
    private void initVideoPlay(Application app) {
        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_KSY, KSYPlayer.class.getName(), "Ksyplayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_KSY);
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        PlayerLibrary.init(app);
    }
    private void initBugly() {
//        Context context = getApplicationContext();
//        String packageName = context.getPackageName();
//        String processName = getProcessName(Process.myPid());
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(getApplicationContext(), "082d769382", true);
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    private static Activity currentActivity;
    private ActivityLifecycleCallbacks callback = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            currentActivity = activity;
//            Looper.myQueue().addIdleHandler(YiYuanIdleHandler.getInstance());
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {


        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UiUtils.compat(this, 1920);
    }


    public static MyApplication getInstance() {
        return mInstance;
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private static String getAppName(Context context, int pid) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    // 返回当前进程名
                    return info.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
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
