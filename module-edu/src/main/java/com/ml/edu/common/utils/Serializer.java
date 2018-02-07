package com.ml.edu.common.utils;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by afirez on 18-1-31.
 */
@Singleton
public class Serializer {
    private final Gson gson = new Gson();

    private static volatile Serializer sInstance;

    public static Serializer getInstance() {
        if (sInstance == null) {
            synchronized (Serializer.class) {
                if (sInstance == null) {
                    sInstance = new Serializer();
                }
            }
        }
        return sInstance;
    }

    @Inject
    public Serializer() {

    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }

    public <T> T deserialize(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
