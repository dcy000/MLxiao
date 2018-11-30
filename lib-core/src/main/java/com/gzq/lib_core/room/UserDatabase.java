package com.gzq.lib_core.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gzq.lib_core.bean.UserInfoBean;

@Database(entities = {UserInfoBean.class},version = 1,exportSchema = false)
public abstract class UserDatabase extends RoomDatabase{
    public abstract  UserDao userDao();
}
