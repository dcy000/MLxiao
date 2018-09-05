package com.gcml.health.measure.single_measure.fragment;

import android.text.TextUtils;

import com.gcml.common.data.UserSpHelper;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 11:09
 * created by:gzq
 * description:单次体重测量
 */
public class SingleMeasureWeightFragment extends Weight_Fragment {
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            //得到身高和体重，再计算一下体质
            if (mTvTizhi!=null){
                String userHeight = UserSpHelper.getUserHeight();
                if (!TextUtils.isEmpty(userHeight)) {
                    float parseFloat = Float.parseFloat(userHeight);
                    float weight = Float.parseFloat(results[0]);
                    if (mTvTizhi != null) {
                        mTvTizhi.setText(String.format("%.2f", weight / (parseFloat * parseFloat / 10000)));
                    }
                }
            }
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量体重" + results[0] + "公斤", false);
            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("3");
            data.setWeight(Float.parseFloat(results[0]));
            datas.add(data);
            HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    ToastUtils.showLong("数据上传成功");
                }

                @Override
                public void onError() {
                    ToastUtils.showLong("数据上传失败");
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
