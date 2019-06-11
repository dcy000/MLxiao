package com.gcml.module_video.video;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoType implements Parcelable {
    // "id": 75,
    // "text": "智能检测",
    // "value": "7",
    // "typeId": 19

    private int id;
    private String text;
    private String value;
    private int typeId;

    public VideoType() {
    }

    protected VideoType(Parcel in) {
        id = in.readInt();
        text = in.readString();
        value = in.readString();
        typeId = in.readInt();
    }

    public static final Creator<VideoType> CREATOR = new Creator<VideoType>() {
        @Override
        public VideoType createFromParcel(Parcel in) {
            return new VideoType(in);
        }

        @Override
        public VideoType[] newArray(int size) {
            return new VideoType[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
        dest.writeString(value);
        dest.writeInt(typeId);
    }
}
