package com.gcml.mall;

import android.app.Application;
import android.content.Context;

import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;

public class MallAppLifecycleCallbacks implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {
        MallApplication.INSTANCE.attachBaseContext(app, base);
    }

    @Override
    public void onCreate(Application app) {
        MallApplication.INSTANCE.onCreate(app);
    }

    @Override
    public void onTerminate(Application app) {
        MallApplication.INSTANCE.onTerminate(app);
    }
}
