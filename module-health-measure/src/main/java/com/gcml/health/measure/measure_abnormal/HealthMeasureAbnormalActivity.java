package com.gcml.health.measure.measure_abnormal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/10 14:56
 * created by:gzq
 * description:TODO
 */
public class HealthMeasureAbnormalActivity extends ToolbarBaseActivity implements HealthMeasureChooseAbnormalReason {
    private String firstTitle = "您的测量数据与标准值相差较大，您是否存在以下情况：";
    public static final String KEY_MEASURE_TYPE = "measureType";
    private int measureType;
    private HealthMeasureAbnormalBaseFragment baseFragment;
    public static final String KEY_HAS_ABNIRMAL_REASULT="has_abnormal_reasult";
    public static void startActivity(Object host, int measureType,int requestCode) {
        if (host instanceof Activity){
            Intent intent = new Intent(((Activity) host), HealthMeasureAbnormalActivity.class);
            intent.putExtra(KEY_MEASURE_TYPE, measureType);
            ((Activity) host).startActivityForResult(intent,requestCode);
            Timber.e("HealthMeasureAbnormalActivity：activity");
        }else if (host instanceof Fragment){
            Intent intent = new Intent(((Fragment) host).getContext(), HealthMeasureAbnormalActivity.class);
            intent.putExtra(KEY_MEASURE_TYPE, measureType);
            ((Fragment) host).startActivityForResult(intent,requestCode);
            Timber.e("HealthMeasureAbnormalActivity：Fragment");
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_abnormal);
        measureType = getIntent().getIntExtra(KEY_MEASURE_TYPE, -1);
        mTitleText.setText("测 量 异 常");
        MLVoiceSynthetize.startSynthesize(UM.getApp(),firstTitle);
        initFragment();
    }

    private void initFragment() {
        switch (measureType) {
            default:
                Timber.e("HealthMeasureAbnormalActivity：传入的参数不正确");
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE:
                //血压测量
                baseFragment = new MeasureXueyaWarningFragment();
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR:
                baseFragment=new MeasureXuetangWarningFragment();
                break;
        }
        if (baseFragment != null) {
            baseFragment.setOnChooseReason(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, baseFragment).commit();
        }else{
            Timber.e("HealthMeasureAbnormalActivity：初始化Fragment失败");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    public void hasReason(int reason) {
        Intent intent=new Intent();
        intent.putExtra(KEY_HAS_ABNIRMAL_REASULT,true);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void noReason() {
        Intent intent=new Intent();
        intent.putExtra(KEY_HAS_ABNIRMAL_REASULT,false);
        setResult(RESULT_OK,intent);
        finish();
    }
}
