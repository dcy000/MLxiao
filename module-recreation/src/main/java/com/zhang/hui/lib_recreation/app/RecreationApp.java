package com.zhang.hui.lib_recreation.app;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by lenovo on 2018/8/15.
 */
@AutoService(AppLifecycleCallbacks.class)
public class RecreationApp implements AppLifecycleCallbacks {
    public static Application application;

    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        application = app;
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(app, builder.toString());
    }

    @Override
    public void onTerminate(Application app) {

    }
}
