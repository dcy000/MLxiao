package com.gcml.auth.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gcml.common.data.UserEntity;

@Database(entities = {UserEntity.class}, version = 1, exportSchema = false)
public abstract class UserDb extends RoomDatabase {
    public abstract UserDao userDao();
}
