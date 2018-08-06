package com.gcml.common;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.billy.cc.core.component.CC;
import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;

public class CCAppLifecycleCallbacks implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        boolean debug = (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        CC.enableVerboseLog(debug);
        CC.enableDebug(debug);
        CC.init(app);
//        CC.enableRemoteCC(true);
    }

    @Override
    public void onTerminate(Application app) {

    }
}
