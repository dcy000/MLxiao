package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.SingleDialog;
import com.gcml.health.measure.bloodpressure_habit.GetHypertensionHandActivity;
import com.gcml.health.measure.first_diagnosis.bean.ApiResponse;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.measure_abnormal.HealthMeasureAbnormalActivity;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.health.measure.single_measure.ShowMeasureBloodpressureResultActivity;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.synthetize.MLVoiceSynthetize;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 9:54
 * created by:gzq
 * description:单次血压测量
 */
public class SingleMeasureBloodpressureFragment extends Bloodpressure_Fragment {
    private static final int CODE_REQUEST_ABNORMAL = 10001;
    private ArrayList<DetectionData> datas;
    private int highPressure;
    private int lowPressure;
    private boolean isMeasureTask = false;

    public SingleMeasureBloodpressureFragment() {
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        isMeasureTask = bundle.getBoolean(IPresenter.IS_MEASURE_TASK);
    }

    @Override
    public void onResume() {
        super.onResume();
        getHypertensionHand();
    }

    /**
     * 获取惯用手
     */
    private void getHypertensionHand() {
        String userHypertensionHand = UserSpHelper.getUserHypertensionHand();
        if (TextUtils.isEmpty(userHypertensionHand)) {
            //还没有录入惯用手，则跳转到惯用手录入activity
            mContext.startActivity(new Intent(mContext, GetHypertensionHandActivity.class));
        } else {
            if ("0".equals(userHypertensionHand)) {
                showHypertensionHandDialog("左手");
            } else if ("1".equals(userHypertensionHand)) {
                showHypertensionHandDialog("右手");
            }
        }
    }

    private void showHypertensionHandDialog(String hand) {
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，请使用" + hand + "测量");
        new SingleDialog(mContext)
                .builder()
                .setMsg("请使用" + hand + "测量")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量高压" + results[0] + ",低压" + results[1] + ",脉搏" + results[2], false);

            datas = new ArrayList<>();
            DetectionData pressureData = new DetectionData();
            DetectionData dataPulse = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            pressureData.setDetectionType("0");
            highPressure = Integer.parseInt(results[0]);
            pressureData.setHighPressure(highPressure);
            lowPressure = Integer.parseInt(results[1]);
            pressureData.setLowPressure(lowPressure);
            dataPulse.setDetectionType("9");
            dataPulse.setPulse(Integer.parseInt(results[2]));
            datas.add(pressureData);
            datas.add(dataPulse);

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
                            HealthMeasureAbnormalActivity.startActivity(mActivity, IPresenter.MEASURE_BLOOD_PRESSURE, CODE_REQUEST_ABNORMAL);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }

    private void uploadData() {
        if (datas == null) {
            Timber.e("SingleMeasureBloodpressureFragment：数据被回收，程序异常");
            return;
        }
        HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
            @Override
            public void onSuccess(String callbackString) {
                try {
                    ApiResponse<List<DetectionResult>> apiResponse = new Gson().fromJson(callbackString,
                            new TypeToken<ApiResponse<List<DetectionResult>>>() {
                            }.getType());
                    if (apiResponse.isSuccessful()) {
                        ToastUtils.showLong("上传数据成功");
                        DetectionResult result = apiResponse.getData().get(0);
                        if (isMeasureTask) {
                            ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result.getDiagnose(),
                                    result.getScore(), highPressure, lowPressure, result.getResult(), true);
                            mActivity.finish();
                        } else {
                            ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result.getDiagnose(),
                                    result.getScore(), highPressure, lowPressure, result.getResult());
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                ToastUtils.showShort("上传数据失败");
            }
        });
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
