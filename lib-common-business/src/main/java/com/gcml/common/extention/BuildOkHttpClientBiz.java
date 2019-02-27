package com.gcml.common.extention;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gcml.common.api.BuildOkHttpClient;
import com.google.auto.service.AutoService;

import okhttp3.OkHttpClient;

@AutoService(BuildOkHttpClient.class)
public class BuildOkHttpClientBiz implements BuildOkHttpClient {
    @Override
    public void buildOkHttpClient(Context context, OkHttpClient.Builder builder) {

        builder.addInterceptor(new CommonHeaderInterceptor());

        if ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            // debug
            builder.addNetworkInterceptor(new StethoInterceptor());

        }
    }
}
