package com.example.han.referralproject.video;


/**
 * Created by lenovo on 2017/11/24.
 */

public class VideoDetailEntity {
    private String url;
    private String title;

    public VideoDetailEntity() {
    }

    public VideoDetailEntity(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
