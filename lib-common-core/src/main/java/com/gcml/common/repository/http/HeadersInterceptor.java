package com.gcml.common.repository.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;

import com.gcml.common.app.lifecycle.AppDelegate;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadersInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sp = AppDelegate.INSTANCE.app().getSharedPreferences("ScopeMediaPrefsFile", Context.MODE_PRIVATE);
        String headerserver = sp.getString("header-server", "");
        Request request = chain.request();

        if (!TextUtils.isEmpty(headerserver)) {
            request = request.newBuilder()
                    .addHeader("serverId", headerserver)
                    .build();
        }
        request = request.newBuilder()
                .addHeader("equipmentId", getAndroidId())
                .build();
        return chain.proceed(request);
    }

    /**
     * 获取ANDROID ID
     *
     * @return
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(AppDelegate.INSTANCE.app().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
