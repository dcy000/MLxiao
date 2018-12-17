package com.gcml.common.api;

import android.content.Context;

import okhttp3.OkHttpClient;

public interface BuildOkHttpClient {
    void buildOkHttpClient(Context context, OkHttpClient.Builder builder);
}