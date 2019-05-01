package com.zhang.hui.lib_recreation.app;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;

@AutoService(AppLifecycleCallbacks.class)
public class CCAppLifecycleCallbacks implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
//        boolean debug = (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
//        CC.enableVerboseLog(debug);
//        CC.enableDebug(debug);
//        CC.init(app);
//        CC.enableRemoteCC(true);
    }

    @Override
    public void onTerminate(Application app) {

    }
}
