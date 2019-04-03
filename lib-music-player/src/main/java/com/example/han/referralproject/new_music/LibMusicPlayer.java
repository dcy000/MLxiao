package com.example.han.referralproject.new_music;

import android.content.Context;

import com.gcml.common.OkHttpClientHelper;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by afirez on 18-2-7.
 */

public class LibMusicPlayer {

    public static void init(Context context) {
        Preferences.init(context);
        initOkHttpUtils();
    }

    private static void initOkHttpUtils() {

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .addInterceptor(new HttpInterceptor())
//                .build();
        OkHttpClient okHttpClient = OkHttpClientHelper.get();
        okHttpClient.newBuilder().addInterceptor(new HttpInterceptor());
        OkHttpUtils.initClient(okHttpClient);
    }
}
