package com.gcml.common.repository.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gcml.common.repository.entity.SheetEntity;


@Database(entities = {SheetEntity.class}, version = 1,exportSchema = false)
public abstract class SheetDb extends RoomDatabase {
    public abstract SheetDao sheetDao();
}
