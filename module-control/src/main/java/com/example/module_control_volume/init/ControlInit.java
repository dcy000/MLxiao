package com.example.module_control_volume.init;

import android.app.Application;
import android.content.Context;

import com.example.module_control_volume.wakeup.WakeupHelper;
import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;

import timber.log.Timber;

@AutoService(AppLifecycleCallbacks.class)
public class ControlInit implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        WakeupHelper.init(app);
        Timber.i("wake up is init");
    }

    @Override
    public void onTerminate(Application app) {

    }
}
