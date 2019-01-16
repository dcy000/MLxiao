package com.gcml.module_inquiry.model;

import android.net.Uri;

public class SurveyBean {
    private String fragmentTag;
    private Uri videoUri;
    private String videoTitle;

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public SurveyBean(String fragmentTag, Uri videoUri, String videoTitle) {
        this.fragmentTag = fragmentTag;
        this.videoUri = videoUri;
        this.videoTitle = videoTitle;
    }
}
