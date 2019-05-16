package com.gcml.web.demo;

import android.app.Application;

import com.gcml.web.TbsInitHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TbsInitHelper.initAsync(this);
    }

}
