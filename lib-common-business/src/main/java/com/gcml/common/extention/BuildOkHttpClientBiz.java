package com.gcml.common.extention;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.gcml.common.api.BuildOkHttpClient;
import com.gcml.common.business.BuildConfig;
import com.gcml.common.http.header.CommonHeaderInterceptor;
import com.google.auto.service.AutoService;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;

@AutoService(BuildOkHttpClient.class)
public class BuildOkHttpClientBiz implements BuildOkHttpClient {
    @Override
    public void buildOkHttpClient(Context context, OkHttpClient.Builder builder) {

        builder.addInterceptor(new CommonHeaderInterceptor());

        if ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            // debug
            builder.addNetworkInterceptor(new StethoInterceptor());
            builder.addInterceptor(new LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.WARN)
                    .request("Request")
                    .response("Response")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .enableAndroidStudio_v3_LogsHack(true)
                    .build());
        }
    }
}
