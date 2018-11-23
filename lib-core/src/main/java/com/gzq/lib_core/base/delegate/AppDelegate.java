package com.gzq.lib_core.base.delegate;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.utils.ManifestParser;
import com.gzq.lib_core.utils.ProcessUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 对实现了AppLifecycle的接口进行生命周期分发
 */
public class AppDelegate implements AppLifecycle {

    private Application mApplication;
    private List<AppLifecycle> appLifecycles;
    private ActivityLifecycleCallbacks activityDelegate;


    public AppDelegate(@NonNull Context context) {
        if (activityDelegate == null) {
            appLifecycles = new ManifestParser<AppLifecycle>(context, MetaValue.APP_LIFECYCLE).parse();
        }
    }

    @Override
    public void attachBaseContext(@NonNull Context base) {
        if (appLifecycles != null) {
            for (AppLifecycle app : appLifecycles) {
                app.attachBaseContext(base);
            }
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {
        mApplication = application;
        if (appLifecycles != null) {
            for (AppLifecycle app : appLifecycles) {
                app.onCreate(application);
            }
        }
        if (activityDelegate == null) {
            activityDelegate = new ActivityLifecycleCallbacks();
        }
        //注册所有Activity生命周期的监听
        mApplication.registerActivityLifecycleCallbacks(activityDelegate);
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        if (activityDelegate != null) {
            mApplication.unregisterActivityLifecycleCallbacks(activityDelegate);
        }
        if (appLifecycles != null) {
            for (AppLifecycle app : appLifecycles) {
                app.onTerminate(application);
            }
        }
        activityDelegate = null;
        appLifecycles = null;
        mApplication = null;

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (appLifecycles != null) {
            for (AppLifecycle app : appLifecycles) {
                app.onConfigurationChanged(newConfig);
            }
        }
    }

    @Override
    public IEvents provideEvents() {
        return null;
    }
    public List<AppLifecycle> getAppLifecycles(){
        return appLifecycles;
    }
}
