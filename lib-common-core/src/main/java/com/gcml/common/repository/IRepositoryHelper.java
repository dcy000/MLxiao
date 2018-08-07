package com.gcml.common.repository;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

public interface IRepositoryHelper {

    Context getContext();

    <T> T retrofitService(Class<T> service);

    <T> T rxCacheProvider(Class<T> provider);

    <DB extends RoomDatabase> DB roomDb(Class<DB> database, String dbName);

    void clearAllCache();

}
