package com.gcml.health.measure.first_diagnosis.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gcml.health.measure.R;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.data.TimeCountDownUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 *
 */
public class HealthFirstTipsFragment extends BluetoothBaseFragment implements View.OnClickListener {


    private ITimeCountListener timeCountListener;
    private ImageView ivGrayBack;

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
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                getString(R.string.health_measure_first_detect_tips), false);
        timeCountListener = new ITimeCountListener();
        TimeCountDownUtils.getInstance().create(3000, 1000,
                timeCountListener);
        TimeCountDownUtils.getInstance().start();
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
        TimeCountDownUtils.getInstance().cancelAll();
        timeCountListener = null;
    }
}
