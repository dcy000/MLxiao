package com.example.han.referralproject.children.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by afirez on 2018/3/7.
 */

public class SheetModel implements Parcelable {

    @SerializedName("mid")
    private int id;           //歌单id
    @SerializedName("mname")
    private String name;         //歌单名字
    @SerializedName("murl")
    private String imageUrl;     //歌单缩略图
    @SerializedName("mcontent")
    private String content;      //歌单详情介绍
    @SerializedName("flag")
    private String flag;            //保留字段

    private String category;

    public SheetModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "SheetModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", content='" + content + '\'' +
                ", flag='" + flag + '\'' +
                ", category='" + category + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeString(this.content);
        dest.writeString(this.flag);
        dest.writeString(this.category);
    }

    protected SheetModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.content = in.readString();
        this.flag = in.readString();
        this.category = in.readString();
    }

    public static final Parcelable.Creator<SheetModel> CREATOR = new Parcelable.Creator<SheetModel>() {
        @Override
        public SheetModel createFromParcel(Parcel source) {
            return new SheetModel(source);
        }

        @Override
        public SheetModel[] newArray(int size) {
            return new SheetModel[size];
        }
    };
}
