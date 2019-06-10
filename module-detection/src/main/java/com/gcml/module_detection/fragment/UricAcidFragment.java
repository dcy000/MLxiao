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
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_detection.R;
import com.gcml.module_detection.net.DetectionRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

public class UricAcidFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private TextView mTvDetectionTime;
    private TextView mTvResultMiddle;
    private TextView mTvUnitMiddle;
    private TextView mReference1;
    private TextView mReference2;
    private TextView mTvSuggest;
    private boolean isMeasureBUAFinished;

    @Override
    protected int initLayout() {
        return R.layout.fragment_detection;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mTvDetectionTime = (TextView) view.findViewById(com.gcml.module_detection.R.id.tv_detection_time);
        mTvResultMiddle = (TextView) view.findViewById(com.gcml.module_detection.R.id.tv_result_middle);
        mTvUnitMiddle = (TextView) view.findViewById(com.gcml.module_detection.R.id.tv_unit_middle);
        mReference1 = (TextView) view.findViewById(com.gcml.module_detection.R.id.reference1);
        mReference2 = (TextView) view.findViewById(R.id.reference2);
        mTvSuggest = (TextView) view.findViewById(com.gcml.module_detection.R.id.tv_suggest);
        mTvResultMiddle.setVisibility(View.VISIBLE);
        mTvResultMiddle.setText("--");
        mTvUnitMiddle.setVisibility(View.VISIBLE);
        mTvUnitMiddle.setText("mmol/L");
        mReference1.setVisibility(View.VISIBLE);
        mReference1.setText("男性：≤0.42mmol/L");
        mReference2.setVisibility(View.VISIBLE);
        mReference2.setText("女性：≤0.36mmol/L");
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                if (detectionData.isInit()) {
                    isMeasureBUAFinished = false;
                } else {
                    Float uricAcid = detectionData.getUricAcid();
                    if (uricAcid != null && uricAcid != 0 && !isMeasureBUAFinished) {
                        isMeasureBUAFinished = true;
                        mTvDetectionTime.setText(TimeUtils.milliseconds2String(System.currentTimeMillis(), new SimpleDateFormat("yyyy-MM-dd HH:mm")));
                        mTvResultMiddle.setText(String.format(Locale.getDefault(), "%.2f", uricAcid));
                        onMeasureFinished(detectionData);
                        robotSpeak(detectionData);
                        postData(detectionData);
                    }
                }
            }
        });
    }

    private void robotSpeak(DetectionData detectionData) {
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量耳温" + detectionData.getTemperAture() + "摄氏度", false);
    }

    private void postData(DetectionData detectionData) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData lithicAcidData = new DetectionData();
        lithicAcidData.setDetectionType("8");
        lithicAcidData.setUricAcid(detectionData.getUricAcid());
        datas.add(lithicAcidData);
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
