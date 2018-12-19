package com.gcml.common.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gcml.common.api.BuildOkHttpClient;
import com.gcml.common.http.HttpLogInterceptor;
import com.google.auto.service.AutoService;

import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@AutoService(BuildOkHttpClient.class)
public class BuildOkHttpClientImpl implements BuildOkHttpClient {
    @Override
    public void buildOkHttpClient(Context context, OkHttpClient.Builder builder) {
        RetrofitUrlManager.getInstance().with(builder);
        builder.addNetworkInterceptor(new HttpLogInterceptor(null));
        builder.writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
    }
}
