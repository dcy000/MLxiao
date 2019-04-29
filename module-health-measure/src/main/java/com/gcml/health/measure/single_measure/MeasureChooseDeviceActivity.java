package com.gcml.health.measure.single_measure;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/health/measure/choose/device")
public class MeasureChooseDeviceActivity extends ToolbarBaseActivity implements View.OnClickListener {
    private boolean isTest;
    private int measureType;
    private LinearLayout llXueya;
    private LinearLayout llXueyang;
    private LinearLayout llTiwen;
    private LinearLayout llXuetang;
    private LinearLayout llXindian;
    private LinearLayout llTizhong;
    private LinearLayout llSan;
    private LinearLayout llMore;
    public static final String IS_FACE_SKIP = "isFaceSkip";
    /**
     * 血糖检测
     */
    private TextView mTvxuetang;
    /**
     * 三合一
     */
    private TextView mTvsanheyi;
    private String servicePackage;
    private String serviceUUID;

    public static void startActivity(Context context, boolean isFaceSkip) {
        Intent intent = new Intent(context, MeasureChooseDeviceActivity.class);
        intent.putExtra(IS_FACE_SKIP, isFaceSkip);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startActivity(Context context, boolean isFaceSkip, String type, String serviceUUid) {
        Intent intent = new Intent(context, MeasureChooseDeviceActivity.class);
        intent.putExtra(IS_FACE_SKIP, isFaceSkip);
        intent.putExtra("ServicePackage", type);
        intent.putExtra("ServicePackageUUID", serviceUUid);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 返回上一页
     */
    @Override
    protected void backLastActivity() {
        if (isTest) {
            backMainActivity();
            finish();
        } else {
            if (servicePackage == null) {
                finish();
                return;
            }
            if ((!TextUtils.isEmpty(servicePackage)) && servicePackage.equals("1") || servicePackage.equals("2")) {
                showQuitDialog(false);
            } else {
                finish();
            }
        }
    }

    /**
     * 返回到主页面
     */
    @Override
    protected void backMainActivity() {
        if (servicePackage == null) {
            super.backMainActivity();
            return;
        }
        if (servicePackage.equals("1") || servicePackage.equals("2")) {
            showQuitDialog(true);
        } else {
//            CCAppActions.jump2MainActivity();
            super.backMainActivity();
        }

    }

    private void showQuitDialog(boolean isMain) {
        new AlertDialog(MeasureChooseDeviceActivity.this)
                .builder()
                .setMsg("退出则消费本次购买的套餐，是否继续退出？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HealthMeasureRepository.cancelServicePackage(serviceUUID)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<Object>() {
                                    @Override
                                    public void onNext(Object o) {
                                        if (isMain) {
//                                            CCAppActions.jump2MainActivity();
                                            MeasureChooseDeviceActivity.super.backMainActivity();
                                        } else {

                                        }

                                        finish();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtils.showShort("取消服务包失败");
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_choose_device);
        mTitleText.setText("健 康 检 测");
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        isTest = getIntent().getBooleanExtra("isTest", false);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请选择你需要测量的项目", false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Uri uri;
        int i = v.getId();
        if (i == R.id.ll_xueya) {
            measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
            jump2MeasureVideoPlayActivity(uri, "血压测量演示视频");
        } else if (i == R.id.ll_xueyang) {
            measureType = IPresenter.MEASURE_BLOOD_OXYGEN;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
            jump2MeasureVideoPlayActivity(uri, "血氧测量演示视频");
        } else if (i == R.id.ll_tiwen) {
            measureType = IPresenter.MEASURE_TEMPERATURE;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
            jump2MeasureVideoPlayActivity(uri, "耳温测量演示视频");
        } else if (i == R.id.ll_xuetang) {
            measureType = IPresenter.MEASURE_BLOOD_SUGAR;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
            jump2MeasureVideoPlayActivity(uri, "血糖测量演示视频");
        } else if (i == R.id.ll_xindian) {
            measureType = IPresenter.MEASURE_ECG;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
            jump2MeasureVideoPlayActivity(uri, "心电测量演示视频");
        } else if (i == R.id.ll_san) {
            measureType = IPresenter.MEASURE_THREE;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
            jump2MeasureVideoPlayActivity(uri, "三合一测量演示视频");
        } else if (i == R.id.ll_tizhong) {
            //体重
            measureType = IPresenter.MEASURE_WEIGHT;
            AllMeasureActivity.startActivity(this, measureType);

        } else if (i == R.id.ll_more) {
//            measureType=IPresenter.MEASURE_HAND_RING;
//            AllMeasureActivity.startActivity(this,measureType);
            ToastUtils.showLong("敬请期待");
//            startActivity(new Intent(this, TestWuhuaqiActivity.class));
        } else {
        }
    }

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        //暂时不需要演示视频
        aferVideo();
//        Routerfit.register(AppRouter.class).skipMeasureVideoPlayActivity(uri, null, title, new ActivityCallback() {
//            @Override
//            public void onActivityResult(int result, Object data) {
//                if (result == Activity.RESULT_OK) {
//                    if (data == null) return;
//                    if (data.toString().equals("pressed_button_skip")) {
//                        aferVideo();
//                    } else if (data.toString().equals("video_play_end")) {
//                        aferVideo();
//                    }
//                } else if (result == Activity.RESULT_CANCELED) {
//                }
//            }
//        });
    }

    private void aferVideo() {
        Routerfit.register(AppRouter.class).skipAllMeasureActivity(
                measureType, getIntent().getBooleanExtra(IS_FACE_SKIP, false),
                servicePackage, serviceUUID);
    }

    private void initView() {
        llXueya = (LinearLayout) findClickView(R.id.ll_xueya);
        llXueyang = (LinearLayout) findClickView(R.id.ll_xueyang);
        llTiwen = (LinearLayout) findClickView(R.id.ll_tiwen);
        llXuetang = (LinearLayout) findClickView(R.id.ll_xuetang);
        llXindian = (LinearLayout) findClickView(R.id.ll_xindian);
        llTizhong = (LinearLayout) findClickView(R.id.ll_tizhong);
        llSan = (LinearLayout) findClickView(R.id.ll_san);
        llMore = (LinearLayout) findClickView(R.id.ll_more);
        mTvxuetang = (TextView) findViewById(R.id.tvxuetang);
        mTvsanheyi = (TextView) findViewById(R.id.tvsanheyi);
        servicePackage = getIntent().getStringExtra("ServicePackage");
        serviceUUID = getIntent().getStringExtra("ServicePackageUUID");
        if (servicePackage != null && servicePackage.equals("1")) {
            //套餐1
            mTvxuetang.setBackgroundResource(R.drawable.health_measure_text_bg_gengduo);
            mTvsanheyi.setBackgroundResource(R.drawable.health_measure_text_bg_gengduo);
            llSan.setClickable(false);
            llXuetang.setClickable(false);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
