package com.gcml.mall;

import android.app.Application;
import android.content.Context;

import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;

public class MallAppLifecycleCallbacks implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {
        MallApp.INSTANCE.attachBaseContext(app, base);
    }

    @Override
    public void onCreate(Application app) {
        MallApp.INSTANCE.onCreate(app);
    }

    @Override
    public void onTerminate(Application app) {
        MallApp.INSTANCE.onTerminate(app);
    }
}
