package com.ml.edu.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by afirez on 18-2-6.
 */

public class SheetEntity {

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

    public void setImageUrl(String url) {
        this.imageUrl = url;
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
        return "SheetEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", content='" + content + '\'' +
                ", flag=" + flag +
                ", category='" + category + '\'' +
                '}';
    }
}
