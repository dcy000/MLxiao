package com.gcml.health.measure.hypertension_management;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserEntity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.CustomDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionOnlyOneFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

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

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BloodpressureManagerActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void dealLogic() {
        fromActivity = getIntent().getStringExtra("fromActivity");
        toActivity = getIntent().getStringExtra("toActivity");
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
        jump2MeasureVideoPlayActivity(uri, "血压测量演示视频");
        super.dealLogic();
    }

    private void initFragment() {
        mTitleText.setText("血 压 测 量");
        measure_type = IPresenter.MEASURE_BLOOD_PRESSURE;
        baseFragment = new HealthBloodDetectionOnlyOneFragment();
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
//        CCResultActions.onCCResultAction(ResultAction.MEASURE_SUCCESS);
//        Routerfit.setResult(Routerfit.RESULT_OK,"success");
        if (TextUtils.isEmpty(fromActivity)) return;
        if (fromActivity.equals("SlowDiseaseManagementActivity")) {
            showEndDialog();
        } else if (fromActivity.equals("DetecteTipActivity")) {
            if (TextUtils.isEmpty(toActivity)) return;
            if (toActivity.equals("WeightManagerActivity")) {
                Routerfit.register(AppRouter.class).skipWeightManagerActivity("BloodpressureManagerActivity", "TreatmentPlanActivity");
            } else if (toActivity.equals("TreatmentPlanActivity")) {
                Routerfit.register(AppRouter.class).skipTreatmentPlanActivity();
            }
        }
    }

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        CC.obtainBuilder(CCVideoActions.MODULE_NAME)
                .setActionName(CCVideoActions.SendActionNames.TO_MEASUREACTIVITY)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URI, uri)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URL, null)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_TITLE, title)
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                String resultAction = result.getDataItem(CCVideoActions.ReceiveResultKeys.KEY_EXTRA_CC_CALLBACK);
                switch (resultAction) {
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_BACK:
                        //点击了返回按钮
                        break;
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        initFragment();
                        break;
                    default:
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
//                        CCAppActions.jump2MainActivity();
//                        CC.obtainBuilder("com.app.symptom.check")
//                                .build()
//                                .call();

//                        CC.obtainBuilder("app.hypertension.manager.slow.disease")
//                                .build()
//                                .call();
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
                            CC.obtainBuilder("com.gcml.task.isTask")
                                    .build()
                                    .callAsync(new IComponentCallback() {
                                        @Override
                                        public void onResult(CC cc, CCResult result) {
                                            if (result.isSuccess()) {
                                                CC.obtainBuilder("app.component.task").addParam("startType", "MLMain").build().callAsync();
                                            } else {
                                                CC.obtainBuilder("app.component.task.comply").build().callAsync();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
