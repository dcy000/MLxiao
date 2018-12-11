package com.gcml.common.app.lifecycle;

import android.app.Application;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by afirez on 2018/6/15.
 */

public class App extends Application {

    private static Application mApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        AppDelegate.INSTANCE.attachBaseContext(this, base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication=this;
        AppDelegate.INSTANCE.onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppDelegate.INSTANCE.onTerminate(this);
    }
    public static Application getApp() {
        return mApplication;
    }
    /**
     * 为了解决在fragment中使用{@link android.support.v4.app.Fragment#getString(int)}
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     * @param id 字符串资源id
     * @return
     */
    public static String obtainString(@StringRes int id){
        return getApp().getResources().getString(id);
    }

    /**
     * 为了解决在fragment中使用{@link Fragment#getContext()} getContext().getColor(int color)
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     * @param id 颜色资源id
     * @return
     */
    public static int obtainColor(@ColorRes int id){
        return ContextCompat.getColor(getApp(),id);
    }
}
