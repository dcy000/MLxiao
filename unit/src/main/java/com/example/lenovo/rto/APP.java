package com.example.lenovo.rto;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 2018/5/2.
 */

public class APP extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
