package com.gcml.module_factory_test.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoBean implements Parcelable {

    private String displayName;
    private String cover;
    private String path;

    public VideoBean() {
    }

    public VideoBean(String displayName, String cover, String path) {
        this.displayName = displayName;
        this.cover = cover;
        this.path = path;
    }

    protected VideoBean(Parcel in) {
        displayName = in.readString();
        cover = in.readString();
        path = in.readString();
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(cover);
        dest.writeString(path);
    }
}
