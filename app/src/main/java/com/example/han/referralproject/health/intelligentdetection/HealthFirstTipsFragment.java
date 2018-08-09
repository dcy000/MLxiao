package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.gcml.lib_utils.data.TimeCountDownUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 *
 */
public class HealthFirstTipsFragment extends BluetoothBaseFragment {


    public HealthFirstTipsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int initLayout() {
        return R.layout.health_fragment_first_tips;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

    }


    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(MyApplication.getInstance(),
                getString(R.string.health_first_detect_tips), false);
        TimeCountDownUtils.getInstance().create(3000, 1000,
                new TimeCountDownUtils.TimeCountListener() {
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
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        MLVoiceSynthetize.stop();
        TimeCountDownUtils.getInstance().cancelAll();
    }
}
