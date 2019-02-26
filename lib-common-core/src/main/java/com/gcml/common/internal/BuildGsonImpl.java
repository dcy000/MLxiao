package com.gcml.common.internal;

import android.content.Context;

import com.gcml.common.api.BuildGson;
import com.google.auto.service.AutoService;
import com.google.gson.GsonBuilder;

@AutoService(BuildGson.class)
public class BuildGsonImpl implements BuildGson {
    @Override
    public void buildGson(Context context, GsonBuilder builder) {
        builder.enableComplexMapKeySerialization();
    }
}
