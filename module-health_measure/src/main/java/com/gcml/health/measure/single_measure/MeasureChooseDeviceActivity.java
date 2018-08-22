package com.gcml.health.measure.single_measure;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.ecg.XinDianDetectActivity;
import com.gcml.health.measure.video.MeasureVideoPlayActivity;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.iflytek.recognition.MLVoiceRecognize;
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
                MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "血压测量演示视频",
                        MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);

            } else if (i == R.id.ll_xueyang) {
                measureType = IPresenter.MEASURE_BLOOD_OXYGEN;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
                MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "血氧测量演示视频",
                        MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);

            } else if (i == R.id.ll_tiwen) {
                measureType = IPresenter.MEASURE_TEMPERATURE;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
                MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "耳温测量演示视频",
                        MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);

            } else if (i == R.id.ll_xuetang) {
                measureType = IPresenter.MEASURE_BLOOD_SUGAR;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
                MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "血糖测量演示视频",
                        MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);

            } else if (i == R.id.ll_xindian) {
                measureType = IPresenter.MEASURE_ECG;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
                MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "心电测量演示视频",
                        MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);

            } else if (i == R.id.ll_san) {
                measureType = IPresenter.MEASURE_OTHERS;
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                MeasureVideoPlayActivity.startActivityForResult(this, uri, null, "三合一测量演示视频",
                        MeasureVideoPlayActivity.REQUEST_PALY_VIDEO);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MeasureVideoPlayActivity.REQUEST_PALY_VIDEO) {
            if (resultCode == RESULT_OK) {
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
        }
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
