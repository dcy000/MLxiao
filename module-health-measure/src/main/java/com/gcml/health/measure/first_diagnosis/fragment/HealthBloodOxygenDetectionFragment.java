package com.gcml.health.measure.first_diagnosis.fragment;

import android.annotation.SuppressLint;
import android.view.View;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.base.DetectionDataBean;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodoxygen.BloodOxygenFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 14:05
 * created by:gzq
 * description:TODO
 */
public class HealthBloodOxygenDetectionFragment extends BloodOxygenFragment {
    private boolean isJump2Next = false;

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText(UM.getString(R.string.next_step));
        setBtnClickableState(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.open_device_and_detection), false);
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
        if (results.length == 2) {
            ArrayList<DetectionData> datas = new ArrayList<>();
            final DetectionData data = new DetectionData();
            //0血压 01左侧血压 02右侧血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 10腰围 11呼吸频率 12身高 13心率
            data.setDetectionType("6");
            data.setBloodOxygen(Float.parseFloat(results[0]));
            data.setPulse(Integer.parseInt(results[1]));
            datas.add(data);
            if (fragmentDatas != null) {
                fragmentDatas.data(new DetectionDataBean(IPresenter.MEASURE_BLOOD_OXYGEN, datas));
            }
            HealthMeasureRepository.postMeasureData(datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                    .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                        @Override
                        public void onNext(List<DetectionResult> o) {
//                            ((FirstDiagnosisActivity) mActivity).putCacheData(data);
                            setBtnClickableState(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showShort(UM.getString(R.string.upload_data_fail) + ":" + e.getMessage());
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
