package com.gcml.health.assistant.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllTaskList implements Parcelable {
    @SerializedName("schedule")
    private int schedule;
    @SerializedName("userTasklist")
    private List<AllTaskEntity> taskList;

    public AllTaskList() {
    }

    protected AllTaskList(Parcel in) {
        schedule = in.readInt();
    }

    public static final Creator<AllTaskList> CREATOR = new Creator<AllTaskList>() {
        @Override
        public AllTaskList createFromParcel(Parcel in) {
            return new AllTaskList(in);
        }

        @Override
        public AllTaskList[] newArray(int size) {
            return new AllTaskList[size];
        }
    };

    public int getSchedule() {
        return schedule;
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public List<AllTaskEntity> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<AllTaskEntity> taskList) {
        this.taskList = taskList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(schedule);
    }
}
