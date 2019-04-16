package com.gcml.health.measure.single_measure.no_upload_data;

import com.gcml.common.utils.UM;
import com.gcml.health.measure.R;
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
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.this_time_temperature)+ results[0] + UM.getString(R.string.celsius), false);
        }
    }
}
