package com.medlink.mall;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by afirez on 2018/5/31.
 */

public enum MallApp {

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

    }

    public void onTerminate(Application app) {

    }
}
