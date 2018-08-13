package com.zhang.hui.lib_mlvoice;

import android.app.Application;
import android.content.Context;

import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by lenovo on 2018/8/13.
 */

public class VoiceApp implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        SpeechUtility.createUtility(app, "appid=" + "59196d96");
    }

    @Override
    public void onTerminate(Application app) {

    }
}
