package com.gzq.lib_core.base.config;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * created on 2018/10/19 17:02
 * created by: gzq
 * description: okhttp配置
 */
public interface OkhttpConfig {
    void okhttp(Context context, OkHttpClient.Builder builder);
}
