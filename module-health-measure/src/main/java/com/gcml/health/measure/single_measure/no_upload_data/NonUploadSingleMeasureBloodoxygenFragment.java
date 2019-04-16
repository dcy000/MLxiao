package com.gcml.health.measure.single_measure.no_upload_data;

import com.gcml.common.utils.UM;
import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.bloodoxygen.BloodOxygenFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 11:43
 * created by:gzq
 * description:TODO
 */
public class NonUploadSingleMeasureBloodoxygenFragment extends BloodOxygenFragment {
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 2) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(),
                    UM.getString(R.string.this_time_blood_oxygen) + results[0] + "%", false);
        }
    }
}
