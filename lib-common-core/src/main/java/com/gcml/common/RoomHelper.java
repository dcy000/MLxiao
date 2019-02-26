package com.gcml.common;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.gcml.common.api.BuildRoomDb;
import com.gcml.common.cache.Cache;
import com.gcml.common.cache.CacheType;

import java.util.ServiceLoader;

public class RoomHelper {

    public static <DB extends RoomDatabase> DB db(Class<DB> database, String dbName) {
        return RoomHelper.INSTANCE.obtainDb(database, dbName);
    }

    private <DB extends RoomDatabase> DB obtainDb(Class<DB> database, String dbName) {
        if (dbCache == null) {
            synchronized (this) {
                if (dbCache == null) {
                    dbCache = CacheHelper.factory().build(CacheType.CUSTOM_CACHE_TYPE);
                }
            }
        }
        Object obj = dbCache.get(database.getName());
        if (obj == null) {
            synchronized (this) {
                obj = dbCache.get(database.getName());
                if (obj == null) {
                    RoomDatabase.Builder builder = Room.databaseBuilder(AppDelegate.INSTANCE.app(), database, dbName);
                    ServiceLoader<BuildRoomDb> loader = ServiceLoader.load(BuildRoomDb.class);
                    for (BuildRoomDb buildRoomDb : loader) {
                        buildRoomDb.buildRoomDb(AppDelegate.INSTANCE.app(), database.getName(), builder);
                    }
                    obj = builder.build();
                    dbCache.put(database.getName(), obj);
                }
            }
        }
        return (DB) obj;
    }

    private volatile Cache<String, Object> dbCache;

    private static RoomHelper INSTANCE;

    static {
        INSTANCE = new RoomHelper();
    }
}
