package com.ml.bci.game.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 2018/6/12.
 */

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        BciAppLifecircleCallbacks.getInstance().attachBaseContext(this, base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BciAppLifecircleCallbacks.getInstance().onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BciAppLifecircleCallbacks.getInstance().onTerminate(this);
    }
}
