package com.gcml.health.measure.hypertension_management;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCResultActions;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodpressureFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:TODO
 */
public class BloodpressureManagerActivity extends BaseManagementActivity{
    public static void startActivity(Context context){
        Intent intent=new Intent(context,BloodpressureManagerActivity.class);
        if (context instanceof Application){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
    @Override
    protected void dealLogic() {
        measure_type= IPresenter.MEASURE_BLOOD_PRESSURE;
        SingleMeasureBloodpressureFragment singleMeasureBloodpressureFragment = new SingleMeasureBloodpressureFragment();
        singleMeasureBloodpressureFragment.getVideoDemoView().setVisibility(View.GONE);
        singleMeasureBloodpressureFragment.getHealthRecordView().setText("下一步");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,singleMeasureBloodpressureFragment).commit();
        super.dealLogic();
    }

    @Override
    public void jump2HealthHistory(int measureType) {
        //点击了下一步
        CCResultActions.onCCResultAction(ResultAction.MEASURE_SUCCESS);
    }
}
