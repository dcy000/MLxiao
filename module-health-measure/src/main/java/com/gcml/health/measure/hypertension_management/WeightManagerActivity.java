package com.gcml.health.measure.hypertension_management;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.gcml.common.App;
import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCResultActions;
import com.gcml.health.measure.first_diagnosis.fragment.HealthWeightDetectionUiFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:提供给高血压管理入口进入的体重测量界面
 */
@Route(path = "/health/measure/weight/manager")
public class WeightManagerActivity extends BaseManagementActivity {
    private String fromActivity;
    private String toActivity;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WeightManagerActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void dealLogic() {
        fromActivity = getIntent().getStringExtra("fromActivity");
        toActivity = getIntent().getStringExtra("toActivity");
        mTitleText.setText("体 重 测 量");
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
        if (TextUtils.isEmpty(fromActivity)) return;
        if (fromActivity.equals("BloodpressureManagerActivity") ||
                fromActivity.equals("DetecteTipActivity") ||
                fromActivity.equals("BloodsugarManagerActivity")) {
            if (TextUtils.isEmpty(toActivity)) return;
            if (toActivity.equals("TreatmentPlanActivity")) {
                Routerfit.register(AppRouter.class).skipTreatmentPlanActivity();
            }
        }
        AppManager.getAppManager().finishAllActivity();
        finish();
    }
}
