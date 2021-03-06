package com.gcml.health.measure.single_measure.no_upload_data;

import com.gcml.lib_utils.UtilsManager;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 11:48
 * created by:gzq
 * description:TODO
 */
public class NonUploadSingleMeasureTemperatureFragment extends Temperature_Fragment {
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量耳温" + results[0] + "摄氏度", false);
        }
    }
}
