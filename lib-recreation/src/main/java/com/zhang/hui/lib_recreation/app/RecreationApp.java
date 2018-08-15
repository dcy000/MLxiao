package com.zhang.hui.lib_recreation.app;

import android.app.Application;
import android.content.Context;

import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;

/**
 * Created by lenovo on 2018/8/15.
 */

public class RecreationApp implements AppLifecycleCallbacks {
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
