package com.gcml.web;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;

@AutoService(AppLifecycleCallbacks.class)
public class App implements AppLifecycleCallbacks {

    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        TbsInitHelper.initAsync(app);
    }

    @Override
    public void onTerminate(Application app) {

    }
}
