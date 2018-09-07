package com.gcml.mall;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import cn.beecloud.BeeCloud;

/**
 * Created by afirez on 2018/5/31.
 */

public enum MallApplication {

    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    private Application mApp;

    public Application getApp() {
        return mApp;
    }

    public void attachBaseContext(Application app, Context base) {
        mApp = app;
    }

    public void onCreate(Application app) {
        BeeCloud.setAppIdAndSecret("51bc86ef-06da-4bc0-b34c-e221938b10c9", "4410cd33-2dc5-48ca-ab60-fb7dd5015f8d");
    }

    public void onTerminate(Application app) {

    }
}
