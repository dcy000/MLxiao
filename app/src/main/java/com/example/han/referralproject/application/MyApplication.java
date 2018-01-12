package com.example.han.referralproject.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.R;
import com.example.han.referralproject.music.AppCache;
import com.example.han.referralproject.music.ForegroundObserver;
import com.example.han.referralproject.music.HttpInterceptor;
import com.example.han.referralproject.music.Preferences;
import com.example.han.referralproject.util.LocalShared;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.medlink.crash.NoCrash;
import com.medlink.danbogh.call2.NimInitHelper;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.UiUtils;
import com.medlink.danbogh.wakeup.WakeupHelper;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.beecloud.BeeCloud;
import cn.jpush.android.api.DefaultPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;


public class MyApplication extends Application {
    private static MyApplication mInstance;
    public String userId;
    public String xfid;
    public String telphoneNum;

    public String emDoctorId = "gcml_doctor_18940866148";
    public String userName;

    public String nimUserId() {
        return "user_" + userId;
    }

    public String eqid;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        NoCrash.init(this);
//        NoCrash.getInstance().install();
//        LeakCanary.install(this);
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
                .append(getString(R.string.app_id))
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

        SpeechUtility utility = SpeechUtility.createUtility(this, builder.toString());
        NimInitHelper.getInstance().

                init(this, true);

        AppCache.init(this);
        AppCache.updateNightMode(Preferences.isNightMode());
        ForegroundObserver.init(this);

        initOkHttpUtils();

        BeeCloud.setAppIdAndSecret("51bc86ef-06da-4bc0-b34c-e221938b10c9", "4410cd33-2dc5-48ca-ab60-fb7dd5015f8d");

        //初始化极光
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UiUtils.compat(this, 1920);
    }

    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        OkHttpUtils.initClient(okHttpClient);
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
