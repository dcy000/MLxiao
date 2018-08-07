package com.gcml.common.repository.di;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gcml.common.repository.http.HttpLogInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ClientModule {
    private static final int TIME_OUT = 10;

    private Application mApplication;

    public ClientModule(Application application) {
        mApplication = application;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(
            Retrofit.Builder builder,
            HttpUrl baseUrl,
            OkHttpClient client,
            Gson gson,
            @Nullable RetrofitConfiguration configuration) {
        builder.baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        if (configuration != null) {
            configuration.configRetrofit(mApplication, builder);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(
            OkHttpClient.Builder builder,
            Interceptor interceptor,
            @Nullable HttpInterceptor httpInterceptor,
            @Nullable List<Interceptor> interceptors,
            @Nullable OkHttpConfiguration configuration) {
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor);
        if (httpInterceptor != null) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(httpInterceptor.onHttpRequestBefore(chain, chain.request()));
                }
            });
        }
        if (interceptors != null) {
            for (Interceptor interceptor1 : interceptors) {
                builder.addInterceptor(interceptor1);
            }
        }
        if (configuration != null) {
            configuration.configOkHttp(mApplication, builder);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    Gson provideGson(@Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null) {
            configuration.configGson(mApplication, builder);
        }
        return builder.create();
    }

    @Singleton
    @Provides
    RxCache provideRxCache(
            @Nullable RxCacheConfiguration configuration,
            @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();
        RxCache rxCache = null;
        if (configuration != null) {
            rxCache = configuration.configRxCache(mApplication, builder);
        }
        if (rxCache != null) {
            return rxCache;
        }
        return builder.persistence(cacheDirectory, new GsonSpeaker());
    }

    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        if (!cacheDirectory.exists()) {
            cacheDirectory.mkdirs();
        }
        return cacheDirectory;
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }

    /**
     * 打印请求信息的拦截器
     *
     * @param interceptor
     * @return
     */
    @Singleton
    @Provides
    Interceptor provideInterceptor(HttpLogInterceptor interceptor) {
        return interceptor;
    }

    public interface RetrofitConfiguration {
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    public interface OkHttpConfiguration {
        void configOkHttp(Context context, OkHttpClient.Builder builder);
    }


    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }

    public interface RxCacheConfiguration {
        RxCache configRxCache(Context context, RxCache.Builder builder);
    }

    public interface HttpInterceptor {
        Request onHttpRequestBefore(Interceptor.Chain chain, Request request);

        Response onHttpResponse(
                String httpResult, Interceptor.Chain chain, Response response);

        HttpInterceptor EMPTY = new HttpInterceptor() {
            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                return request;
            }

            @Override
            public Response onHttpResponse(String httpResult, Interceptor.Chain chain, Response response) {
                return response;
            }
        };
    }
}
