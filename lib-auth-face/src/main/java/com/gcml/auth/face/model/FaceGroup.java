package com.gcml.auth.face.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "FaceGroup")
public class FaceGroup {

    @NonNull
    @SerializedName("groupId")
    @ColumnInfo(name = "groupId")
    @PrimaryKey
    public String groupId = "";

    @SerializedName("num")
    @ColumnInfo(name = "faceCount")
    public String faceCount;
}
