package com.gcml.common.internal;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.gcml.common.api.BuildRoomDb;
import com.google.auto.service.AutoService;

@AutoService(BuildRoomDb.class)
public class BuildRoomDbImpl implements BuildRoomDb {
    @Override
    public void buildRoomDb(Context context, String dbClazzName, RoomDatabase.Builder builder) {
        if ("com.gcml.auth.model.UserDb".equals(dbClazzName)) {
            builder.addMigrations(new Migration(1, 2) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("ALTER TABLE UserOld ADD COLUMN waist TEXT");
                }
            });
        }
    }
}
