package com.zane.androidupnpdemo.live_tv;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by gzq on 2018/3/16.
 */

public class LiveMediaApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
    }
}
