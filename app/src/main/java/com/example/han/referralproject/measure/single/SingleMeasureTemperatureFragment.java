package com.example.han.referralproject.measure.single;

import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 10:47
 * created by:gzq
 * description:单次耳温测量
 */
public class SingleMeasureTemperatureFragment extends Temperature_Fragment {
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            MLVoiceSynthetize.startSynthesize(getContext(),"主人，您本次测量耳温"+results[0]+"摄氏度",false);
            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData temperatureData = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            temperatureData.setDetectionType("4");
            temperatureData.setTemperAture(Float.parseFloat(results[0]));
            datas.add(temperatureData);
            NetworkApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    ToastUtils.showShort("上传数据成功");
                }

                @Override
                public void onError() {
                    ToastUtils.showShort("上传数据失败");
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MLVoiceSynthetize.stop();
    }
}
