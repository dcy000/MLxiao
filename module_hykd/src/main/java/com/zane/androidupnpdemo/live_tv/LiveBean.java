package com.zane.androidupnpdemo.live_tv;

import android.os.Parcel;
import android.os.Parcelable;

public class LiveBean implements Parcelable{
    private String tvName;
    private String tvUrl;
    private String tvImgUrl;

    public LiveBean() {
    }

    public LiveBean(String tvName, String tvUrl, String tvImgUrl) {
        this.tvName = tvName;
        this.tvUrl = tvUrl;
        this.tvImgUrl = tvImgUrl;
    }

    protected LiveBean(Parcel in) {
        tvName = in.readString();
        tvUrl = in.readString();
        tvImgUrl = in.readString();
    }

    public static final Creator<LiveBean> CREATOR = new Creator<LiveBean>() {
        @Override
        public LiveBean createFromParcel(Parcel in) {
            return new LiveBean(in);
        }

        @Override
        public LiveBean[] newArray(int size) {
            return new LiveBean[size];
        }
    };

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getTvUrl() {
        return tvUrl;
    }

    public void setTvUrl(String tvUrl) {
        this.tvUrl = tvUrl;
    }

    public String getTvImgUrl() {
        return tvImgUrl;
    }

    public void setTvImgUrl(String tvImgUrl) {
        this.tvImgUrl = tvImgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tvName);
        dest.writeString(tvUrl);
        dest.writeString(tvImgUrl);
    }
}
