package com.gcml.common.repository.di;

import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.repository.cache.Cache;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

@Singleton
@Component(modules = {
        RepositoryModule.class,
        ClientModule.class,
        RepositoryConfigModule.class
})
public interface RepositoryComponent {
    IRepositoryHelper repositoryHelper();

    OkHttpClient okHttpClient();

    File cacheFile();

    /**
     * 为外部使用提供 Cache,切勿大量存放大容量数据
     *
     * @return Cache
     */
    Cache<String, Object> extras();

    void inject(RepositoryApp RepositoryApp);
}
