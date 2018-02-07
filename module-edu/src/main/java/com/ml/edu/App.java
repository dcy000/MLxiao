package com.ml.edu;

import android.app.Application;

import com.example.han.referralproject.new_music.HttpInterceptor;
import com.example.han.referralproject.new_music.LibMusicPlayer;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import okhttp3.OkHttpClient;

/**
 * Created by afirez on 18-2-2.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        LibMusicPlayer.init(this);
    }
}
