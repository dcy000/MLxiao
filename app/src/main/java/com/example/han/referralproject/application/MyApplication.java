package com.example.han.referralproject.application;

import android.app.Application;
import android.content.Context;

import com.example.han.referralproject.R;
import com.example.han.referralproject.util.LocalShared;
import com.iflytek.cloud.SpeechUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class MyApplication extends Application {
    private static MyApplication mInstance;
    public String userId;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LocalShared mShared = LocalShared.getInstance(this);
        // userId = mShared.getUserId();
        userId = "1223";
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
    }

    public static MyApplication getInstance() {
        return mInstance;
    }
}
