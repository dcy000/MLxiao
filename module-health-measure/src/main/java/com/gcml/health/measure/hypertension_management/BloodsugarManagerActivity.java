package com.gcml.health.measure.hypertension_management;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.health.measure.R;
import com.gcml.health.measure.single_measure.fragment.SingleMeasureBloodsugarFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:提供给高血压管理入口进入的血糖测量界面
 */
public class BloodsugarManagerActivity extends BaseManagementActivity {


    private String fromActivity;
    private String toActivity;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BloodsugarManagerActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void dealLogic() {
        fromActivity = getIntent().getStringExtra("fromActivity");
        toActivity = getIntent().getStringExtra("toActivity");
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
        jump2MeasureVideoPlayActivity(uri, "血糖测量演示视频");
        super.dealLogic();
    }

    private void initFragment() {
        mTitleText.setText("血 糖 测 量");
        measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        baseFragment = new SingleMeasureBloodsugarFragment();
        baseFragment.setOnDealVoiceAndJumpListener(this);
        baseFragment.setOnFragmentChangedListener(this);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isOnlyShowBtnHealthRecord", true);
        baseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, baseFragment).commitAllowingStateLoss();
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void untieDevice() {
        baseFragment.autoConnect();
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
    }

    @Override
    public void jump2HealthHistory(int measureType) {
        if (TextUtils.isEmpty(fromActivity)) return;
        if (fromActivity.equals("DetecteTipActivity")){
            if (TextUtils.isEmpty(toActivity)) return;
            if (toActivity.equals("WeightManagerActivity")){
                Routerfit.register(AppRouter.class).skipWeightManagerActivity("BloodsugarManagerActivity","TreatmentPlanActivity");
            }
        }
    }

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        Routerfit.register(AppRouter.class).skipMeasureVideoPlayActivity(uri, null, title, new ActivityCallback() {
            @Override
            public void onActivityResult(int result, Object data) {
                if (result == Activity.RESULT_OK) {
                    if (data == null) return;
                    if (data.toString().equals("pressed_button_skip")) {
                        initFragment();
                    } else if (data.toString().equals("video_play_end")) {
                        initFragment();
                    }
                } else if (result == Activity.RESULT_CANCELED) {
                }
            }
        });
    }

}
