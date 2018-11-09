package com.gcml.common.app.quality;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

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
