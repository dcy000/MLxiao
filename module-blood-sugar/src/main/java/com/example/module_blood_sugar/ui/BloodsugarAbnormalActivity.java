package com.example.module_blood_sugar.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.module_blood_sugar.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_bluetooth.common.HealthMeasureAbnormalBaseFragment;
import com.gzq.lib_bluetooth.common.HealthMeasureChooseAbnormalReason;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/10 14:56
 * created by:gzq
 * description:TODO
 */
public class BloodsugarAbnormalActivity extends ToolbarBaseActivity implements HealthMeasureChooseAbnormalReason {
    private String firstTitle = "主人，您的测量数据与标准值相差较大，您是否存在以下情况：";
    public static final String KEY_MEASURE_TYPE = "measureType";
    private HealthMeasureAbnormalBaseFragment baseFragment;
    public static final String KEY_HAS_ABNIRMAL_REASULT = "has_abnormal_reasult";

    public static void startActivity(Object host, int measureType, int requestCode) {
        if (host instanceof Activity) {
            Intent intent = new Intent(((Activity) host), BloodsugarAbnormalActivity.class);
            intent.putExtra(KEY_MEASURE_TYPE, measureType);
            ((Activity) host).startActivityForResult(intent, requestCode);
            Timber.e("BloodsugarAbnormalActivity：activity");
        } else if (host instanceof Fragment) {
            Intent intent = new Intent(((Fragment) host).getContext(), BloodsugarAbnormalActivity.class);
            intent.putExtra(KEY_MEASURE_TYPE, measureType);
            ((Fragment) host).startActivityForResult(intent, requestCode);
            Timber.e("BloodsugarAbnormalActivity：Fragment");
        }

    }


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_measure_activity_abnormal;
    }

    @Override
    public void initParams(Intent intentArgument) {
        initFragment();
        MLVoiceSynthetize.startSynthesize(firstTitle);
    }

    @Override
    public void initView() {
        mTitleText.setText("测 量 异 常");
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    private void initFragment() {
        baseFragment = new MeasureXuetangWarningFragment();
        if (baseFragment != null) {
            baseFragment.setOnChooseReason(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, baseFragment).commit();
        } else {
            Timber.e("BloodsugarAbnormalActivity：初始化Fragment失败");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    public void hasReason(int reason) {
        Intent intent = new Intent();
        intent.putExtra(KEY_HAS_ABNIRMAL_REASULT, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void noReason() {
        Intent intent = new Intent();
        intent.putExtra(KEY_HAS_ABNIRMAL_REASULT, false);
        setResult(RESULT_OK, intent);
        finish();
    }
}
