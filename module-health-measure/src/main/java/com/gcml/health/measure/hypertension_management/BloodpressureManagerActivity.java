package com.gcml.health.measure.hypertension_management;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserEntity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.CustomDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionOnlyOneFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:52
 * created by:gzq
 * description:提供给高血压管理入口进入的血压测量界面
 */
@Route(path = "/health/measure/bloodpressure/manager")
public class BloodpressureManagerActivity extends BaseManagementActivity {
    private String fromActivity;
    private String toActivity;
    private int detectionAcountDay;

    @Override
    protected void dealLogic() {
        fromActivity = getIntent().getStringExtra("fromActivity");
        toActivity = getIntent().getStringExtra("toActivity");
        detectionAcountDay = getIntent().getIntExtra("DetectionAcountDay", 3);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
        jump2MeasureVideoPlayActivity(uri, "血压测量演示视频");
        super.dealLogic();
    }

    private void initFragment() {
        mTitleText.setText("血 压 测 量");
        measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        baseFragment = new HealthBloodDetectionOnlyOneFragment();
        if (detectionAcountDay < 2) {
            Bundle bundle = new Bundle();
            bundle.putString("button", "完  成");
            baseFragment.setArguments(bundle);
        }
        baseFragment.setOnDealVoiceAndJumpListener(this);
        baseFragment.setOnDealVoiceAndJumpListener(this);
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
        //点击了下一步
        if (TextUtils.isEmpty(fromActivity)) return;
        if (fromActivity.equals("SlowDiseaseManagementActivity")) {
            if (detectionAcountDay >= 2) {
                //TODO:此处应该再请求一次(ZZB/api/healthMonitor/hypertension/diagnose/{userId}/)这个接口，并判断平均血压的高低
                Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureNormal");
            } else {
                showEndDialog();
            }
        } else if (fromActivity.equals("DetecteTipActivity")) {
            if (TextUtils.isEmpty(toActivity)) return;
            if (toActivity.equals("WeightManagerActivity")) {
                Routerfit.register(AppRouter.class).skipWeightManagerActivity("BloodpressureManagerActivity", "TreatmentPlanActivity");
            } else if (toActivity.equals("TreatmentPlanActivity")) {
                Routerfit.register(AppRouter.class).skipTreatmentPlanActivity();
            }
        } else if (fromActivity.equals("ShowMeasureBloodpressureResultFragment")) {
            if (TextUtils.isEmpty(toActivity)) return;
            if (toActivity.equals("TreatmentPlanActivity")) {
                Routerfit.register(AppRouter.class).skipTreatmentPlanActivity();
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

    private void showEndDialog() {
        new CustomDialog(this).builder()
                .setImg(0)
                .setMsg("您已完成今日血压测量，为了更好的了解您的情况，建议完成为您量身定制的每日任务")
                .setPositiveButton("每日任务", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toTask();
                    }
                }).show();
    }

    private void toTask() {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity().subscribeOn(Schedulers.io())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                            ToastUtils.showShort("请先去个人中心完善体重和身高信息");
                            MLVoiceSynthetize.startSynthesize(UM.getApp(),
                                    "请先去个人中心完善体重和身高信息");
                        } else {
                            Routerfit.register(AppRouter.class).getTaskProvider()
                                    .isTaskHealth()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new io.reactivex.observers.DefaultObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            Routerfit.register(AppRouter.class).skipTaskActivity("MLMain");

                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            if (throwable instanceof NullPointerException) {
                                                Routerfit.register(AppRouter.class).skipTaskActivity("MLMain");
                                            } else {
                                                Routerfit.register(AppRouter.class).skipTaskComplyActivity();
                                            }
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    }
                });
    }
}
