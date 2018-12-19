package com.gcml.common.internal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.auto.service.AutoService;

@AutoService(FragmentManager.FragmentLifecycleCallbacks.class)
public class AppFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {
    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        LeakCanaryHelper.INSTANCE.watcher().watch(f);
    }
}
