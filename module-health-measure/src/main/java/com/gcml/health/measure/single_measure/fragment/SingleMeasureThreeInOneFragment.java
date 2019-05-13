package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.three.ThreeInOneFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 11:13
 * created by:gzq
 * description:单次三合一测量
 */
public class SingleMeasureThreeInOneFragment extends ThreeInOneFragment {
    private ArrayList<DetectionData> datas = new ArrayList<>();
    DetectionData sugarData;
    DetectionData cholesterolData;
    DetectionData lithicAcidData;
    private int selectMeasureSugarTime;
    private DetectionData results;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        if (bundle != null) {
            selectMeasureSugarTime = bundle.getInt("selectMeasureSugarTime");
        }
        mTitle11.setText("<3.9");
        switch (selectMeasureSugarTime) {
            case 0:
                //空腹
                mTitle1.setText("血糖(空腹)");
                mTitle12.setText("3.9~6.1");
                mTitle13.setText(">6.1");
                break;
            case 1:
                //饭后1小时
                mTitle1.setText("血糖(饭后1小时)");
                mTitle12.setText("3.9~7.8");
                mTitle13.setText(">7.8");
                break;
            case 2:
                //饭后2小时
                mTitle1.setText("血糖(饭后2小时)");
                mTitle12.setText("3.9~7.8");
                mTitle13.setText(">7.8");
                break;
            case 3:
                //其他时间
                mTitle1.setText("血糖(其他时间)");
                mTitle12.setText("3.9~11.1");
                mTitle13.setText(">11.1");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onMeasureFinished(DetectionData detectionData) {
        this.results = detectionData;
        if (detectionData.getBloodSugar()!=0) {
            sugarData = new DetectionData();
            sugarData.setDetectionType("1");
            sugarData.setSugarTime(selectMeasureSugarTime);
            sugarData.setBloodSugar(detectionData.getBloodSugar());
            datas.add(sugarData);
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量血糖" + sugarData.getBloodSugar());
            uploadData(datas);

            if (measureItemChanged != null) {
                measureItemChanged.onChanged(2);
            }
        }
        if (detectionData.getCholesterol()!=0) {
            cholesterolData = new DetectionData();
            cholesterolData.setDetectionType("7");
            cholesterolData.setCholesterol(detectionData.getCholesterol());
            datas.add(cholesterolData);
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量胆固醇" + cholesterolData.getCholesterol());
            uploadData(datas);
            if (measureItemChanged != null) {
                measureItemChanged.onChanged(5);
            }
        }

        if (detectionData.getUricAcid()!=0) {
            lithicAcidData = new DetectionData();
            lithicAcidData.setDetectionType("8");
            lithicAcidData.setUricAcid(detectionData.getUricAcid());

            datas.add(lithicAcidData);
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量尿酸" + lithicAcidData.getUricAcid());
            uploadData(datas);
            if (measureItemChanged != null) {
                measureItemChanged.onChanged(6);
            }
        }
    }


    @SuppressLint("CheckResult")
    private void uploadData(ArrayList<DetectionData> datas) {

        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> o) {
                        ToastUtils.showLong("数据上传成功");
                        datas.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        datas.clear();
                        showUploadDataFailedDialog(results);
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
