package com.example.han.referralproject.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.example.han.referralproject.R;
import com.example.han.referralproject.util.LocalShared;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.iflytek.cloud.SpeechUtility;
import com.medlink.danbogh.call.CallManager;
import com.medlink.danbogh.call.CallReceiver;
import com.medlink.danbogh.call.EMAccountHelper;

import org.litepal.LitePal;

import java.util.Iterator;
import java.util.List;


public class MyApplication extends Application {
    private static MyApplication mInstance;
    public String userId;
    public String telphoneNum;

    public String emDoctorId = "gcml_doctor_18940866148";

    public String emBrId() {
        return "gcml_br_" + "12345678912";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        mInstance = this;
        LocalShared mShared = LocalShared.getInstance(this);
        userId = mShared.getUserId();
        telphoneNum = mShared.getPhoneNum();
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        //EM
        initHyphenate(this);
        //if (telphoneNum != null) {
            EMAccountHelper.login(emBrId(), "123");
        //}
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
        //options.setAppKey("yunshangzhijia#yunyue");

        // 设置小米推送 appID 和 appKey
//        options.setMipushConfig("2882303761517573806", "5981757315806");

        // 设置消息是否按照服务器时间排序
        options.setSortMessageByServerTime(false);
        // 初始化环信SDK,一定要先调用init()
        EMClient.getInstance().init(context, options);
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
}
