package com.gcml.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.gcml.common.AppDelegate;

/**
 * Created by afirez on 2018/6/15.
 */

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        AppDelegate.INSTANCE.attachBaseContext(this, base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDelegate.INSTANCE.onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppDelegate.INSTANCE.onTerminate(this);
    }
}