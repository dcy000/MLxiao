package com.example.lenovo.rto.init;

import android.app.Application;
import android.content.Context;

import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;

@AutoService(AppLifecycleCallbacks.class)
public class BaiduUnitInit implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        EHSharedPreferences.initUNITContext(app);
    }

    @Override
    public void onTerminate(Application app) {

    }
}
