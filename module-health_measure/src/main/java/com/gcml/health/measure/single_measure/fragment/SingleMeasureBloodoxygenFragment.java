package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;

import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 10:41
 * created by:gzq
 * description:单次血氧测量
 */
public class SingleMeasureBloodoxygenFragment extends Bloodoxygen_Fragment {
    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 2) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                    "主人，您本次测量血氧" + results[0] + "%", false);


            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData pressureData = new DetectionData();
            DetectionData dataPulse = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            pressureData.setDetectionType("6");
            pressureData.setBloodOxygen(Float.parseFloat(results[0]));
            dataPulse.setDetectionType("9");
            dataPulse.setPulse(Integer.parseInt(results[1]));
            datas.add(pressureData);
            datas.add(dataPulse);

            HealthMeasureRepository.postMeasureData(datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<Object>() {
                        @Override
                        public void onNext(Object o) {
                            ToastUtils.showShort("上传数据成功");
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showShort("上传数据失败:"+e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

//            HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
//                @Override
//                public void onSuccess(String callbackString) {
//                    ToastUtils.showShort("上传数据成功");
//                }
//
//                @Override
//                public void onError() {
//                    ToastUtils.showShort("上传数据失败");
//                }
//            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
