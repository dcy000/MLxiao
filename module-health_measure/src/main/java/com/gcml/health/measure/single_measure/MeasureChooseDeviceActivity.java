package com.gcml.health.measure.single_measure;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.cc.CCVideoActions;
import com.gcml.health.measure.ecg.XinDianDetectActivity;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Calendar;

public class MeasureChooseDeviceActivity extends ToolbarBaseActivity implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private boolean isTest;
    private int measureType;
    private LinearLayout llXueya;
    private LinearLayout llXueyang;
    private LinearLayout llTiwen;
    private LinearLayout llXuetang;
    private ConstraintLayout cl1;
    private LinearLayout llXindian;
    private LinearLayout llTizhong;
    private LinearLayout llSan;
    private LinearLayout llMore;
    private ConstraintLayout cl2;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MeasureChooseDeviceActivity.class);
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
        }
        finish();
    }

    /**
     * 返回到主页面
     */
    @Override
    protected void backMainActivity() {
        CCAppActions.jump2MainActivity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_choose_device);
        mTitleText.setText("健 康 检 测");
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        isTest = getIntent().getBooleanExtra("isTest", false);

        llXueya.setOnClickListener(this);
        llXueyang.setOnClickListener(this);
        llXuetang.setOnClickListener(this);
        llXindian.setOnClickListener(this);
        llTizhong.setOnClickListener(this);
        llTiwen.setOnClickListener(this);
        llSan.setOnClickListener(this);
        llMore.setOnClickListener(this);

        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，请选择你需要测量的项目", false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;

            Intent intent = new Intent();
            Uri uri;
            int i = v.getId();
            if (i == R.id.ll_xueya) {
                measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
                jump2MeasureVideoPlayActivity(uri,"血压测量演示视频");
            } else if (i == R.id.ll_xueyang) {
                measureType = IPresenter.MEASURE_BLOOD_OXYGEN;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
                jump2MeasureVideoPlayActivity(uri,"血氧测量演示视频");
            } else if (i == R.id.ll_tiwen) {
                measureType = IPresenter.MEASURE_TEMPERATURE;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
                jump2MeasureVideoPlayActivity(uri,"耳温测量演示视频");
            } else if (i == R.id.ll_xuetang) {
                measureType = IPresenter.MEASURE_BLOOD_SUGAR;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
                jump2MeasureVideoPlayActivity(uri,"血糖测量演示视频");
            } else if (i == R.id.ll_xindian) {
                measureType = IPresenter.MEASURE_ECG;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
                jump2MeasureVideoPlayActivity(uri,"心电测量演示视频");
            } else if (i == R.id.ll_san) {
                measureType = IPresenter.MEASURE_OTHERS;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                jump2MeasureVideoPlayActivity(uri,"三合一测量演示视频");
            } else if (i == R.id.ll_tizhong) {
                //体重
                measureType = IPresenter.MEASURE_WEIGHT;
                AllMeasureActivity.startActivity(this, measureType);

            } else if (i == R.id.ll_more) {
                //敬请期待
                ToastUtils.showShort("敬请期待");

            } else {
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
    private void aferVideo(){
        if (measureType == IPresenter.MEASURE_ECG) {
            XinDianDetectActivity.startActivity(this,
                    MeasureChooseDeviceActivity.class.getSimpleName());
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, AllMeasureActivity.class);
        switch (measureType) {

            case IPresenter.MEASURE_BLOOD_PRESSURE:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_PRESSURE);
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_OXYGEN);
                break;
            case IPresenter.MEASURE_TEMPERATURE:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_TEMPERATURE);
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_SUGAR);
                break;
            case IPresenter.MEASURE_WEIGHT:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_WEIGHT);
                break;
            case IPresenter.MEASURE_OTHERS:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_OTHERS);
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                //指纹
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.CONTROL_FINGERPRINT);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
    private void initView() {
        llXueya = (LinearLayout) findViewById(R.id.ll_xueya);
        llXueyang = (LinearLayout) findViewById(R.id.ll_xueyang);
        llTiwen = (LinearLayout) findViewById(R.id.ll_tiwen);
        llXuetang = (LinearLayout) findViewById(R.id.ll_xuetang);
        cl1 = (ConstraintLayout) findViewById(R.id.cl_1);
        llXindian = (LinearLayout) findViewById(R.id.ll_xindian);
        llTizhong = (LinearLayout) findViewById(R.id.ll_tizhong);
        llSan = (LinearLayout) findViewById(R.id.ll_san);
        llMore = (LinearLayout) findViewById(R.id.ll_more);
        cl2 = (ConstraintLayout) findViewById(R.id.cl_2);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
