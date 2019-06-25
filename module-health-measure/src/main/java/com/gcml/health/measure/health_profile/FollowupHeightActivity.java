package com.gcml.health.measure.health_profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.fragment.HealthHeightDetectionUiFragment;
import com.gcml.module_blutooth_devices.base.DetectionDataBean;
import com.gcml.module_blutooth_devices.base.ThisFragmentDatas;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
@Route(path = "/health/measure/followup/height")
public class FollowupHeightActivity extends ToolbarBaseActivity implements ThisFragmentDatas {
    private FrameLayout mFlContainer;
    private HealthHeightDetectionUiFragment heightDetectionUiFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_height);
        initView();
        mTitleText.setText("身 高 检 测");
        if (heightDetectionUiFragment == null) {
            heightDetectionUiFragment = new HealthHeightDetectionUiFragment();
        }
        heightDetectionUiFragment.setOnThisFragmentDataChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, heightDetectionUiFragment).commitAllowingStateLoss();
    }

    private void initView() {
        mFlContainer = (FrameLayout) findViewById(R.id.fl_container);
    }

    @Override
    public void data(DetectionDataBean detectionDataBean) {
        Routerfit.setResult(Activity.RESULT_OK, detectionDataBean);
        finish();
    }
}
