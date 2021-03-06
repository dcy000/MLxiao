package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.R;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HealthSugarDetectionUiFragment extends Bloodsugar_Fragment {


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
        MLVoiceSynthetize.startSynthesize(getContext(), "主人，请将试纸插入仪器，开始测量", false);
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
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                        @Override
                        public void onNext(List<DetectionResult> o) {
                            setBtnClickableState(true);
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
////                    if (fragmentChanged != null && !isJump2Next) {
////                        isJump2Next = true;
////                        fragmentChanged.onFragmentChanged(HealthSugarDetectionUiFragment.this, null);
////                    }
//                    setBtnClickableState(true);
//                    ((FirstDiagnosisActivity) mActivity).putCacheData(data);
//                }
//
//                @Override
//                public void onError() {
//                    ToastUtils.showShort("上传数据失败");
//                }
//            });
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
