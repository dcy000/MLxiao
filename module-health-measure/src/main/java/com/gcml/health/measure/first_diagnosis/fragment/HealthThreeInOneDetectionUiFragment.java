package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.base.DetectionDataBean;
import com.gcml.module_blutooth_devices.base.IBleConstants;
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
        mBtnHealthHistory.setText("下一步");
        setBtnClickableState(false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            selectMeasureSugarTime = arguments.getInt("selectMeasureSugarTime", 0);
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
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请将试纸插入仪器，开始测量", false);
    }

    @Override
    protected void onMeasureFinished(DetectionData detectionData) {
        if (detectionData.getBloodSugar() != 0) {
            sugarData = new DetectionData();
            sugarData.setDetectionType("1");
            sugarData.setSugarTime(selectMeasureSugarTime);
            sugarData.setBloodSugar(detectionData.getBloodSugar());
            datas.add(sugarData);
            uploadData(datas);
        }
        if (detectionData.getCholesterol() != 0) {
            cholesterolData = new DetectionData();
            cholesterolData.setDetectionType("7");
            cholesterolData.setCholesterol(detectionData.getCholesterol());
            datas.add(cholesterolData);
            uploadData(datas);
        }

        if (detectionData.getUricAcid() != 0) {
            lithicAcidData = new DetectionData();
            lithicAcidData.setDetectionType("8");
            lithicAcidData.setUricAcid(detectionData.getUricAcid());
            datas.add(lithicAcidData);
            uploadData(datas);
        }
    }

    @SuppressLint("CheckResult")
    private void uploadData(ArrayList<DetectionData> datas) {
        if (fragmentDatas!=null){
            fragmentDatas.data(new DetectionDataBean(IBleConstants.MEASURE_THREE,datas));
        }
        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showLong("数据上传成功");
                        setBtnClickableState(true);
                        datas.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("数据上传失败:" + e.getMessage());
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
