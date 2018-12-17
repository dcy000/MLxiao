package com.gcml.common.api;

import android.content.Context;

import retrofit2.Retrofit;

public interface BuildRetrofit {
    void buildRetrofit(Context context, Retrofit.Builder builder);
}