package com.gcml.common;

import com.gcml.common.api.BuildOkHttpClient;

import java.util.ServiceLoader;

import okhttp3.OkHttpClient;

public class OkHttpClientHelper {

    private volatile OkHttpClient instance;

    public static OkHttpClient get() {
        return OkHttpClientHelper.INSTANCE.instance();
    }

    public OkHttpClient instance() {
        if (instance == null) {
            synchronized (OkHttpClientHelper.class) {
                if (instance == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    ServiceLoader<BuildOkHttpClient> loader = ServiceLoader.load(BuildOkHttpClient.class);
                    for (BuildOkHttpClient buildOkHttpClient : loader) {
                        buildOkHttpClient.buildOkHttpClient(AppDelegate.INSTANCE.app(), builder);
                    }
                    instance = builder.build();
                }
            }
        }
        return instance;
    }

    private static OkHttpClientHelper INSTANCE;

    static {
        INSTANCE = new OkHttpClientHelper();
    }
}
