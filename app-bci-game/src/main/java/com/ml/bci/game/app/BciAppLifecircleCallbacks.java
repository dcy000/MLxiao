package com.ml.bci.game.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 2018/6/12.
 */

public class BciAppLifecircleCallbacks {

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final BciAppLifecircleCallbacks INSTANCE = new BciAppLifecircleCallbacks();
    }

    private BciAppLifecircleCallbacks() {
    }

    public static BciAppLifecircleCallbacks getInstance() {
        return Holder.INSTANCE;
    }

    private Application mApp;

    public void attachBaseContext(Application app, Context base) {
        mApp = app;
    }

    public void onCreate(Application app) {
//        T.init(app);
    }

    public void onTerminate(Application app) {

    }
}
