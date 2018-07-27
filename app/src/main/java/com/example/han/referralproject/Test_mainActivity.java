package com.example.han.referralproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.activity.SelectXuetangTimeActivity;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;
import com.example.han.referralproject.xindian.XinDianDetectActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.kk.taurus.playerbase.entity.DataSource;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Test_mainActivity extends BaseActivity implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    @BindView(R.id.ll_xueya)
    LinearLayout llXueya;
    @BindView(R.id.ll_xueyang)
    LinearLayout llXueyang;
    @BindView(R.id.ll_tiwen)
    LinearLayout llTiwen;
    @BindView(R.id.ll_xuetang)
    LinearLayout llXuetang;
    @BindView(R.id.cl_1)
    ConstraintLayout cl1;
    @BindView(R.id.ll_xindian)
    LinearLayout llXindian;
    @BindView(R.id.ll_tizhong)
    LinearLayout llTizhong;
    @BindView(R.id.ll_san)
    LinearLayout llSan;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.cl_2)
    ConstraintLayout cl2;
    private long lastClickTime = 0;
    private boolean isTest;
    private int measureType;

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        if (isTest) {
            backMainActivity();
        }
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main2);
        ButterKnife.bind(this);
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
        setEnableListeningLoop(false);

        speak(R.string.tips_test);

    }

    @Override
    public void onClick(View v) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            Intent intent = new Intent();
            Uri uri;
            switch (v.getId()) {
                case R.id.ll_xueya:
                    measureType = IPresenter.MEASURE_BLOOD_PRESSURE;
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueya);
                    MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血压测量演示视频");
//                    intent.setClass(mContext, DetectActivity.class);
//                    intent.putExtra("type", "xueya");
//                    startActivity(intent);
                    break;
                case R.id.ll_xueyang:
                    measureType = IPresenter.MEASURE_BLOOD_OXYGEN;
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xueyang);
                    MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血氧测量演示视频");
//                    intent.setClass(getApplicationContext(), DetectActivity.class);
//                    intent.putExtra("type", "xueyang");
//                    startActivity(intent);
                    break;
                case R.id.ll_tiwen:
                    measureType = IPresenter.MEASURE_TEMPERATURE;
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
                    MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "耳温测量演示视频");
//                    intent.setClass(mContext, DetectActivity.class);
//                    intent.putExtra("type", "wendu");
//                    startActivity(intent);
                    break;
                case R.id.ll_xuetang:
                    measureType = IPresenter.MEASURE_BLOOD_SUGAR;
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
                    MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血糖测量演示视频");
//                    intent.setClass(getApplicationContext(), SelectXuetangTimeActivity.class);
//                    intent.putExtra("type", "xuetang");
//                    startActivity(intent);
                    break;
                case R.id.ll_xindian:
                    measureType = IPresenter.MEASURE_ECG;
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian);
                    MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "心电测量演示视频");
//                    intent.setClass(mContext, XinDianDetectActivity.class);
//                    startActivity(intent);
                    break;
                case R.id.ll_san:
                    measureType = IPresenter.MEASURE_OTHERS;
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_sanheyi);
                    MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "三合一测量演示视频");
//                    intent.setClass(mContext, SelectXuetangTimeActivity.class);
//                    intent.putExtra("type", "sanheyi");
//                    startActivity(intent);
                    break;
                case R.id.ll_tizhong://体重
                    measureType = IPresenter.MEASURE_WEIGHT;
                    intent.setClass(mContext, DetectActivity.class);
                    intent.putExtra("type", "tizhong");
                    startActivity(intent);
                    break;
                case R.id.ll_more://敬请期待
                    ToastUtils.showShort("敬请期待");
                    break;
            }
//            intent.setClass(this, AllMeasureActivity.class);
//            switch (v.getId()) {
//                case R.id.ll_xueya:
//                    intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_PRESSURE);
//                    break;
//                case R.id.ll_xueyang:
//                    intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_OXYGEN);
//                    break;
//                case R.id.ll_tiwen:
//                    intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_TEMPERATURE);
//                    break;
//                case R.id.ll_xuetang:
//                    intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_SUGAR);
//                    break;
//                case R.id.ll_tizhong:
//                    intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_WEIGHT);
//                    break;
//                case R.id.ll_xindian:
//                    intent.putExtra(IPresenter.MEASURE_TYPE,IPresenter.MEASURE_ECG);
//                    break;
//                case R.id.ll_more://指纹
//                    intent.putExtra(IPresenter.MEASURE_TYPE,IPresenter.CONTROL_FINGERPRINT);
//                    break;
//            }
//            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MeasureVideoPlayActivity.REQUEST_PALY_VIDEO) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                switch (measureType) {
                    case IPresenter.MEASURE_BLOOD_PRESSURE:
                        intent.setClass(mContext, DetectActivity.class);
                        intent.putExtra("type", "xueya");
                        break;
                    case IPresenter.MEASURE_BLOOD_OXYGEN:
                        intent.setClass(getApplicationContext(), DetectActivity.class);
                        intent.putExtra("type", "xueyang");
                        break;
                    case IPresenter.MEASURE_BLOOD_SUGAR:
                        intent.setClass(getApplicationContext(), SelectXuetangTimeActivity.class);
                        intent.putExtra("type", "xuetang");
                        break;
                    case IPresenter.MEASURE_TEMPERATURE:
                        intent.setClass(mContext, DetectActivity.class);
                        intent.putExtra("type", "wendu");
                        break;
                    case IPresenter.MEASURE_ECG:
                        intent.setClass(mContext, XinDianDetectActivity.class);
                        break;
                    case IPresenter.MEASURE_OTHERS:
                        intent.setClass(mContext, SelectXuetangTimeActivity.class);
                        intent.putExtra("type", "sanheyi");
                        break;
                }
                startActivity(intent);
            }
        }
    }
}
