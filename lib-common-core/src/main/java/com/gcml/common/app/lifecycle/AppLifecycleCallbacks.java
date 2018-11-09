package com.gcml.common.app.lifecycle;

import android.app.Application;
import android.content.Context;

/**
 * Created by afirez on 2018/6/15.
 */

public interface AppLifecycleCallbacks {

    void attachBaseContext(Application app, Context base);

    void onCreate(Application app);

    void onTerminate(Application app);

}
