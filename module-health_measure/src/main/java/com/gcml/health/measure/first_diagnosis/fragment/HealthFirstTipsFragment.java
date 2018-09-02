package com.gcml.health.measure.first_diagnosis.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.health.measure.R;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.data.TimeCountDownUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

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
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                    "主人，做一个风险评估吧", onSynthesizerListener,false);
        }else{
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                    "初次见面，和小E一起做个全套体检吧", onSynthesizerListener, false);
        }
        ivGrayBack = view.findViewById(R.id.ivGrayBack);
        ivGrayBack.setOnClickListener(this);



    }


    @Override
    public void onResume() {
        super.onResume();

//        timeCountListener = new ITimeCountListener();
//        TimeCountDownUtils.getInstance().create(3000, 1000,
//                timeCountListener);
//        TimeCountDownUtils.getInstance().start();
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
            if (fragmentChanged != null) {
                fragmentChanged.onFragmentChanged(
                        HealthFirstTipsFragment.this, null);
            }
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

    class ITimeCountListener implements TimeCountDownUtils.TimeCountListener {

        @Override
        public void onTick(long millisUntilFinished, String tag) {

        }

        @Override
        public void onFinish(String tag) {
            if (fragmentChanged != null) {
                fragmentChanged.onFragmentChanged(
                        HealthFirstTipsFragment.this, null);
            }
        }
    }
}
