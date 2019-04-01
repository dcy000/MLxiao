package com.gcml.health.measure.single_measure;

import com.gcml.health.measure.single_measure.bean.DetectTimesInfoBean;

/**
 * Created by lenovo on 2019/4/1.
 */

public interface DedectInfoListener {
    void onDetectInfoChange(DetectTimesInfoBean newInfo);
}
