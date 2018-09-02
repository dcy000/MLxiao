package com.gcml.health.measure.hypertension_management;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCResultActions;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodpressureFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:TODO
 */
public class WeightManagerActivity extends BaseManagementActivity {
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WeightManagerActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void dealLogic() {
        mTitleText.setText("体 重 测 量");
        measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        HealthWeightDetectionUiFragment healthWeightDetectionUiFragment = new HealthWeightDetectionUiFragment();
        healthWeightDetectionUiFragment.setOnDealVoiceAndJumpListener(this);
        healthWeightDetectionUiFragment.setOnFragmentChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, healthWeightDetectionUiFragment).commit();
        super.dealLogic();
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        //点击了下一步
        CCResultActions.onCCResultAction(ResultAction.MEASURE_SUCCESS);
        CC.obtainBuilder("com.gcml.old.finishAll").build().callAsync();
        finish();
    }
}
