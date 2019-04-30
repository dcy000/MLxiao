package com.gcml.module_factory_test.bean;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by afirez on 18-1-31.
 */
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

    public Serializer() {

    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }

    public <T> T deserialize(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
