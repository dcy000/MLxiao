package com.ml.call.app;

import android.app.Application;
import android.content.Context;

import com.ml.call.CallApp;

/**
 * Created by lenovo on 2018/5/31.
 */

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        CallApp.INSTANCE.attachBaseContext(this, base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CallApp.INSTANCE.onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CallApp.INSTANCE.onTerminate(this);
    }
}
