package com.gcml.health.measure.single_measure.no_upload_data;

import android.annotation.SuppressLint;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 11:46
 * created by:gzq
 * description:TODO
 */
public class NonUploadSingleMeasureBloodsugarFragment extends Bloodsugar_Fragment {
    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            String roundUp = DataUtils.getRoundUp(results[0], 1);
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + roundUp, false);
        }
    }

}
