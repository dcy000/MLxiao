package com.gcml.common.repository.di;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gcml.common.repository.cache.Cache;
import com.gcml.common.repository.cache.CacheType;
import com.gcml.common.repository.cache.LruCache;
import com.gcml.common.repository.http.BaseUrl;
import com.gcml.common.repository.http.HttpLogInterceptor;
import com.gcml.common.repository.utils.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

@Module
public class RepositoryConfigModule {
    private Application mApplication;
    private HttpUrl mApiUrl;
    private BaseUrl mBaseUrl;
    private File mCacheFile;
    private ClientModule.HttpInterceptor mHttpInterceptor;
    private List<Interceptor> mInterceptors;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkHttpConfiguration mOkHttpConfiguration;
    private ClientModule.GsonConfiguration mGsonConfiguration;
    private ClientModule.RxCacheConfiguration mRxCacheConfiguration;
    private HttpLogInterceptor.Level mHttpLogLevel;
    private DbModule.RoomConfiguration mRoomConfiguration;
    private Cache.Factory mCacheFactory;

    private RepositoryConfigModule(Builder builder) {
        this.mApplication = builder.mApplication;
        this.mApiUrl = builder.mApiUrl;
        this.mBaseUrl = builder.mBaseUrl;
        this.mHttpInterceptor = builder.mHttpInterceptor;
        this.mCacheFile = builder.mCacheFile;
        this.mInterceptors = builder.mInterceptors;
        this.mRetrofitConfiguration = builder.mRetrofitConfiguration;
        this.mOkHttpConfiguration = builder.mOkHttpConfiguration;
        this.mGsonConfiguration = builder.mGsonConfiguration;
        this.mRxCacheConfiguration = builder.mRxCacheConfiguration;
        this.mHttpLogLevel = builder.mHttpLogLevel;
        this.mRoomConfiguration = builder.mRoomConfiguration;
        this.mCacheFactory = builder.mCacheFactory;
    }

    public static Builder builder() {
        return new Builder();
    }


    @Singleton
    @Provides
    @Nullable
    List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }


    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        if (mBaseUrl != null) {
            HttpUrl httpUrl = mBaseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        return mApiUrl;
    }

    @Singleton
    @Provides
    File provideCacheDir() {
        return mCacheFile == null ? FileHelper.getCacheDirectory(mApplication, "") : mCacheFile;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.HttpInterceptor provideHttpInterceptor() {
        return mHttpInterceptor;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkHttpConfiguration provideOkHttpConfiguration() {
        return mOkHttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    HttpLogInterceptor.Level providePrintHttpLogLevel() {
        return mHttpLogLevel;
    }

    @Singleton
    @Provides
    DbModule.RoomConfiguration provideRoomConfiguration() {
        return mRoomConfiguration == null ? DbModule.RoomConfiguration.EMPTY : mRoomConfiguration;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory() {
        return mCacheFactory == null ? new Cache.Factory() {
            @NonNull
            @Override
            public Cache build(CacheType type) {
                //若想自定义 LruCache 的 size,或者不想使用 LruCache ,想使用自己自定义的策略
                //请使用 RepositoryConfigModule.Builder#cacheFactory() 扩展
                switch (type) {
                    case EXTRAS_CACHE_TYPE:
                        //外部 extras 默认最多只能缓存500个内容
                        return new LruCache(500);
                    default:
                        //RepositoryManager 中的容器默认缓存 100 个内容
                        return new LruCache(Cache.Factory.DEFAULT_CACHE_SIZE);
                }
            }
        } : mCacheFactory;
    }

    public static final class Builder {
        private Application mApplication;
        private HttpUrl mApiUrl;
        private BaseUrl mBaseUrl;
        private File mCacheFile;
        private ClientModule.HttpInterceptor mHttpInterceptor;
        private List<Interceptor> mInterceptors;
        private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
        private ClientModule.OkHttpConfiguration mOkHttpConfiguration;
        private ClientModule.GsonConfiguration mGsonConfiguration;
        private ClientModule.RxCacheConfiguration mRxCacheConfiguration;
        private HttpLogInterceptor.Level mHttpLogLevel;
        private DbModule.RoomConfiguration mRoomConfiguration;
        private Cache.Factory mCacheFactory;

        private Builder() {
        }

        @NonNull
        public Builder application(Application application) {
            this.mApplication = application;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("BaseUrl can not be empty");
            }
            this.mApiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseUrl(BaseUrl baseUrl) {
            if (baseUrl == null) {
                throw new IllegalArgumentException("BaseUrl can not be null");
            }
            this.mBaseUrl = baseUrl;
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.mCacheFile = cacheFile;
            return this;
        }

        public Builder httpInterceptor(ClientModule.HttpInterceptor interceptor) {
            this.mHttpInterceptor = interceptor;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            if (mInterceptors == null)
                mInterceptors = new ArrayList<>();
            this.mInterceptors.add(interceptor);
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.mRetrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(ClientModule.OkHttpConfiguration okHttpConfiguration) {
            this.mOkHttpConfiguration = okHttpConfiguration;
            return this;
        }

        public Builder gsonConfiguration(ClientModule.GsonConfiguration gsonConfiguration) {
            this.mGsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder rxCacheConfiguration(ClientModule.RxCacheConfiguration rxCacheConfiguration) {
            this.mRxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder printHttpLogLevel(HttpLogInterceptor.Level httpLogLevel) {
            if (httpLogLevel == null)
                throw new IllegalArgumentException("httpLogLevel == null. Use HttpLogInterceptor.Level.NONE instead.");
            this.mHttpLogLevel = httpLogLevel;
            return this;
        }

        public Builder roomConfiguration(DbModule.RoomConfiguration roomConfiguration) {
            this.mRoomConfiguration = roomConfiguration;
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.mCacheFactory = cacheFactory;
            return this;
        }

        public RepositoryConfigModule build() {
            return new RepositoryConfigModule(this);
        }
    }

    public interface Configuration {
        void configRepository(Context context, Builder builder);
    }
}
