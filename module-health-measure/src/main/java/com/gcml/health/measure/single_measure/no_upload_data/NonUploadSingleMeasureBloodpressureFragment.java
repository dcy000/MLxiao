package com.gcml.health.measure.single_measure.no_upload_data;

import android.annotation.SuppressLint;

import com.gcml.common.utils.UtilsManager;
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
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                    "主人，您本次测量高压" + results[0] + ",低压" + results[1] + ",脉搏" + results[2], false);
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
