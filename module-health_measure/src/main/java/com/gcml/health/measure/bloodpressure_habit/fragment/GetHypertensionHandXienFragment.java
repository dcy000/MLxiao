package com.gcml.health.measure.bloodpressure_habit.fragment;

import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiXienFragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/6 11:48
 * created by:gzq
 * description:TODO
 */
public class GetHypertensionHandXienFragment extends HealthBloodDetectionUiXienFragment {
    @Override
    public void onStart() {
        super.onStart();
//        mBtnHealthHistory.setVisibility(View.GONE);
        mBtnHealthHistory.setText("一键检测");
        setBtnClickableState(true);
        onResume=false;
    }

    @Override
    protected void uploadHandStateFinished() {
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(GetHypertensionHandXienFragment.this, null);
        }
    }

    @Override
    protected void clickHealthHistory(View view) {
        mBtnHealthHistory.setClickable(false);
        mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
        super.dealLogic();
    }
}
