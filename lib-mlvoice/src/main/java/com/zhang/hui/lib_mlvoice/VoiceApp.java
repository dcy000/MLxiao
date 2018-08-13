package com.zhang.hui.lib_mlvoice;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

import com.gcml.common.app.lifecycle.App;
import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by lenovo on 2018/8/13.
 */

public class VoiceApp implements AppLifecycleCallbacks {
    public static ContentResolver context;
    public static Application app;

    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        context = app.getContentResolver();
        VoiceApp.app = app;
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
