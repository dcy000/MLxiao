package com.example.han.referralproject.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.floatingball.AssistiveTouchService;
import com.example.han.referralproject.music.AppCache;
import com.example.han.referralproject.music.ForegroundObserver;
import com.example.han.referralproject.music.HttpInterceptor;
import com.example.han.referralproject.music.Preferences;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.medlink.danbogh.call.CallManager;
import com.medlink.danbogh.call.CallReceiver;
import com.medlink.danbogh.call.EMAccountHelper;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.call2.NimInitHelper;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.UiUtils;
import com.medlink.danbogh.wakeup.WakeupHelper;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePal;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.beecloud.BeeCloud;
import okhttp3.OkHttpClient;


public class MyApplication extends Application {
    private static MyApplication mInstance;
    public String userId;
    public String telphoneNum;

    public String emDoctorId = "gcml_doctor_18940866148";
    public String userName;

    public String nimUserId() {
        return "user_" + userId;
    }

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

//
//        if (isMyServiceRunning(AssistiveTouchService.class)) {
//
//        } else {
//            Intent intent = new Intent(getApplicationContext(), AssistiveTouchService.class);
//            startService(intent);
//        }


        UiUtils.init(this, 1980, 1200);
        UiUtils.compat(this, 1980);
        UiUtils.init(this, 1920, 1200);
        UiUtils.compat(this, 1920);
        T.init(this);
        LitePal.initialize(this);
        mInstance = this;
        LocalShared mShared = LocalShared.getInstance(this);
        userId = mShared.getUserId();
        telphoneNum = mShared.getPhoneNum();
        WakeupHelper.init(this);
        StringBuilder builder = new StringBuilder();
        builder.append("appid=").

                append(getString(R.string.app_id)).

                append(",")
                .

                        append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

        SpeechUtility utility = SpeechUtility.createUtility(this, builder.toString());
        //EM
//        initHyphenate(this);
        //if (telphoneNum != null) {
//        EMAccountHelper.login(emBrId(), "123");
        //}
        NimInitHelper.getInstance().

                init(this, true);

        AppCache.init(this);
        AppCache.updateNightMode(Preferences.isNightMode());
        ForegroundObserver.init(this);

        initOkHttpUtils();

        BeeCloud.setAppIdAndSecret("2732d773-09a4-403d-87b4-b040d14ce4b9",
                "ffa06c16-c2ee-4b48-a65c-795936d53cc7");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UiUtils.compat(this, 1980);
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

    private CallReceiver callReceiver;

    /**
     * 初始化环信sdk，并做一些注册监听的操作
     */
    private void initHyphenate(Context context) {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(this, pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }

        // 初始化sdk的一些配置
        EMOptions options = new EMOptions();
        options.setAutoLogin(true);
        // 动态设置appkey，如果清单配置文件设置了 appkey，这里可以不用设置
        //nimOptions.setAppKey("yunshangzhijia#yunyue");

        // 设置小米推送 appID 和 appKey
//        nimOptions.setMipushConfig("2882303761517573806", "5981757315806");

        // 设置消息是否按照服务器时间排序
        options.setSortMessageByServerTime(false);
        // 初始化环信SDK,一定要先调用init()
//        EMClient.getInstance().init(context, options);
        // 开启 debug 模式
        EMClient.getInstance().setDebugMode(true);
        // 设置通话广播监听器
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }
        //注册通话广播接收者
        context.registerReceiver(callReceiver, callFilter);
        // 通话管理类的初始化
        CallManager.getInstance().init(context);
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
