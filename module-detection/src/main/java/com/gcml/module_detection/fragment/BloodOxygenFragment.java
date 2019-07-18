package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_detection.ConnectActivity;
import com.gcml.module_detection.R;
import com.gcml.common.data.PostDataCallBackBean;
import com.gcml.module_detection.net.DetectionRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DefaultObserver;

public class BloodOxygenFragment extends BluetoothBaseFragment implements View.OnClickListener {
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
        mTvDetectionTime = (TextView) view.findViewById(R.id.tv_detection_time);
        mTvDetectionState = view.findViewById(R.id.tv_detection_state);
        mClBg = view.findViewById(R.id.cl_bg);
        mTvResultMiddle = (TextView) view.findViewById(R.id.tv_result_middle);
        mTvUnitMiddle = (TextView) view.findViewById(R.id.tv_unit_middle);
        mReference1 = (TextView) view.findViewById(R.id.reference1);
        mTvSuggest = (TextView) view.findViewById(R.id.tv_suggest);
        mTvResultMiddle.setVisibility(View.VISIBLE);
        mTvResultMiddle.setText("--");
        mTvUnitMiddle.setVisibility(View.VISIBLE);
        mTvUnitMiddle.setText("%");
        mReference1.setVisibility(View.VISIBLE);
        mReference1.setText("正常范围：>94%");
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                Float bloodOxygen = detectionData.getBloodOxygen();
                if (!isMeasureFinishedOfThisTime && bloodOxygen != null && bloodOxygen != 0) {
                    if (detectionData.isInit()) {
                        mTvResultMiddle.setText("--");
                    } else {
                        isMeasureFinishedOfThisTime = true;
                        mTvDetectionTime.setText(TimeUtils.milliseconds2String(System.currentTimeMillis(), new SimpleDateFormat("yyyy-MM-dd hh:mm")));
                        mTvResultMiddle.setText(String.format(Locale.getDefault(), "%.0f", bloodOxygen));
                        onMeasureFinished(detectionData);
                        robotSpeak(detectionData);
                        postData(detectionData);

                    }
                }
            }
        });
    }

    private void robotSpeak(DetectionData detectionData) {
        MLVoiceSynthetize.startSynthesize(UM.getApp(),
                "您本次测量血氧" +  String.format(Locale.getDefault(), "%.0f", detectionData.getBloodOxygen()) + "%", false);
    }

    private void postData(DetectionData detectionData) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData oxygenData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        oxygenData.setDetectionType("6");
        Float bloodOxygen = detectionData.getBloodOxygen();
        oxygenData.setBloodOxygen(bloodOxygen);
        datas.add(oxygenData);
        DetectionRepository.postMeasureData(datas)
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<PostDataCallBackBean>>() {
                    @Override
                    public void onNext(List<PostDataCallBackBean> o) {
                        notifyActivity(datas,true, IBleConstants.MEASURE_BLOOD_OXYGEN);
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
                        notifyActivity(datas,false, IBleConstants.MEASURE_BLOOD_OXYGEN);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof ConnectActivity) {
            ConnectActivity connectActivity = (ConnectActivity) activity;
            connectActivity.setBtnNextVisible(!connectActivity.isSingleDetection());
//            mBtnHealthHistory.setVisibility(connectActivity.isSingleDetection() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentActivity activity = getActivity();
        if (activity instanceof ConnectActivity) {
            ((ConnectActivity) activity).setBtnNextVisible(false);
        }
    }

    @Override
    public void onClick(View v) {

    }


}
