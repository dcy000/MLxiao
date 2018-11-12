package com.example.han.referralproject.yiyuan.newdetect;

import android.net.Uri;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 13:47
 * created by:gzq
 * description:TODO
 */
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
