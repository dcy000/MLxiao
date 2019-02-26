package com.gcml.common.internal;

import android.content.Context;

import com.gcml.common.api.BuildRxCache;
import com.google.auto.service.AutoService;

import io.rx_cache2.internal.RxCache;

@AutoService(BuildRxCache.class)
public class BuildRxCacheImpl implements BuildRxCache {
    @Override
    public void buildRxCache(Context context, RxCache.Builder builder) {
        builder.useExpiredDataIfLoaderNotAvailable(true);
    }
}
