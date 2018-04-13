package com.zane.androidupnpdemo;

import android.app.Application;

import com.gzq.administrator.lib_common.base.BaseApplication;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;

/**
 * Created by gzq on 2018/3/16.
 */

public class LiveMediaApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.getOkgoInstance()
                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .setCacheTime(3600*1000);
    }
}
