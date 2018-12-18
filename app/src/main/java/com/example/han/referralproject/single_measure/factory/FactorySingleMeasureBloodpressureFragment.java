package com.example.han.referralproject.single_measure.factory;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.MeasureResult;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.single_measure.bean.DetectionData;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 9:54
 * created by:gzq
 * description:单次血压测量
 */
public class FactorySingleMeasureBloodpressureFragment extends Bloodpressure_Fragment {
    private static final int CODE_REQUEST_ABNORMAL = 10001;
    private static final int CODE_REQUEST_GETHYPERTENSIONHAND = 10002;

    public static final String PRESS_FALG_WZ = "WZ";
    public static final String PRESS_FALG = "PressureFlag";
    private ArrayList<DetectionData> datas;
    private boolean isOnPause = false;

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3 && !isOnPause) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量高压" + results[0] + ",低压" + results[1] + ",脉搏" + results[2], false);

//            datas = new ArrayList<>();
//            DetectionData pressureData = new DetectionData();
//            DetectionData dataPulse = new DetectionData();
//            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
//            pressureData.setDetectionType("0");
//            highPressure = Integer.parseInt(results[0]);
//            pressureData.setHighPressure(highPressure);
//            lowPressure = Integer.parseInt(results[1]);
//            pressureData.setLowPressure(lowPressure);
//            dataPulse.setDetectionType("9");
//            int pulse = Integer.parseInt(results[2]);
//            dataPulse.setPulse(pulse);
//            datas.add(pressureData);
//            datas.add(dataPulse);

            int highPressure = Integer.parseInt(results[0]);
            int lowPressure = Integer.parseInt(results[1]);
            int pulse = Integer.parseInt(results[2]);
            LocalShared.getInstance(getActivity()).setXueYa(highPressure + "," + lowPressure);
            uploadXueyaResult(highPressure, lowPressure, pulse, true, null);
          /* 新的提交接口 HealthMeasureRepository.checkIsNormalData(LocalShared.getInstance(getActivity()).getUserId(), datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<Object>() {
                        @Override
                        public void onNext(Object o) {
                            if (listener != null) {
                                listener.onNext(o, bundle);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (listener != null) {
                                listener.onError(e, bundle);
                            }
                        }

                        @Override
                        public void onComplete() {
                            if (listener != null) {
                                listener.onComplete(bundle);
                            }

                        }
                    });*/


        }
    }

    /**
     * 上传血压的测量结果
     */
    private void uploadXueyaResult(final int getNew, final int down, final int maibo, final boolean status, final Fragment fragment) {
        DataInfoBean info = new DataInfoBean();
        info.high_pressure = getNew;
        info.low_pressure = down;
        info.pulse = maibo;
        if (status) {
            info.upload_state = true;
        }
        NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
            @Override
            public void onSuccess(MeasureResult response) {
                if (listener != null) {
                    listener.onNext(response, bundle);
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                if (listener != null) {
                    listener.onError(message, bundle);
                }
            }

        });
    }

    private static final String TAG = "SingleMeasureBloodpress";

    @Override
    public void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }

    public interface PostResultListener {
        void onNext(Object data, Bundle args);

        void onError(Object e, Bundle args);

        void onComplete(Bundle args);

        void clickLiftView(Bundle args);
    }

    PostResultListener listener;

    public void setListener(PostResultListener listener) {
        this.listener = listener;
    }

    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);

        if (listener != null) {
            listener.clickLiftView(bundle);
        }

    }

    @Override
    protected void clickVideoDemo(View view) {
        super.clickVideoDemo(view);

    }

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        mBtnHealthHistory.setVisibility(View.GONE);
    }
}
