package com.gcml.common.app.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by lenovo on 2018/9/2.
 */

public class TopActivityHelper implements Application.ActivityLifecycleCallbacks {
    public static Activity topActivity;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        topActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        topActivity = null;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
