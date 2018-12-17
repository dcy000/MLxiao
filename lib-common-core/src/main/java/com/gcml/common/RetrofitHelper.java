package com.gcml.common;

import com.gcml.common.api.BuildRetrofit;
import com.gcml.common.cache.Cache;
import com.gcml.common.cache.CacheType;

import java.util.ServiceLoader;

import retrofit2.Retrofit;

public class RetrofitHelper {

    private volatile Retrofit instance;

    public static Retrofit get() {
        return RetrofitHelper.INSTANCE.instance();
    }

    public static <T> T service(Class<T> service) {
        return RetrofitHelper.INSTANCE.obtainService(service);
    }

    private <T> T obtainService(Class<T> service) {
        if (serviceCache == null) {
            synchronized (this) {
                if (serviceCache == null) {
                    serviceCache = CacheHelper.factory().build(CacheType.CUSTOM_CACHE_TYPE);
                }
            }
        }
        Object obj = serviceCache.get(service.getName());
        if (obj == null) {
            synchronized (this) {
                obj = serviceCache.get(service.getName());
                if (obj == null) {
                    obj = RetrofitHelper.get().create(service);
                    serviceCache.put(service.getName(), obj);
                }
            }
        }
        return (T) obj;
    }

    private volatile Cache<String, Object> serviceCache;

    private Retrofit instance() {
        if (instance == null) {
            synchronized (RetrofitHelper.class) {
                if (instance == null) {
                    Retrofit.Builder builder = new Retrofit.Builder();
                    ServiceLoader<BuildRetrofit> loader = ServiceLoader.load(BuildRetrofit.class);
                    for (BuildRetrofit buildRetrofit : loader) {
                        buildRetrofit.buildRetrofit(AppDelegate.INSTANCE.app(), builder);
                    }
                    instance = builder.build();
                }
            }
        }
        return instance;
    }

    private static RetrofitHelper INSTANCE;

    static {
        INSTANCE = new RetrofitHelper();
    }
}
