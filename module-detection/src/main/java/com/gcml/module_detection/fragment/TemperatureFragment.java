package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_detection.R;
import com.gcml.common.data.PostDataCallBackBean;
import com.gcml.module_detection.net.DetectionRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DefaultObserver;

public class TemperatureFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private TextView mTvDetectionTime;
    private TextView mTvResultMiddle;
    private TextView mTvUnitMiddle;
    private TextView mReference1;
    private TextView mTvSuggest;
    private TextView mTvDetectionState;
    private ConstraintLayout mClBg;

    @Override
    protected int initLayout() {
        return R.layout.fragment_detection;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mClBg = view.findViewById(R.id.cl_bg);
        mTvDetectionTime = (TextView) view.findViewById(com.gcml.module_detection.R.id.tv_detection_time);
        mTvDetectionState = view.findViewById(R.id.tv_detection_state);
        mTvResultMiddle = (TextView) view.findViewById(com.gcml.module_detection.R.id.tv_result_middle);
        mTvUnitMiddle = (TextView) view.findViewById(com.gcml.module_detection.R.id.tv_unit_middle);
        mReference1 = (TextView) view.findViewById(com.gcml.module_detection.R.id.reference1);
        mTvSuggest = (TextView) view.findViewById(com.gcml.module_detection.R.id.tv_suggest);
        mTvResultMiddle.setVisibility(View.VISIBLE);
        mTvResultMiddle.setText("--");
        mTvUnitMiddle.setVisibility(View.VISIBLE);
        mTvUnitMiddle.setText("℃");
        mReference1.setVisibility(View.VISIBLE);
        mReference1.setText("正常范围：36.1℃~37.1℃");
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                Float temperAture = detectionData.getTemperAture();
                if (!isMeasureFinishedOfThisTime && temperAture != null && temperAture > 30) {
                    if (detectionData.isInit()) {
                        mTvResultMiddle.setText("--");
                        isMeasureFinishedOfThisTime = false;
                    } else {
                        isMeasureFinishedOfThisTime = true;
                        mTvDetectionTime.setText(TimeUtils.milliseconds2String(System.currentTimeMillis(), new SimpleDateFormat("yyyy-MM-dd HH:mm")));
                        mTvResultMiddle.setText(String.format(Locale.getDefault(), "%.2f", temperAture));
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
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        DetectionData temperatureData = new DetectionData();
        temperatureData.setDetectionType("4");
        final Float temperAture = detectionData.getTemperAture();
        temperatureData.setTemperAture(temperAture);
        datas.add(temperatureData);
        DetectionRepository.postMeasureData(datas)
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<PostDataCallBackBean>>() {
                    @Override
                    public void onNext(List<PostDataCallBackBean> o) {
                        notifyActivity(datas,true);
                        if (o == null) return;
                        PostDataCallBackBean postDataCallBackBean = o.get(0);
                        if (postDataCallBackBean == null) return;

                        PostDataCallBackBean.Result2Bean result2 = postDataCallBackBean.getResult2();
                        if (result2 == null) return;
                        mTvSuggest.setText(result2.getResult());
                        mTvDetectionState.setText(result2.getResultConclusion());
                        int resultType = result2.getResultType();
                        switch (resultType) {
                            case 0:
                                mClBg.setBackgroundResource(R.drawable.detection_normal);
                                break;
                            case 1:
                                mClBg.setBackgroundResource(R.drawable.detection_less_high);
                                break;
                            case 2:
                                mClBg.setBackgroundResource(R.drawable.detection_more_high);
                                break;
                        }
                        PostDataCallBackBean.Result1Bean result1 = postDataCallBackBean.getResult1();
                        if (result1 == null) {
                        } else {
                            mTvDetectionState.setText(result1.getDiagnose());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        notifyActivity(datas,false);
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
