package com.iflytek.app;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import timber.log.Timber;

@AutoService(AppLifecycleCallbacks.class)
public class SpeechApp implements AppLifecycleCallbacks {

    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(app, builder.toString());
        Timber.i("voice init");
    }

    @Override
    public void onTerminate(Application app) {

    }
}
