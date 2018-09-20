package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;

import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 11:13
 * created by:gzq
 * description:单次三合一测量
 */
public class SingleMeasureThreeInOneFragment extends ThreeInOne_Fragment {
    private ArrayList<DetectionData> datas = new ArrayList<>();
    DetectionData sugarData;
    DetectionData cholesterolData;
    DetectionData lithicAcidData;

    @Override
    protected void onMeasureFinished(String... results) {
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
//        if (results.length == 2) {
//            if (results[0].equals("bloodsugar")) {
//                sugarData = new DetectionData();
//                sugarData.setDetectionType("1");
//                sugarData.setSugarTime(0);
//                sugarData.setBloodSugar(Float.parseFloat(results[1]));
//            }
//            if (results[0].equals("cholesterol")) {
//                cholesterolData = new DetectionData();
//                cholesterolData.setDetectionType("7");
//                cholesterolData.setCholesterol(Float.parseFloat(results[1]));
//            }
//
//            if (results[0].equals("bua")) {
//                lithicAcidData = new DetectionData();
//                lithicAcidData.setDetectionType("8");
//                lithicAcidData.setUricAcid(Float.parseFloat(results[1]));
//            }
//            if (sugarData != null && cholesterolData != null && lithicAcidData != null) {
//                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖"
//                        + sugarData.getBloodSugar() + ",尿酸" + lithicAcidData.getUricAcid() + ",胆固醇"
//                        + cholesterolData.getCholesterol(), false);
//                datas.add(sugarData);
//                datas.add(cholesterolData);
//                datas.add(lithicAcidData);
//
//                HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
//                    @Override
//                    public void onSuccess(String callbackString) {
//                        ToastUtils.showLong("数据上传成功");
//                    }
//
//                    @Override
//                    public void onError() {
//                        ToastUtils.showLong("数据上传失败");
//                    }
//                });
//            }
//        }

        if (results.length == 2) {
            if (results[0].equals("bloodsugar")) {
                sugarData = new DetectionData();
                sugarData.setDetectionType("1");
                sugarData.setSugarTime(0);
                sugarData.setBloodSugar(Float.parseFloat(results[1]));
                datas.add(sugarData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + sugarData.getBloodSugar());
                uploadData(datas);
            }
            if (results[0].equals("cholesterol")) {
                cholesterolData = new DetectionData();
                cholesterolData.setDetectionType("7");
                cholesterolData.setCholesterol(Float.parseFloat(results[1]));
                datas.add(cholesterolData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量胆固醇" + cholesterolData.getCholesterol());
                uploadData(datas);
            }

            if (results[0].equals("bua")) {
                lithicAcidData = new DetectionData();
                lithicAcidData.setDetectionType("8");
                lithicAcidData.setUricAcid(Float.parseFloat(results[1]));

                datas.add(lithicAcidData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量尿酸" + lithicAcidData.getUricAcid());
                uploadData(datas);
            }
        }
    }
    @SuppressLint("CheckResult")
    private void uploadData(ArrayList<DetectionData> datas) {

//        HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
//            @Override
//            public void onSuccess(String callbackString) {
//                ToastUtils.showLong("数据上传成功");
//                datas.clear();
//            }
//
//            @Override
//            public void onError() {
//                ToastUtils.showLong("数据上传失败");
//                datas.clear();
//            }
//        });

        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showLong("数据上传成功");
                        datas.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("数据上传失败:"+e.getMessage());
                        datas.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
