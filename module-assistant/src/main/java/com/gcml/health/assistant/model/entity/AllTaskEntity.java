package com.gcml.health.assistant.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AllTaskEntity implements Parcelable {


    /**
     * userTasklogId : 1
     * taskName : 54
     * taskTime : 2019-06-26
     * status : 0 : 确认 1： 已完成 2： 未开启
     */

    @SerializedName("userTasklogId")
    private int userTasklogId;
    @SerializedName("taskName")
    private String taskName;
    @SerializedName("taskTime")
    private String taskTime;
    @SerializedName("status")
    private String status;

    public AllTaskEntity() {
    }

    protected AllTaskEntity(Parcel in) {
        userTasklogId = in.readInt();
        taskName = in.readString();
        taskTime = in.readString();
        status = in.readString();
    }

    public static final Creator<AllTaskEntity> CREATOR = new Creator<AllTaskEntity>() {
        @Override
        public AllTaskEntity createFromParcel(Parcel in) {
            return new AllTaskEntity(in);
        }

        @Override
        public AllTaskEntity[] newArray(int size) {
            return new AllTaskEntity[size];
        }
    };

    public int getUserTasklogId() {
        return userTasklogId;
    }

    public void setUserTasklogId(int userTasklogId) {
        this.userTasklogId = userTasklogId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userTasklogId);
        dest.writeString(taskName);
        dest.writeString(taskTime);
        dest.writeString(status);
    }
}
