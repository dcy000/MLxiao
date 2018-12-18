package com.gcml.common.api;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


public interface BuildRoomDb {
    void buildRoomDb(Context context, String dbClazzName, RoomDatabase.Builder builder);
}
