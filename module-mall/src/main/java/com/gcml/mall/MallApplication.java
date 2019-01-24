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
//        BeeCloud.setAppIdAndSecret("51bc86ef-06da-4bc0-b34c-e221938b10c9", "4410cd33-2dc5-48ca-ab60-fb7dd5015f8d");
        BeeCloud.setAppIdAndSecret("91ee2a0a-661d-4d81-8979-547124be340d", "b8b53d06-5571-404a-bda2-a1d0b8bca0e8");
    }

    public void onTerminate(Application app) {

    }
}
