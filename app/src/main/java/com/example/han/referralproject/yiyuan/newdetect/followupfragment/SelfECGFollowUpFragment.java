package com.example.han.referralproject.yiyuan.newdetect.followupfragment;

import android.view.View;

import com.example.han.referralproject.single_measure.SelfECGDetectionFragment;

/**
 * Created by lenovo on 2018/11/13.
 */

public class SelfECGFollowUpFragment extends SelfECGDetectionFragment{
    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
    }


    @Override
    protected void onMeasureFinished(String... results) {
        super.onMeasureFinished(results);
    }

    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, null);
        }
    }
}
