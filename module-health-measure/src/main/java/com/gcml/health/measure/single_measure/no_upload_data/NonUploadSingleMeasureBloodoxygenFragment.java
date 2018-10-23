package com.gcml.health.measure.single_measure.no_upload_data;

import com.gcml.common.utils.UtilsManager;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 11:43
 * created by:gzq
 * description:TODO
 */
public class NonUploadSingleMeasureBloodoxygenFragment extends Bloodoxygen_Fragment {
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 2) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                    "主人，您本次测量血氧" + results[0] + "%", false);
        }
    }
}
