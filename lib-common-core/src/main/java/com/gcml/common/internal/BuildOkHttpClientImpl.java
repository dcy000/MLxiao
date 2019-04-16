package com.gcml.common.internal;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.gcml.common.api.BuildOkHttpClient;
import com.gcml.common.http.HttpLogInterceptor;
import com.google.auto.service.AutoService;

import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import tech.linjiang.pandora.Pandora;

@AutoService(BuildOkHttpClient.class)
public class BuildOkHttpClientImpl implements BuildOkHttpClient {
    @Override
    public void buildOkHttpClient(Context context, OkHttpClient.Builder builder) {
        RetrofitUrlManager.getInstance().with(builder);

        if ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            // debug
            builder.addNetworkInterceptor(new HttpLogInterceptor(null));
        }
        if (context.getPackageName().equals(getCurProcessName(context))) {
            builder.addInterceptor(Pandora.get().getInterceptor());
        }
        builder.writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
    }

    private String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
