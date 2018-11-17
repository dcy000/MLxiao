package com.iflytek.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.iflytek.cloud.SpeechUtility;

public class SpeechApp implements AppLifecycle {

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

        SpeechUtility.createUtility(application, "appid=" + App.getApp().getResources().getString(R.string.app_id));

        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }
}
