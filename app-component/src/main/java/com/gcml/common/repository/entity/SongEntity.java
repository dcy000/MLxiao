package com.gcml.common.repository.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by afirez on 18-2-6.
 */

public class SongEntity {

    @SerializedName("cid")
    private String id;        //歌曲id
    @SerializedName("title")
    private String name;      //歌曲名字
    @SerializedName("wr")
    private String singer;    //歌手
    @SerializedName("content")
    private String url;       //歌曲url地址
    @SerializedName("mid")
    private String sheetId;   //所属歌单id
    @SerializedName("type")
    private int type;         //类型,歌曲为3
    @SerializedName("flag")
    private String flag;         //保留字段

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Song {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", url='" + url + '\'' +
                ", sheetId='" + sheetId + '\'' +
                ", type=" + type +
                ", flag=" + flag +
                '}';
    }
}
