package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_detection.R;
import com.gcml.module_detection.bean.PostDataCallBackBean;
import com.gcml.module_detection.net.DetectionRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

public class BloodSugarFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private Bundle bundle;
    /**
     * --:--
     */
    private TextView mTvDetectionTime;
    /**
     * 测量中
     */
    private TextView mTvDetectionState;
    /**
     * --
     */
    private TextView mTvResultMiddle;
    /**
     * --
     */
    private TextView mTvUnitMiddle;
    /**
     * --
     */
    private TextView mReference1;
    /**
     * --
     */
    private TextView mReference2;
    /**
     *
     */
    private TextView mTvSuggest;

    @Override
    protected int initLayout() {
        return R.layout.fragment_detection;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        this.bundle = bundle;
        mTvDetectionTime = (TextView) view.findViewById(R.id.tv_detection_time);
        mTvDetectionState = (TextView) view.findViewById(R.id.tv_detection_state);
        mTvResultMiddle = (TextView) view.findViewById(R.id.tv_result_middle);
        mTvUnitMiddle = (TextView) view.findViewById(R.id.tv_unit_middle);
        mReference1 = (TextView) view.findViewById(R.id.reference1);
        mReference2 = (TextView) view.findViewById(R.id.reference2);
        mTvSuggest = (TextView) view.findViewById(R.id.tv_suggest);
        mTvResultMiddle.setVisibility(View.VISIBLE);
        mTvUnitMiddle.setVisibility(View.VISIBLE);
        mTvUnitMiddle.setText("mmol/L");
        mReference1.setVisibility(View.VISIBLE);
        mReference1.setText("正常范围：3.9mmol/L~11.1mmol/L");

        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                if (detectionData.isInit()) {
                    mTvResultMiddle.setText("--");
                    isMeasureFinishedOfThisTime = false;
                } else {
                    Float bloodSugar = detectionData.getBloodSugar();
                    mTvDetectionTime.setText(TimeUtils.milliseconds2String(System.currentTimeMillis(), new SimpleDateFormat("yyyy-MM-dd HH:mm")));
                    mTvResultMiddle.setText(String.format(Locale.getDefault(), "%.1f", bloodSugar));
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

    int selectMeasureSugarTime = 0;

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
            selectMeasureSugarTime = bundle.getInt("selectMeasureSugarTime");
            bloodSugarData.setSugarTime(selectMeasureSugarTime);
        } else {
            bloodSugarData.setSugarTime(0);
        }
        Float bloodSugar = detectionData.getBloodSugar();
        bloodSugarData.setBloodSugar(bloodSugar);
        datas.add(bloodSugarData);
        DetectionRepository.postMeasureData(datas)
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<PostDataCallBackBean>>() {
                    @Override
                    public void onNext(List<PostDataCallBackBean> o) {
                        if (o == null) return;
                        PostDataCallBackBean postDataCallBackBean = o.get(0);
                        PostDataCallBackBean.Result2Bean result2 = postDataCallBackBean.getResult2();
                        if (result2 == null) return;
                        mTvSuggest.setText(result2.getResult());

                        PostDataCallBackBean.Result1Bean result1 = postDataCallBackBean.getResult1();
                        if (result1 == null) {
                            switch (selectMeasureSugarTime) {
                                case 0:
                                    if (bloodSugar <= 3.9) {
                                        mTvDetectionState.setText("偏低");
                                        return;
                                    }
                                    if (bloodSugar < 6.1) {
                                        mTvDetectionState.setText("正常");
                                        return;
                                    }
                                    mTvDetectionState.setText("偏高");
                                    break;
                                case 2:
                                    if (bloodSugar <= 3.9) {
                                        mTvDetectionState.setText("偏低");
                                        return;
                                    }
                                    if (bloodSugar < 7.8) {
                                        mTvDetectionState.setText("正常");
                                        return;
                                    }
                                    mTvDetectionState.setText("偏高");
                                    break;
                                case 3:
                                    if (bloodSugar <= 3.9) {
                                        mTvDetectionState.setText("偏低");
                                        return;
                                    }
                                    if (bloodSugar < 11.1) {
                                        mTvDetectionState.setText("正常");
                                        return;
                                    }
                                    mTvDetectionState.setText("偏高");
                                    break;
                            }
                            if (bloodSugar <= 94) {
                                mTvDetectionState.setText("异常");
                                return;
                            }
                            mTvDetectionState.setText("正常");
                        } else {
                            mTvDetectionState.setText(result1.getDiagnose());
                        }
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
