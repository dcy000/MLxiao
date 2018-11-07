package com.example.han.referralproject;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_video.measure.MeasureVideoPlayActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class MeasureChooseDeviceActivity extends BaseActivity implements View.OnClickListener {
    private boolean isTest;
    private int measureType;
    public static final String IS_FACE_SKIP = "isFaceSkip";
    private LinearLayout mLlXueya;
    private LinearLayout mLlXueyang;
    private LinearLayout mLlTiwen;
    private LinearLayout mLlXuetang;
    private LinearLayout mLlXindian;
    private LinearLayout mLlTizhong;
    private LinearLayout mLlSan;
    private LinearLayout mLlMore;


    public static void startActivity(Context context, boolean isFaceSkip) {
        Intent intent = new Intent(context, MeasureChooseDeviceActivity.class);
        intent.putExtra(IS_FACE_SKIP, isFaceSkip);
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
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main2);
        mTitleText.setText("健 康 检 测");
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        isTest = getIntent().getBooleanExtra("isTest", false);
        MLVoiceSynthetize.startSynthesize(MyApplication.getInstance(), "主人，请选择你需要测量的项目", false);
    }

    @Override
    public void onClick(View v) {
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
            measureType = IPresenter.MEASURE_OTHERS;
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
            jump2MeasureVideoPlayActivity(uri, "三合一测量演示视频");
        } else if (i == R.id.ll_tizhong) {
            //体重
            measureType = IPresenter.MEASURE_WEIGHT;
            AllMeasureActivity.startActivity(this, measureType);

        } else if (i == R.id.ll_more) {
            measureType = IPresenter.MEASURE_HAND_RING;
            AllMeasureActivity.startActivity(this, measureType);

        } else {
        }
    }

    /**
     * 跳转到MeasureVideoPlayActivity
     */
    private void jump2MeasureVideoPlayActivity(Uri uri, String title) {
        MeasureVideoPlayActivity.startForResultActivity(this, uri, null, title, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (data != null) {
                String result = data.getStringExtra("result");
                switch (result) {
                    case MeasureVideoPlayActivity.SendResultActionNames.PRESSED_BUTTON_BACK:
                        //点击了返回按钮
                        break;
                    case MeasureVideoPlayActivity.SendResultActionNames.PRESSED_BUTTON_SKIP:
                        //点击了跳过按钮
                        aferVideo();
                        break;
                    case MeasureVideoPlayActivity.SendResultActionNames.VIDEO_PLAY_END:
                        //视屏播放结束
                        aferVideo();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void aferVideo() {
        Intent intent = new Intent();
        intent.setClass(this, AllMeasureActivity.class);
        intent.putExtra(IPresenter.MEASURE_TYPE, measureType);
        intent.putExtra(IS_FACE_SKIP, getIntent().getBooleanExtra(IS_FACE_SKIP, false));
        startActivity(intent);
    }

    private void initView() {
        mLlXueya = (LinearLayout) findViewById(R.id.ll_xueya);
        mLlXueya.setOnClickListener(this);
        mLlXueyang = (LinearLayout) findViewById(R.id.ll_xueyang);
        mLlXueyang.setOnClickListener(this);
        mLlTiwen = (LinearLayout) findViewById(R.id.ll_tiwen);
        mLlTiwen.setOnClickListener(this);
        mLlXuetang = (LinearLayout) findViewById(R.id.ll_xuetang);
        mLlXuetang.setOnClickListener(this);
        mLlXindian = (LinearLayout) findViewById(R.id.ll_xindian);
        mLlXindian.setOnClickListener(this);
        mLlTizhong = (LinearLayout) findViewById(R.id.ll_tizhong);
        mLlTizhong.setOnClickListener(this);
        mLlSan = (LinearLayout) findViewById(R.id.ll_san);
        mLlSan.setOnClickListener(this);
        mLlMore = (LinearLayout) findViewById(R.id.ll_more);
        mLlMore.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
