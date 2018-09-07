package com.gcml.health.measure.hypertension_management;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gcml.common.data.AppManager;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCResultActions;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionOnlyOneFragment;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodpressureFragment;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:提供给高血压管理入口进入的血压测量界面
 */
public class BloodpressureManagerActivity extends BaseManagementActivity {
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BloodpressureManagerActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void dealLogic() {
        mTitleText.setText("血 压 测 量");
        measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        baseFragment=new HealthBloodDetectionOnlyOneFragment();
        baseFragment.setOnDealVoiceAndJumpListener(this);
        baseFragment.setOnDealVoiceAndJumpListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, baseFragment).commit();
        AppManager.getAppManager().addActivity(this);
        super.dealLogic();
    }

    @Override
    protected void untieDevice() {
        super.untieDevice();
        //血压
        String nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
        SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
        ((Bloodpressure_Fragment) baseFragment).onStop();
        ((Bloodpressure_Fragment) baseFragment).dealLogic();
        clearBluetoothCache(nameAddress);
    }

    @Override
    public void jump2HealthHistory(int measureType) {
        //点击了下一步
        CCResultActions.onCCResultAction(ResultAction.MEASURE_SUCCESS);
    }
}
