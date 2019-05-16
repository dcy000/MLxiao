package com.gcml.health.measure.single_measure.no_upload_data;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.UM;
import com.gcml.module_blutooth_devices.temperature.TemperatureFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 11:48
 * created by:gzq
 * description:TODO
 */
public class NonUploadSingleMeasureTemperatureFragment extends TemperatureFragment {
    @Override
    protected void onMeasureFinished(DetectionData detectionData) {
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量耳温" + detectionData.getTemperAture() + "摄氏度", false);
    }
}
