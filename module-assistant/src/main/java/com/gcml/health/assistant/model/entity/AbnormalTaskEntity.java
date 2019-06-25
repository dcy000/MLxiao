package com.gcml.health.assistant.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AbnormalTaskEntity implements Parcelable {

    /**
     * seq : 0
     * createdBy : admin
     * createdOn : 1560912940000
     * modifiedBy : admin
     * modifiedOn : 1560915906000
     * deletionState : 0
     * description :
     * sysTypeItemTaskId : 8cce6e523ae345718bde435ca923b2f6
     * sysTypeItmeId : 126
     * taskName : 54
     * taskTime : 2019-06-26
     */

    @SerializedName("deletionState")
    private String deletionState;
    @SerializedName("description")
    private String description;
    @SerializedName("sysTypeItemTaskId")
    private String sysTypeItemTaskId;
    @SerializedName("sysTypeItmeId")
    private int sysTypeItmeId;
    @SerializedName("taskName")
    private String taskName;
    @SerializedName("taskTime")
    private String taskTime;

    protected AbnormalTaskEntity(Parcel in) {
        deletionState = in.readString();
        description = in.readString();
        sysTypeItemTaskId = in.readString();
        sysTypeItmeId = in.readInt();
        taskName = in.readString();
        taskTime = in.readString();
    }

    public static final Creator<AbnormalTaskEntity> CREATOR = new Creator<AbnormalTaskEntity>() {
        @Override
        public AbnormalTaskEntity createFromParcel(Parcel in) {
            return new AbnormalTaskEntity(in);
        }

        @Override
        public AbnormalTaskEntity[] newArray(int size) {
            return new AbnormalTaskEntity[size];
        }
    };

    public String getDeletionState() {
        return deletionState;
    }

    public void setDeletionState(String deletionState) {
        this.deletionState = deletionState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSysTypeItemTaskId() {
        return sysTypeItemTaskId;
    }

    public void setSysTypeItemTaskId(String sysTypeItemTaskId) {
        this.sysTypeItemTaskId = sysTypeItemTaskId;
    }

    public int getSysTypeItmeId() {
        return sysTypeItmeId;
    }

    public void setSysTypeItmeId(int sysTypeItmeId) {
        this.sysTypeItmeId = sysTypeItmeId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deletionState);
        dest.writeString(description);
        dest.writeString(sysTypeItemTaskId);
        dest.writeInt(sysTypeItmeId);
        dest.writeString(taskName);
        dest.writeString(taskTime);
    }
}
