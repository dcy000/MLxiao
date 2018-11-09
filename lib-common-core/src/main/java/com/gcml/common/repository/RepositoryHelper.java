package com.gcml.common.repository;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.gcml.common.repository.cache.Cache;
import com.gcml.common.repository.cache.CacheType;
import com.gcml.common.repository.di.DbModule;
import com.gcml.common.repository.utils.Preconditions;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

@Singleton
public class RepositoryHelper implements IRepositoryHelper {
    private Application mApplication;
    private Lazy<Retrofit> mRetrofit;
    private Lazy<RxCache> mRxCache;
    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mRxCacheProviderCache;
    private Cache<String, Object> mRoomDbCache;
    private final Cache.Factory mCacheFactory;
    private DbModule.RoomConfiguration mRoomConfiguration;

    @Inject
    public RepositoryHelper(
            Application application,
            Lazy<Retrofit> retrofit,
            Lazy<RxCache> rxCache,
            Cache.Factory cacheFactory,
            DbModule.RoomConfiguration roomConfiguration) {
        this.mApplication = application;
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
        this.mCacheFactory = cacheFactory;
        this.mRoomConfiguration = roomConfiguration;
    }

    @Override
    public Context getContext() {
        return mApplication;
    }

    @Override
    public <T> T retrofitService(Class<T> service) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCacheFactory.build(CacheType.RETROFIT_SERVICE_CACHE_TYPE);
        }
        Preconditions.checkNotNull(mRetrofitServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService;
        synchronized (mRetrofitServiceCache) {
            retrofitService = (T) mRetrofitServiceCache.get(service.getName());
            if (retrofitService == null) {
                retrofitService = mRetrofit.get().create(service);
                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }

    @Override
    public <T> T rxCacheProvider(Class<T> provider) {
        if (mRxCacheProviderCache == null) {
            mRxCacheProviderCache = mCacheFactory.build(CacheType.CACHE_SERVICE_CACHE_TYPE);
        }
        Preconditions.checkNotNull(mRxCacheProviderCache, "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService;
        synchronized (mRxCacheProviderCache) {
            cacheService = (T) mRxCacheProviderCache.get(provider.getName());
            if (cacheService == null) {
                cacheService = mRxCache.get().using(provider);
                mRxCacheProviderCache.put(provider.getName(), cacheService);
            }
        }
        return cacheService;
    }

    @Override
    public <DB extends RoomDatabase> DB roomDb(Class<DB> database, String dbName) {
        if (mRoomDbCache == null) {
            mRoomDbCache = mCacheFactory.build(CacheType.ROOM_DATABASE_CACHE_TYPE);
        }
        Preconditions.checkNotNull(mRoomDbCache, "Cannot return null from a Cache.Factory#build(int) method");
        DB roomDatabase;
        synchronized (mRoomDbCache) {
            roomDatabase = (DB) mRoomDbCache.get(database.getName());
            if (roomDatabase == null) {
                RoomDatabase.Builder builder = Room.databaseBuilder(mApplication, database, dbName);
                //自定义 Room 配置
                if (mRoomConfiguration != null) {
                    mRoomConfiguration.configRoom(mApplication, dbName, builder);
                }
                roomDatabase = (DB) builder.build();
                mRoomDbCache.put(database.getName(), roomDatabase);
            }
        }
        return roomDatabase;
    }

    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }
}
