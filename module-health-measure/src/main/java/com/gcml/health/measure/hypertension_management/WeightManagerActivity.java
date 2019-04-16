package com.gcml.health.measure.hypertension_management;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gcml.common.data.AppManager;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCResultActions;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:提供给高血压管理入口进入的体重测量界面
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
        mTitleText.setText(R.string.title_weight_detection);
        measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        baseFragment = new HealthWeightDetectionUiFragment();
        baseFragment.setOnDealVoiceAndJumpListener(this);
        baseFragment.setOnFragmentChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, baseFragment).commit();
        super.dealLogic();
    }

    @Override
    protected void untieDevice() {
        baseFragment.autoConnect();
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        //点击了下一步
        CCResultActions.onCCResultAction(ResultAction.MEASURE_SUCCESS);
//        CC.obtainBuilder("com.gcml.old.finishAll").build().callAsync();
        AppManager.getAppManager().finishAllActivity();
        finish();
    }
}
