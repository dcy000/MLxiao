package com.gcml.health.measure.first_diagnosis.bean;

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
    private Fragment fragment;

    public FirstDiagnosisBean(Fragment fragment,List<DetectionData> cacheDatas) {
        this.cacheDatas = cacheDatas;
        this.fragment = fragment;
    }

    public List<DetectionData> getCacheDatas() {
        return cacheDatas;
    }

    public void setCacheDatas(List<DetectionData> cacheDatas) {
        this.cacheDatas = cacheDatas;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
