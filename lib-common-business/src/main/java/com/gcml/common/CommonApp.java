package com.gcml.common;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.gcml.common.utils.JPushMessageHelper;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.ui.UiUtils;
import com.google.auto.service.AutoService;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.sjtu.yifei.route.Routerfit;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

@AutoService(AppLifecycleCallbacks.class)
public class CommonApp implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        // 工具类
        UM.init(app);
        // end 工具类

        // UI 适配
        UiUtils.init(app, 1920, 1200);
        // end UI 适配

        // 友盟
        MobclickAgent.setScenarioType(app, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.UMAnalyticsConfig umConfig = new MobclickAgent.UMAnalyticsConfig(
                app,
                "5a604f5d8f4a9d02230001b1",
                "GCML"
        );
        MobclickAgent.setCatchUncaughtExceptions(false);
        MobclickAgent.startWithConfigure(umConfig);
        // end 友盟

        // 语音模块
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(app, builder.toString());
        //end 语音模块

        // 极光推送
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(app);
        JPushMessageHelper.init();
        // end 极光推送

        // 路由 ARetrofit
        Routerfit.init(app);
        // end 路由 ARetrofit
    }

    @Override
    public void onTerminate(Application app) {

    }
}
