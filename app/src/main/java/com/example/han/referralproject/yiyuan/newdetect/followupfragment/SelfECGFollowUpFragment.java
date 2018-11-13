package com.example.han.referralproject.yiyuan.newdetect.followupfragment;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.single_measure.SelfECGDetectionFragment;

/**
 * Created by lenovo on 2018/11/13.
 */

public class SelfECGFollowUpFragment extends SelfECGDetectionFragment {

    private Bundle data = new Bundle();

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
    }


    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, data);
        }
    }

    @Override
    public void uploadEcg(int ecg, int heartRate) {
        data.putString("ecg", ecg + "");
        data.putString("heartRate", heartRate + "");
        super.uploadEcg(ecg, heartRate);
    }
}
