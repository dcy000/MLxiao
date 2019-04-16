package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gcml.common.utils.RxUtils;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
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
                mTitle1.setText(R.string.title_blood_glucose_empty);
                mTitle12.setText("3.9~6.1");
                mTitle13.setText(">6.1");
                break;
            case 1:
                //饭后1小时
                mTitle1.setText(R.string.title_blood_glucose_one);
                mTitle12.setText("3.9~7.8");
                mTitle13.setText(">7.8");
                break;
            case 2:
                //饭后2小时
                mTitle1.setText(R.string.title_blood_glucose_two);
                mTitle12.setText("3.9~7.8");
                mTitle13.setText(">7.8");
                break;
            case 3:
                //其他时间
                mTitle1.setText(R.string.title_blood_glucose_other);
                mTitle12.setText("3.9~11.1");
                mTitle13.setText(">11.1");
                break;
            default:
                break;
        }
    }


    @Override
    protected void onMeasureFinished(String... results) {
        //0血压 01左侧血压 02右侧血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 10腰围 11呼吸频率 12身高 13心率
        if (results.length == 2) {
            if (results[0].equals("bloodsugar")) {
                sugarData = new DetectionData();
                sugarData.setDetectionType("1");
                sugarData.setSugarTime(selectMeasureSugarTime);
                sugarData.setBloodSugar(Float.parseFloat(results[1]));
                datas.add(sugarData);
                MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.this_time_blood_glucose) + sugarData.getBloodSugar());
                uploadData(datas);

                if (measureItemChanged != null) {
                    measureItemChanged.onChanged(2);
                }
            }
            if (results[0].equals("cholesterol")) {
                cholesterolData = new DetectionData();
                cholesterolData.setDetectionType("7");
                cholesterolData.setCholesterol(Float.parseFloat(results[1]));
                datas.add(cholesterolData);
                MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.this_time_cholesterol) + cholesterolData.getCholesterol());
                uploadData(datas);
                if (measureItemChanged != null) {
                    measureItemChanged.onChanged(5);
                }
            }

            if (results[0].equals("bua")) {
                lithicAcidData = new DetectionData();
                lithicAcidData.setDetectionType("8");
                lithicAcidData.setUricAcid(Float.parseFloat(results[1]));

                datas.add(lithicAcidData);
                MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.this_time_uric_acid) + lithicAcidData.getUricAcid());
                uploadData(datas);
                if (measureItemChanged != null) {
                    measureItemChanged.onChanged(6);
                }
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
                        ToastUtils.showLong(R.string.upload_data_success);
                        datas.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong(UM.getString(R.string.upload_data_fail) + ":" + e.getMessage());
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
