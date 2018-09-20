package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.measure_abnormal.HealthMeasureAbnormalActivity;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 10:54
 * created by:gzq
 * description:单次血糖测量
 */
public class SingleMeasureBloodsugarFragment extends Bloodsugar_Fragment {
    private Bundle bundle;
    private ArrayList<DetectionData> datas;
    private static final int CODE_REQUEST_ABNORMAL = 10002;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        this.bundle = bundle;
        if (bundle != null) {
            if (bundle.getBoolean("isOnlyShowBtnHealthRecord")) {
                mBtnVideoDemo.setVisibility(View.GONE);
                mBtnHealthHistory.setText("下一步");
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            String roundUp = DataUtils.getRoundUp(results[0], 1);
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + roundUp, false);

            datas = new ArrayList<>();
            DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("1");
            if (bundle != null) {
                data.setSugarTime(bundle.getInt("selectMeasureSugarTime"));
            } else {
                data.setSugarTime(0);
            }
            data.setBloodSugar(Float.parseFloat(roundUp));
            datas.add(data);

            HealthMeasureRepository.checkIsNormalData(UserSpHelper.getUserId(), datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<Object>() {
                        @Override
                        public void onNext(Object o) {
                            uploadData();
                        }

                        @Override
                        public void onError(Throwable e) {
                            HealthMeasureAbnormalActivity.startActivity(mActivity, IPresenter.MEASURE_BLOOD_SUGAR, CODE_REQUEST_ABNORMAL);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }

    @SuppressLint("CheckResult")
    private void uploadData() {
        if (datas==null){
            Timber.e("SingleMeasureBloodpressureFragment：数据被回收，程序异常");
            return;
        }

        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("数据上传成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("数据上传失败:"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
//            @Override
//            public void onSuccess(String callbackString) {
//                ToastUtils.showShort("数据上传成功");
//            }
//
//            @Override
//            public void onError() {
//                ToastUtils.showShort("数据上传失败");
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_REQUEST_ABNORMAL) {
                if (data != null) {
                    boolean booleanExtra = data.getBooleanExtra(HealthMeasureAbnormalActivity.KEY_HAS_ABNIRMAL_REASULT, false);
                    if (booleanExtra) {
                        //数据异常
                        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，因为你测量出现偏差，此次测量将不会作为历史数据");
                    } else {
                        uploadData();
                    }

                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
