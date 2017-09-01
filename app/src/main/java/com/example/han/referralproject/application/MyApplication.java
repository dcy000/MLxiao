package com.example.han.referralproject.application;

import android.app.Application;
import android.content.Context;

import com.example.han.referralproject.R;
import com.iflytek.cloud.SpeechUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class MyApplication extends Application {
//

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        super.onCreate();

    }





}
