package com.gcml.health.assistant.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AbnormalTaskList implements Parcelable {
    @SerializedName("taskId")
    private int taskId;
    @SerializedName("status")
    private String status;
    @SerializedName("taskList")
    private List<AbnormalTaskEntity> taskList;

    protected AbnormalTaskList(Parcel in) {
        taskId = in.readInt();
        status = in.readString();
        taskList = in.createTypedArrayList(AbnormalTaskEntity.CREATOR);
    }

    public static final Creator<AbnormalTaskList> CREATOR = new Creator<AbnormalTaskList>() {
        @Override
        public AbnormalTaskList createFromParcel(Parcel in) {
            return new AbnormalTaskList(in);
        }

        @Override
        public AbnormalTaskList[] newArray(int size) {
            return new AbnormalTaskList[size];
        }
    };

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AbnormalTaskEntity> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<AbnormalTaskEntity> taskList) {
        this.taskList = taskList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskId);
        dest.writeString(status);
        dest.writeTypedList(taskList);
    }
}
