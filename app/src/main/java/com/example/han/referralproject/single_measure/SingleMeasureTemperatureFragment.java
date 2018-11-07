package com.example.han.referralproject.single_measure;

import android.annotation.SuppressLint;

import com.example.han.referralproject.single_measure.bean.DetectionData;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
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
    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量耳温" + results[0] + "摄氏度", false);
            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData temperatureData = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            temperatureData.setDetectionType("4");
            temperatureData.setTemperAture(Float.parseFloat(results[0]));
            datas.add(temperatureData);

            //TODO:=================
//            HealthMeasureRepository.postMeasureData(datas)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
//                    .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
//                        @Override
//                        public void onNext(List<DetectionResult> o) {
//                            ToastUtils.showShort("上传数据成功");
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            ToastUtils.showShort("上传数据失败:" + e.getMessage());
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
