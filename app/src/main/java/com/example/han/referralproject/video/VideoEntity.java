package com.example.han.referralproject.video;


/**
 * Created by lenovo on 2017/11/24.
 */

public class VideoEntity {
    private int upid;
    //category
    private String tag1;
    private String videourl;
    private String imageurl;
    private String title;
    private String time;
    private String flag;
    private String tag2;
    private String soart;

    public VideoEntity() {
    }

    public VideoEntity(String videourl, String title) {
        this.videourl = videourl;
        this.title = title;
    }

    public int getUpid() {
        return upid;
    }

    public void setUpid(int upid) {
        this.upid = upid;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getSoart() {
        return soart;
    }

    public void setSoart(String soart) {
        this.soart = soart;
    }
}
