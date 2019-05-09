package com.gcml.common;

import com.gcml.common.api.BuildGson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ServiceLoader;

public class GsonHelper {

    private volatile Gson instance;

    public static Gson get() {
        return GsonHelper.INSTANCE.instance();
    }

    private Gson instance() {
        if (instance == null) {
            synchronized (GsonHelper.class) {
                if (instance == null) {
                    GsonBuilder builder = new GsonBuilder();
                    ServiceLoader<BuildGson> loader = ServiceLoader.load(BuildGson.class);
                    for (BuildGson buildGson : loader) {
                        buildGson.buildGson(AppDelegate.INSTANCE.app(), builder);
                    }
                    instance = builder.create();
                }
            }
        }
        return instance;
    }

    private static GsonHelper INSTANCE;

    static {
        INSTANCE = new GsonHelper();
    }
}
