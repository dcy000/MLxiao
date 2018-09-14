package com.gcml.health.measure.single_measure.no_upload_data;

import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 11:50
 * created by:gzq
 * description:TODO
 */
public class NonUploadSingleMeasureThreeInOneFragment extends ThreeInOne_Fragment {

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 2) {
            if (results[0].equals("bloodsugar")) {
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + results[1]);
            }
            if (results[0].equals("cholesterol")) {
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量胆固醇" + results[1]);
            }

            if (results[0].equals("bua")) {
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量尿酸" + results[1]);
            }
        }
    }
}
