package com.gcml.common;

import android.app.Application;
import android.content.Context;

import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;
import com.gcml.common.utils.JPushMessageHelper;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.ui.UiUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class CommonApp implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        //初始化工具类
        UtilsManager.init(app);
        UiUtils.init(app, 1920, 1200);
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
