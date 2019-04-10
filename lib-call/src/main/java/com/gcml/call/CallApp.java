package com.gcml.call;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public enum CallApp {

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
        CallInitHelper.getInstance().init(app, true);
    }

    public void onTerminate(Application app) {

    }
}
