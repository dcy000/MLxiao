package com.gcml.common.repository.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gcml.common.repository.entity.SheetEntity;

import java.util.List;

@Dao
public interface SheetDao {

    @Query("SELECT * FROM Sheet")
    List<SheetEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SheetEntity> sheets);

    @Query("DELETE FROM Sheet")
    void deleteAll();
}
