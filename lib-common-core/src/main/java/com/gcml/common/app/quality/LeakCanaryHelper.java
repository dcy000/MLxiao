package com.gcml.common.app.quality;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public enum LeakCanaryHelper {
    INSTANCE;

    private RefWatcher watcher;

    public RefWatcher watcher() {
        return watcher;
    }

    public void install(Application app) {
        if (LeakCanary.isInAnalyzerProcess(app)) {
            watcher = RefWatcher.DISABLED;
        } else {
            watcher = LeakCanary.install(app);
        }
    }
}
