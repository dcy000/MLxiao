package com.ml.bci.game.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.ml.bci.game.common.utils.T;

/**
 * Created by lenovo on 2018/6/12.
 */

public class BciAppLifecycleCallbacks {

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final BciAppLifecycleCallbacks INSTANCE = new BciAppLifecycleCallbacks();
    }

    private BciAppLifecycleCallbacks() {
    }

    public static BciAppLifecycleCallbacks getInstance() {
        return Holder.INSTANCE;
    }

    private Application mApp;

    public void attachBaseContext(Application app, Context base) {
        mApp = app;
    }

    public void onCreate(Application app) {
        T.init(app);
    }

    public void onTerminate(Application app) {

    }
}
