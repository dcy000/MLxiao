package com.gcml.health.measure.first_diagnosis.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 *
 */
public class HealthFirstTipsFragment extends BluetoothBaseFragment implements View.OnClickListener {


    //    private ITimeCountListener timeCountListener;
    private ImageView ivGrayBack;
    private OnSynthesizerListener onSynthesizerListener;
    private long time;

    public HealthFirstTipsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_first_tips;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        if (onSynthesizerListener == null) {
            onSynthesizerListener = new OnSynthesizerListener();
        }
        if (bundle != null) {
            String title = bundle.getString("title");
            ((TextView) view.findViewById(R.id.tv_tips)).setText(title);
            MLVoiceSynthetize.startSynthesize(UM.getApp(),
                    "初次见面，我是小易！为了更好地了解您的身体，先来做一个全套体检吧", onSynthesizerListener, false);
        } else {
            MLVoiceSynthetize.startSynthesize(UM.getApp(),
                    "恭喜您完成问卷，下面让我们进行身体指标检测吧", onSynthesizerListener, false);
        }
        ivGrayBack = view.findViewById(R.id.ivGrayBack);
        ivGrayBack.setOnClickListener(this);


    }


    @Override
    public void onResume() {
        super.onResume();
        Observable
                .timer(12, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(aLong -> {
                    if (fragmentChanged != null) {
                        fragmentChanged.onFragmentChanged(
                                HealthFirstTipsFragment.this, null);
                    }
                });
    }

    @Override
    public void updateData(DetectionData detectionData) {

    }

    class OnSynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
            time = System.currentTimeMillis();
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            Timber.e("语音耗时：" + (System.currentTimeMillis() - time));
//            if (fragmentChanged != null) {
//                fragmentChanged.onFragmentChanged(
//                        HealthFirstTipsFragment.this, null);
//            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivGrayBack) {
            mActivity.finish();
        }
    }

}
