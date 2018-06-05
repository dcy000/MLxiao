package com.ml.call;

import android.app.Application;
import android.content.Context;

import com.ml.call.utils.T;

/**
 * Created by afirez on 2018/5/31.
 */

public class CallApp {

    private static class Holder {
        private static CallApp sInstance = new CallApp();
    }

    public static CallApp getInstance() {
        return Holder.sInstance;
    }

    private Application mApp;

    public Application getApp() {
        return mApp;
    }

    public void attachBaseContext(Application app, Context base) {
        mApp = app;
    }

    public void onCreate(Application app) {
        T.init(app);
        NimInitHelper.getInstance().init(app, true);
    }

    public void onTerminate(Application app) {

    }
}
