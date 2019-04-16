package com.gcml.common;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.gcml.common.utils.JPushMessageHelper;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.ui.UiUtils;
import com.google.auto.service.AutoService;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import ren.yale.android.cachewebviewlib.WebViewCacheInterceptor;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptorInst;

@AutoService(AppLifecycleCallbacks.class)
public class BizApp implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        //初始化工具类
        UM.init(app);
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

        //预加载x5内核
        Intent intent = new Intent(app, X5CorePreLoadService.class);
        app.startService(intent);

        //webview缓存框架
        WebViewCacheInterceptorInst.getInstance().
                init(new WebViewCacheInterceptor.Builder(app));
    }

    @Override
    public void onTerminate(Application app) {

    }
}
