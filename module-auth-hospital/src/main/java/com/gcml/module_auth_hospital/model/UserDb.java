package com.gcml.module_auth_hospital.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gcml.common.data.UserEntity;

@Database(entities = {UserEntity.class}, version = 2, exportSchema = false)
public abstract class UserDb extends RoomDatabase {
    public abstract UserDao userDao();
}
