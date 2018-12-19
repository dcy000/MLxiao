package com.gcml.common;

import android.support.annotation.NonNull;

import com.gcml.common.cache.Cache;
import com.gcml.common.cache.CacheType;
import com.gcml.common.cache.LruCache;

public class CacheHelper {

    private volatile Cache<String, Object> cache;

    public static Cache<String, Object> get() {
        return CacheHelper.INSTANCE.cacheInstance();
    }

    private Cache<String, Object> cacheInstance() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = CacheHelper.factory().build(CacheType.EXTRAS_CACHE_TYPE);
                }
            }
        }
        return cache;
    }

    private volatile Cache.Factory instance;

    public static Cache.Factory factory() {
        return CacheHelper.INSTANCE.factoryInstance();
    }

    private Cache.Factory factoryInstance() {
        if (instance == null) {
            synchronized (CacheHelper.class) {
                if (instance == null) {
                    instance = new Cache.Factory() {
                        @NonNull
                        @Override
                        public Cache build(CacheType type) {
                            switch (type) {
                                case EXTRAS_CACHE_TYPE:
                                    //外部 extras 默认最多只能缓存500个内容
                                    return new LruCache(500);
                                default:
                                    // 默认缓存 100 个内容
                                    return new LruCache(Cache.Factory.DEFAULT_CACHE_SIZE);
                            }
                        }
                    };
                }
            }
        }
        return instance;
    }

    public static CacheHelper INSTANCE;

    static {
        INSTANCE = new CacheHelper();
    }
}
