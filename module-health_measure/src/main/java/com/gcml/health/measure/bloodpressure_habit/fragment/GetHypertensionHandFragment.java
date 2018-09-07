package com.gcml.health.measure.bloodpressure_habit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;
import com.gcml.lib_utils.base.ToolbarBaseActivity;

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
        mBtnHealthHistory.setText("完成");
    }
}
