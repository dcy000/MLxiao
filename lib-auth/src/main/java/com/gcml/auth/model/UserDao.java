package com.gcml.auth.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gcml.common.data.UserEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UserDao {
    @Query("SELECT * FROM UserOld")
    Single<List<UserEntity>> findAll();

    @Query("SELECT * FROM UserOld WHERE id = :userId")
    Single<UserEntity> findOneById(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAll(UserEntity... user);

    @Query("DELETE FROM UserOld")
    void deleteAll();

}
