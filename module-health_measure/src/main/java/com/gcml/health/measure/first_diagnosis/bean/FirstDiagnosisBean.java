package com.gcml.health.measure.first_diagnosis.bean;

import android.net.Uri;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 13:47
 * created by:gzq
 * description:TODO
 */
public class FirstDiagnosisBean {
    private List<DetectionData> cacheDatas;
    private String fragmentTag;
    private Uri videoUri;
    private String videoTitle;

    public List<DetectionData> getCacheDatas() {
        return cacheDatas;
    }

    public void setCacheDatas(List<DetectionData> cacheDatas) {
        this.cacheDatas = cacheDatas;
    }

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

    public FirstDiagnosisBean(List<DetectionData> cacheDatas, String fragmentTag, Uri videoUri, String videoTitle) {
        this.cacheDatas = cacheDatas;
        this.fragmentTag = fragmentTag;
        this.videoUri = videoUri;
        this.videoTitle = videoTitle;
    }
}
