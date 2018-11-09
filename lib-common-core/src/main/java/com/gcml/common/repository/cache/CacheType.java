package com.gcml.common.repository.cache;

public enum CacheType {
    /**
     * RepositoryHelper 的 RetrofitService 容器类型
     */
    RETROFIT_SERVICE_CACHE_TYPE,

    /**
     * RepositoryHelper 的 CacheService 容器类型
     */
    CACHE_SERVICE_CACHE_TYPE,

    /**
     * RepositoryHelper 的 RoomDatabase 容器类型
     */
    ROOM_DATABASE_CACHE_TYPE,

    /**
     * 外部使用类型
     */
    EXTRAS_CACHE_TYPE,

    /**
     * 自定义缓存类型，方便扩展
     */
    CUSTOM_CACHE_TYPE
}
