package com.gcml.health.measure.bloodpressure_habit.fragment;

import android.view.View;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/6 11:48
 * created by:gzq
 * description:TODO
 */
public class GetHypertensionHandFragment extends HealthBloodDetectionUiFragment {
    @Override
    public void onStart() {
        super.onStart();
        mHistory1.setVisibility(View.GONE);
    }

    @Override
    protected void uploadHandStateFinished() {
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(GetHypertensionHandFragment.this, null);
        }
    }
}
