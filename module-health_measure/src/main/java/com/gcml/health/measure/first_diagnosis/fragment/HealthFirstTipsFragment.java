package com.gcml.health.measure.first_diagnosis.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gcml.health.measure.R;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.data.TimeCountDownUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 *
 */
public class HealthFirstTipsFragment extends BluetoothBaseFragment implements View.OnClickListener {


//    private ITimeCountListener timeCountListener;
    private ImageView ivGrayBack;
    private OnSynthesizerListener onSynthesizerListener;

    public HealthFirstTipsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_first_tips;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        ivGrayBack = view.findViewById(R.id.ivGrayBack);
        ivGrayBack.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (onSynthesizerListener==null){
            onSynthesizerListener = new OnSynthesizerListener();
        }
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                getString(R.string.health_measure_first_detect_tips), onSynthesizerListener,false);
//        timeCountListener = new ITimeCountListener();
//        TimeCountDownUtils.getInstance().create(3000, 1000,
//                timeCountListener);
//        TimeCountDownUtils.getInstance().start();
    }
    class  OnSynthesizerListener implements SynthesizerListener{

        @Override
        public void onSpeakBegin() {

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
            getActivity().finish();
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

    @Override
    public void onStop() {
        super.onStop();
        MLVoiceSynthetize.stop();
//        TimeCountDownUtils.getInstance().cancelAll();
//        timeCountListener = null;
    }
}
