package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Lifecycle;
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

import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

public class BloodpressureFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private TextView mTvDetectionTime;
    private TextView mTvDetectionState;
    private TextView mTvResultLeft;
    private TextView mTvResultRight;
    private TextView mTvUnitLeft;
    private TextView mTvUnitRight;
    private TextView mReference1;
    private TextView mReference2;
    private TextView mTvSuggest;
    private ConstraintLayout mClBg;

    @Override
    protected int initLayout() {
        return R.layout.fragment_detection;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mClBg = view.findViewById(R.id.cl_bg);
        mTvDetectionTime = (TextView) view.findViewById(R.id.tv_detection_time);
        mTvDetectionState = (TextView) view.findViewById(R.id.tv_detection_state);
        mTvResultLeft = (TextView) view.findViewById(R.id.tv_result_left);
        mTvResultRight = (TextView) view.findViewById(R.id.tv_result_right);
        mTvUnitLeft = (TextView) view.findViewById(R.id.tv_unit_left);
        mTvUnitRight = (TextView) view.findViewById(R.id.tv_unit_right);
        mReference1 = (TextView) view.findViewById(R.id.reference1);
        mReference2 = (TextView) view.findViewById(R.id.reference2);
        mTvSuggest = (TextView) view.findViewById(R.id.tv_suggest);
        mTvResultLeft.setVisibility(View.VISIBLE);
        mTvResultRight.setVisibility(View.VISIBLE);
        mTvUnitLeft.setVisibility(View.VISIBLE);
        mTvUnitLeft.setText("mmHg");
        mTvUnitRight.setVisibility(View.VISIBLE);
        mTvUnitRight.setText("次/min");
        mReference1.setVisibility(View.VISIBLE);
        mReference2.setVisibility(View.VISIBLE);
        mReference1.setText("收缩压：90mmHg~140mmHg");
        mReference2.setText("舒张压：60mmHg~90mmHg");
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                Integer highPressure = detectionData.getHighPressure();
                if (!isMeasureFinishedOfThisTime && highPressure != null && highPressure != 0) {
                    if (detectionData.isInit()) {
                        mTvResultLeft.setText(highPressure + "/--");
                        mTvResultRight.setText("--");
                        isMeasureFinishedOfThisTime = false;
                    } else {
                        mTvDetectionTime.setText(TimeUtils.milliseconds2String(System.currentTimeMillis(), new SimpleDateFormat("yyyy-MM-dd HH:mm")));
                        mTvResultLeft.setText(highPressure + "/" + detectionData.getLowPressure());
                        mTvResultRight.setText(String.valueOf(detectionData.getPulse()));
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
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量高压" + detectionData.getHighPressure() + ",低压" + detectionData.getLowPressure() + ",脉搏" + detectionData.getPulse(), false);
    }

    private void postData(DetectionData detectionData) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData pulseData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        pressureData.setDetectionType("0");
        pressureData.setHighPressure(detectionData.getHighPressure());
        pressureData.setLowPressure(detectionData.getLowPressure());
        pulseData.setDetectionType("9");
        pulseData.setPulse(detectionData.getPulse());
        datas.add(pressureData);
        datas.add(pulseData);

        DetectionRepository.postMeasureData(datas)
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this, Lifecycle.Event.ON_STOP))
                .subscribe(new DefaultObserver<List<PostDataCallBackBean>>() {
                    @Override
                    public void onNext(List<PostDataCallBackBean> o) {
                        Timber.i(">>>>>血压<<<<<<<");
                        notifyActivity(datas,true);
                        if (o == null) return;
                        PostDataCallBackBean postDataCallBackBean = o.get(0);
                        if (postDataCallBackBean == null) return;
                        PostDataCallBackBean.Result1Bean result1 = postDataCallBackBean.getResult1();
                        if (result1 == null) return;
                        PostDataCallBackBean.Result2Bean result2 = postDataCallBackBean.getResult2();
                        if (result2 == null) return;
//                        String diagnose = result1.getDiagnose();
                        mTvDetectionState.setText(result2.getResultConclusion());
                        mTvSuggest.setText(result2.getResult());
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
