package com.gzq.lib_core.base.config;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * created on 2018/10/19 17:03
 * created by: gzq
 * description: room数据库框架配置
 */
public interface RoomDatabaseConfig<DB extends RoomDatabase> {
    void room(Context context, RoomDatabase.Builder<DB> builder);
}
