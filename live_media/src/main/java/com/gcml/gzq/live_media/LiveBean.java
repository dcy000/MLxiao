package com.gcml.gzq.live_media;

import java.io.Serializable;
import java.util.List;

public class LiveBean {
    private String tvName;
    private String tvUrl;

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

    public LiveBean() {

    }

    public LiveBean(String tvName, String tvUrl) {
        this.tvName = tvName;
        this.tvUrl = tvUrl;
    }
}
