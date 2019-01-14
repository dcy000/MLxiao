package com.gcml.common;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.gcml.common.utils.JPushMessageHelper;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.ui.UiUtils;
import com.google.auto.service.AutoService;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

@AutoService(AppLifecycleCallbacks.class)
public class BizApp implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        //初始化工具类
        UtilsManager.init(app);
        UiUtils.init(app, 1920, 1200);
        app.registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onTrimMemory(int level) {

            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                UiUtils.compatWithOrientation(newConfig);
            }

            @Override
            public void onLowMemory() {

            }
        });
        //语音模块初始化
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

        SpeechUtility.createUtility(app, builder.toString());

        JPushMessageHelper.init();
    }

    @Override
    public void onTerminate(Application app) {

    }
}
