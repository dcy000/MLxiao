package com.gcml.common.repository;

import android.app.Application;
import android.content.Context;

import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;

public class RepositoryAppLifecycleCallbacks implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {
        RepositoryApp.INSTANCE.attachBaseContext(app, base);
    }

    @Override
    public void onCreate(Application app) {
        RepositoryApp.INSTANCE.onCreate(app);
    }

    @Override
    public void onTerminate(Application app) {
        RepositoryApp.INSTANCE.onTerminate(app);
    }
}
