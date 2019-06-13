package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Observer;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_detection.R;
import com.gcml.module_detection.bean.PostDataCallBackBean;
import com.gcml.module_detection.net.DetectionRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

public class WeightFragment extends BluetoothBaseFragment implements View.OnClickListener {
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
    private boolean isPostDataTrue;
    private int height;

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
        mTvUnitLeft.setText("Kg");
        mTvUnitRight.setVisibility(View.VISIBLE);
        mTvUnitRight.setText("Kg/m²");
        mReference1.setVisibility(View.VISIBLE);
        mReference1.setText("BMI正常范围：18.5~23.9");
        height = UserSpHelper.getUserHeight();
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                Float weight = detectionData.getWeight();
                if (!isMeasureFinishedOfThisTime && weight != null && weight != 0) {
                    if (detectionData.isInit()) {
                        mTvResultLeft.setText("--");
                        mTvResultRight.setText("--");
                    } else {
                        if (detectionData.isWeightOver()) {
                            isMeasureFinishedOfThisTime = true;
                            onMeasureFinished(detectionData);
                            robotSpeak(detectionData);
                            postData(detectionData);
                            mTvResultLeft.setText(String.format(Locale.getDefault(), "%.1f", weight));
                        } else {
                            mTvResultLeft.setText(String.format(Locale.getDefault(), "%.1f", weight));
                        }
                    }
                }
            }
        });
    }

    private void robotSpeak(DetectionData detectionData) {
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量体重" + detectionData.getWeight() + "公斤", false);
    }

    private void postData(DetectionData detectionData) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        DetectionData weightData = new DetectionData();
        weightData.setDetectionType("3");
        Float weight = detectionData.getWeight();
        weightData.setWeight(weight);
        datas.add(weightData);
        DetectionRepository.postMeasureData(datas)
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<PostDataCallBackBean>>() {
                    @Override
                    public void onNext(List<PostDataCallBackBean> o) {
                        //解决数据多次上传，页面混乱，不知道有没有效果，（好像有效果）
                        if (isPostDataTrue) {
                            return;
                        }
                        isPostDataTrue = true;
                        if (o == null) return;
                        PostDataCallBackBean postDataCallBackBean = o.get(0);
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
