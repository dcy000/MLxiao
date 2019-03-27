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

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

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
            if (!TextUtils.isEmpty(servicePackage)&&servicePackage.equals("1") || servicePackage.equals("2")) {
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
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "请选择你需要测量的项目", false);
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
        CC.obtainBuilder(CCVideoActions.MODULE_NAME)
                .setActionName(CCVideoActions.SendActionNames.TO_MEASUREACTIVITY)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URI, uri)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_URL, null)
                .addParam(CCVideoActions.SendKeys.KEY_EXTRA_TITLE, title)
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                String resultAction = result.getDataItem(CCVideoActions.ReceiveResultKeys.KEY_EXTRA_CC_CALLBACK);
                if (resultAction == null) {
                    aferVideo();
                }
                switch (resultAction) {
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_BACK:
                        //点击了返回按钮
                        break;
                    case CCVideoActions.ReceiveResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                        aferVideo();
                        break;
                    case CCVideoActions.ReceiveResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        aferVideo();
                        break;
                    default:
                }
            }
        });
    }

    private void aferVideo() {
        Intent intent = new Intent();
        intent.setClass(this, AllMeasureActivity.class);
        intent.putExtra(IPresenter.MEASURE_TYPE, measureType);
        intent.putExtra(IS_FACE_SKIP, getIntent().getBooleanExtra(IS_FACE_SKIP, false));
        startActivity(intent);
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
