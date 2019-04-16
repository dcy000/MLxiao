package com.gcml.health.measure.single_measure.no_upload_data;

import android.annotation.SuppressLint;

import com.gcml.common.utils.UM;
import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.bloodpressure.BloodpressureFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 10:15
 * created by:gzq
 * description:不需要上传测量数据的单测
 */
public class NonUploadSingleMeasureBloodpressureFragment extends BloodpressureFragment {
    private static final int CODE_REQUEST_GETHYPERTENSIONHAND = 10002;
    private boolean isOnPause=false;


    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3&&!isOnPause) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.this_time_blood_pressure) + results[0] + UM.getString(R.string.voice_low_pressure) + results[1] + UM.getString(R.string.voice_pulse) + results[2], false);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isOnPause=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause=true;
    }
}
