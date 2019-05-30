package com.gcml.common.internal;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.google.auto.service.AutoService;

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@AutoService(Application.ActivityLifecycleCallbacks.class)
public class AppActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) { }
    @Override
    public void onActivityStarted(Activity activity) { }
    @Override
    public void onActivityResumed(Activity activity) { }
    @Override
    public void onActivityPaused(Activity activity) { }
    @Override
    public void onActivityStopped(Activity activity) { }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }
    @Override
    public void onActivityDestroyed(Activity activity) {
        LeakCanaryHelper.INSTANCE.watcher().watch(activity);
    }
}
