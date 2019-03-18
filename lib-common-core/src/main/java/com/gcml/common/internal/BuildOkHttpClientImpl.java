package com.gcml.common.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.gcml.common.BuildConfig;
import com.gcml.common.api.BuildOkHttpClient;
import com.gcml.common.http.HttpLogInterceptor;
import com.google.auto.service.AutoService;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;

@AutoService(BuildOkHttpClient.class)
public class BuildOkHttpClientImpl implements BuildOkHttpClient {
    @Override
    public void buildOkHttpClient(Context context, OkHttpClient.Builder builder) {
        RetrofitUrlManager.getInstance().with(builder);

        if ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            // debug
            builder.addNetworkInterceptor(new HttpLogInterceptor(null));
            builder.addInterceptor(getLoggingInterceptor());
        }
        builder.writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
    }

    public LoggingInterceptor getLoggingInterceptor() {
        return new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.WARN)
                .request("Request")
                .response("Response")
                .enableAndroidStudio_v3_LogsHack(true)
                .build();
    }
}
