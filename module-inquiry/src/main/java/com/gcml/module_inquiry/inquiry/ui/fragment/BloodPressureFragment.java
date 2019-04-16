package com.gcml.module_inquiry.inquiry.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.DetectionResult;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.bloodpressure.BloodPressurePresenter;
import com.gcml.module_blutooth_devices.bloodpressure.BloodpressureFragment;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.WenZenEntryAcitivity;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.ChildActionListenerAdapter;
import com.gcml.module_inquiry.model.HealthFileRepostory;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by lenovo on 2019/3/22.
 */

public class BloodPressureFragment extends BloodpressureFragment {
    private ArrayList<DetectionData> datas;
    private int highPressure;
    private int lowPressure;
    private BloodPressurePresenter bloodPressurePresenter;
    private int pulse;

    @Override
    protected BaseBluetooth obtainPresenter() {
        bloodPressurePresenter = new BloodPressurePresenter(this);

        WenZenEntryAcitivity activity = (WenZenEntryAcitivity) getActivity();
        activity.setBlueTitle(R.drawable.common_icon_bluetooth_break, bloodPressurePresenter);
        return bloodPressurePresenter;
    }

    protected void onMeasureFinished(String... results) {
        if (results.length == 3) {
            datas = new ArrayList<>();
            DetectionData pressureData = new DetectionData();
            DetectionData dataPulse = new DetectionData();
            //0血压 01左侧血压 02右侧血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 10腰围 11呼吸频率 12身高 13心率
            pressureData.setDetectionType("0");
            highPressure = Integer.parseInt(results[0]);
            pressureData.setHighPressure(highPressure);
            lowPressure = Integer.parseInt(results[1]);
            pressureData.setLowPressure(lowPressure);
            dataPulse.setDetectionType("9");
            pulse = Integer.parseInt(results[2]);
            dataPulse.setPulse(pulse);
            datas.add(pressureData);
            datas.add(dataPulse);
            uploadData();
        }
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        mBtnHealthHistory.setText(getString(R.string.prediagnosis_next_tips));
        mBtnVideoDemo.setText(getString(R.string.prediagnosis_pre_tips));
        mBtnVideoDemo.setVisibility(View.GONE);

//        WenZenEntryAcitivity activity = (WenZenEntryAcitivity) getActivity();
//        activity. setBlueTitle(R.drawable.common_icon_bluetooth_break, bloodPressurePresenter);
    }

    @Override
    protected void clickVideoDemo(View view) {
        if (listenerAdapter != null) {
            listenerAdapter.onFinalNext("8", highPressure + "", lowPressure + "");
        }
    }

    @Override
    protected void clickHealthHistory(View view) {
        if (listenerAdapter != null) {
            listenerAdapter.onBack("8", null, null);
        }

        WenZenEntryAcitivity activity = (WenZenEntryAcitivity) getActivity();
        activity.setNormalTitle();
    }

    HealthFileRepostory fileRepostory = new HealthFileRepostory();

    @SuppressLint("CheckResult")
    private void uploadData() {
        if (datas == null) {
            Timber.e("SingleMeasureBloodpressureFragment：数据被回收，程序异常");
            return;
        }
        LoadingDialog dialog = new LoadingDialog.Builder(mContext)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.prediagnosis_loading_tips))
                .create();
        fileRepostory.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> detectionResults) {
                        ToastUtils.showLong(getString(R.string.prediagnosis_data_submit_success_tips));
                        MLVoiceSynthetize.startSynthesize(UM.getApp(), getString(R.string.prediagnosis_height_pressure_tips) + highPressure + getString(R.string.prediagnosis_height_low_tips) + lowPressure + getString(R.string.prediagnosis_pulse_tips) + pulse, new MLSynthesizerListener() {
                            @Override
                            public void onCompleted(SpeechError speechError) {
                                super.onCompleted(speechError);
                                listenerAdapter.onFinalNext("8", highPressure + "", lowPressure + "");
                            }
                        }, false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    protected ChildActionListenerAdapter listenerAdapter;

    public void setListenerAdapter(ChildActionListenerAdapter listenerAdapter) {
        this.listenerAdapter = listenerAdapter;
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static BloodPressureFragment newInstance(String param1, String param2) {
        BloodPressureFragment fragment = new BloodPressureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateState(String datas) {
        if (listenerAdapter != null) {
            if (!TextUtils.isEmpty(datas) && TextUtils.equals(datas, getString(R.string.bluetooth_device_connected))) {
                listenerAdapter.onBluetoothConnect(bloodPressurePresenter);
            } else if (!TextUtils.isEmpty(datas) && TextUtils.equals(datas, getString(R.string.bluetooth_device_disconnected))) {
                listenerAdapter.onBluetoothBreak(bloodPressurePresenter);
            }
        }
    }
}
