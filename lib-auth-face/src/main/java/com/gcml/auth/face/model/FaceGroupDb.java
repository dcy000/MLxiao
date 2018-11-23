package com.gcml.auth.face.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {FaceGroup.class}, version = 1, exportSchema = false)
public abstract class FaceGroupDb extends RoomDatabase {
    public abstract FaceGroupDao faceGroupDao();
}
