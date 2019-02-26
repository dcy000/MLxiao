package com.gcml.call;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;

@AutoService(AppLifecycleCallbacks.class)
public class CallAppLifecycleCallbacks implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {
        CallApp.INSTANCE.attachBaseContext(app, base);
    }

    @Override
    public void onCreate(Application app) {
        CallApp.INSTANCE.onCreate(app);
    }

    @Override
    public void onTerminate(Application app) {
        CallApp.INSTANCE.onTerminate(app);
    }
}
