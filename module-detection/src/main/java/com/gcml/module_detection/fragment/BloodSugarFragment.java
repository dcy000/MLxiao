package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Observer;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.bloodsugar.BloodSugarPresenter;
import com.gcml.module_detection.net.DetectionRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

public class BloodSugarFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;
    private Bundle bundle;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodsugar;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        this.bundle = bundle;
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = view.findViewById(R.id.tv_result);
        mTvResult.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                if (detectionData.isInit()) {
                    mTvResult.setText("0.00");
                    isMeasureFinishedOfThisTime = false;
                } else {
                    Float bloodSugar = detectionData.getBloodSugar();
                    mTvResult.setText(String.format(Locale.getDefault(), "%.1f", bloodSugar));
                    if (!isMeasureFinishedOfThisTime && bloodSugar != null && bloodSugar != 0) {
                        isMeasureFinishedOfThisTime = true;
                        onMeasureFinished(detectionData);
                        robotSpeak(detectionData);
                        postData(detectionData);
                    }
                }
            }
        });
    }

    private void robotSpeak(DetectionData detectionData) {
        String roundUp = DataUtils.getRoundUp(detectionData.getBloodSugar(), 1);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量血糖" + roundUp, false);
    }

    private void postData(DetectionData detectionData) {
        ArrayList<DetectionData> datas = new ArrayList<>();

        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        DetectionData bloodSugarData = new DetectionData();
        bloodSugarData.setDetectionType("1");
        if (bundle != null) {
            bloodSugarData.setSugarTime(bundle.getInt("selectMeasureSugarTime"));
        } else {
            bloodSugarData.setSugarTime(0);
        }
        bloodSugarData.setBloodSugar(detectionData.getBloodSugar());
        datas.add(bloodSugarData);
        DetectionRepository.postMeasureData(datas)
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        Timber.i(">>>>" + o.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {

    }
}
