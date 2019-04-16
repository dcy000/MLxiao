package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.base.DetectionDataBean;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.three.ThreeInOneFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HealthThreeInOneDetectionUiFragment extends ThreeInOneFragment {
    private ArrayList<DetectionData> datas = new ArrayList<>();
    private boolean isJump2Next = false;
    private DetectionData sugarData;
    private DetectionData cholesterolData;
    private DetectionData lithicAcidData;
    private int selectMeasureSugarTime;

    @Override
    public void onStart() {
        super.onStart();
        isJump2Next = false;
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText(R.string.next_step);
        setBtnClickableState(false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            selectMeasureSugarTime = arguments.getInt("selectMeasureSugarTime", 0);
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
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.test_strip_insertion_instrument_and_measure), false);
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
            }
            if (results[0].equals("cholesterol")) {
                cholesterolData = new DetectionData();
                cholesterolData.setDetectionType("7");
                cholesterolData.setCholesterol(Float.parseFloat(results[1]));
                datas.add(cholesterolData);
                MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.this_time_cholesterol) + cholesterolData.getCholesterol());
                uploadData(datas);
            }

            if (results[0].equals("bua")) {
                lithicAcidData = new DetectionData();
                lithicAcidData.setDetectionType("8");
                lithicAcidData.setUricAcid(Float.parseFloat(results[1]));

                datas.add(lithicAcidData);
                MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.this_time_uric_acid) + lithicAcidData.getUricAcid());
                uploadData(datas);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void uploadData(ArrayList<DetectionData> datas) {
        if (fragmentDatas != null) {
            fragmentDatas.data(new DetectionDataBean(IPresenter.MEASURE_THREE, datas));
        }
        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showLong(R.string.upload_data_success);
                        setBtnClickableState(true);
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
    protected void clickHealthHistory(View view) {
        if (fragmentChanged != null && !isJump2Next) {
            isJump2Next = true;
            fragmentChanged.onFragmentChanged(this, null);
        }
    }

    private void setBtnClickableState(boolean enableClick) {
        if (enableClick) {
            mBtnHealthHistory.setClickable(true);
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_health_history_set);
        } else {
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
            mBtnHealthHistory.setClickable(false);
        }
    }
}
