package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.bloodsugar.BloodSugarFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HealthSugarDetectionUiFragment extends BloodSugarFragment {


    private int selectMeasureSugarTime;
    private boolean isJump2Next = false;

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

    }

    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getContext(), "请将试纸插入仪器，开始测量", false);
    }

    @Override
    protected void clickHealthHistory(View view) {
        if (fragmentChanged != null && !isJump2Next) {
            isJump2Next = true;
            fragmentChanged.onFragmentChanged(this, null);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            ArrayList<DetectionData> datas = new ArrayList<>();
            final DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("1");
            data.setSugarTime(selectMeasureSugarTime);
            data.setBloodSugar(Float.parseFloat(results[0]));
            datas.add(data);


            HealthMeasureRepository.postMeasureData(datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                    .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                        @Override
                        public void onNext(List<DetectionResult> o) {
                            ToastUtils.showLong("上传数据成功");
                            setBtnClickableState(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            showUploadDataFailedDialog(results);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

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
