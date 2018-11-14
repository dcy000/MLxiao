package com.gzq.lib_core.base.config;

import android.content.Context;

import retrofit2.Retrofit;

/**
 * created on 2018/10/19 17:02
 * created by: gzq
 * description: retrofit配置
 */
public interface RetrofitConfig {
    void retrofit(Context context, Retrofit.Builder builder);
}
