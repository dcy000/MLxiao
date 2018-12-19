package com.gcml.family;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;

/**
 * Created by lenovo on 2018/8/15.
 */

public class FamilyApplication implements AppLifecycleCallbacks {
    public static Application application;

    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        application = app;
    }

    @Override
    public void onTerminate(Application app) {

    }
}
