package com.gcml.module_health_record.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.gcml.module_health_record.events.HealthRecordEvents;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.base.ui.IEvents;

public class HealthRecordApp implements AppLifecycle {
    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {

    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    @Override
    public IEvents provideEvents() {
        return new HealthRecordEvents();
    }
}
