package com.example.han.referralproject.inquiry.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.utils.UM;
import com.gcml.module_blutooth_devices.bloodpressure.BloodpressureFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * Created by lenovo on 2018/11/12.
 */

public class HypertensionFollowUpFragment extends BloodpressureFragment {

    private boolean isOnPause;

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause = true;
    }

    Bundle data = new Bundle();

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3 && !isOnPause) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "主人，您本次测量高压" + results[0] + ",低压" + results[1] + ",脉搏" + results[2], false);
            int highPressure = Integer.parseInt(results[0]);
            int lowPressure = Integer.parseInt(results[1]);
            int pulse = Integer.parseInt(results[2]);
            data.putString("highPressure", highPressure + "");
            data.putString("lowPressure", lowPressure + "");
            data.putString("pulse", pulse + "");
            uploadXueyaResult(highPressure, lowPressure, pulse, true, null);
        }
    }

    /**
     * 上传血压的测量结果
     */
    private void uploadXueyaResult(final int getNew, final int down, final int maibo, final boolean status, final Fragment fragment) {
        DataInfoBean info = new DataInfoBean();
        info.high_pressure = getNew;
        info.low_pressure = down;
        info.pulse = maibo;
        if (status) {
            info.upload_state = true;
        }
        NetworkApi.postData(info, response -> {
        }, message -> {
        });
    }

    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, data);
        }
    }
}
