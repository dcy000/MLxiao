package com.gcml.old.update;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.gcml.common.communication.InterFaceManagerment;
import com.google.auto.service.AutoService;

/**
 * Created by lenovo on 2019/1/15.
 */
//@AutoService(AppLifecycleCallbacks.class)
public class AppUpdateLifeCallBacks implements AppLifecycleCallbacks {

    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        InterFaceManagerment.update=new UpdateActivity();
    }

    @Override
    public void onTerminate(Application app) {

    }
}
