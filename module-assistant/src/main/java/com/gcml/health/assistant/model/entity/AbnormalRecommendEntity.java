package com.gcml.health.assistant.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AbnormalRecommendEntity implements Parcelable {

    /**
     * adviceId : 117
     * adviceMsg : 54
     * state : 1
     * count : 0
     */

    @SerializedName("adviceId")
    private int adviceId;
    @SerializedName("tag")
    private String tag;
    @SerializedName("adviceMsg")
    private String adviceMsg;
    @SerializedName("adviceName")
    private String adviceName;
    @SerializedName("state")
    private String state;
    @SerializedName("count")
    private int count;

    public AbnormalRecommendEntity() {
    }

    protected AbnormalRecommendEntity(Parcel in) {
        adviceId = in.readInt();
        tag = in.readString();
        adviceMsg = in.readString();
        adviceName = in.readString();
        state = in.readString();
        count = in.readInt();
    }

    public static final Creator<AbnormalRecommendEntity> CREATOR = new Creator<AbnormalRecommendEntity>() {
        @Override
        public AbnormalRecommendEntity createFromParcel(Parcel in) {
            return new AbnormalRecommendEntity(in);
        }

        @Override
        public AbnormalRecommendEntity[] newArray(int size) {
            return new AbnormalRecommendEntity[size];
        }
    };

    public int getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(int adviceId) {
        this.adviceId = adviceId;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAdviceMsg() {
        return adviceMsg;
    }

    public void setAdviceMsg(String adviceMsg) {
        this.adviceMsg = adviceMsg;
    }

    public String getAdviceName() {
        return adviceName;
    }

    public void setAdviceName(String adviceName) {
        this.adviceName = adviceName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adviceId);
        dest.writeString(tag);
        dest.writeString(adviceMsg);
        dest.writeString(adviceName);
        dest.writeString(state);
        dest.writeInt(count);
    }

    public String getTag() {
        return tag;
    }

    public String getCountDesc() {
        return count + "人执行";
    }
}
