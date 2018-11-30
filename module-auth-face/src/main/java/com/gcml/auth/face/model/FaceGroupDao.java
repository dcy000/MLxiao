package com.gcml.auth.face.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FaceGroupDao {
    @Query("SELECT * FROM FaceGroup")
    Flowable<List<FaceGroup>> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAll(List<FaceGroup> faceGroups);

    @Query("DELETE FROM FaceGroup")
    void deleteAll();
}
