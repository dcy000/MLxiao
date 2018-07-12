package com.example.han.referralproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.activity.BaseActivity;
import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.gcml.module_blutooth_devices.utils.ToastTool;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.fingerprint_devices.Fingerpint_Fragment;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;

public class AllMeasureActivity extends BaseActivity {
    private BaseFragment baseFragment;
    private int measure_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_measure);
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.icon_refresh);
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        measure_type = getIntent().getIntExtra(IPresenter.MEASURE_TYPE, -1);
        switch (measure_type) {
            case IPresenter.MEASURE_TEMPERATURE://体温测量
                if (baseFragment == null) {
                    baseFragment = new Temperature_Fragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                if (baseFragment == null) {
                    baseFragment = new Bloodpressure_Fragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                if (baseFragment == null) {
                    baseFragment = new Bloodsugar_Fragment();
                }
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                if (baseFragment == null) {
                    baseFragment = new Bloodoxygen_Fragment();
                }
                break;
            case IPresenter.MEASURE_WEIGHT://体重
                if (baseFragment == null) {
                    baseFragment = new Weight_Fragment();
                }
                break;
            case IPresenter.MEASURE_ECG:
                if (baseFragment == null) {
                    baseFragment = new ECG_Fragment();
                }
                break;
            case IPresenter.MEASURE_OTHERS://三合一
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                if (baseFragment == null) {
                    baseFragment = new Fingerpint_Fragment();
                }
                break;
        }
        fragmentTransaction.replace(R.id.frame, baseFragment).commit();
        if (baseFragment != null) {
            baseFragment.setOnDealVoiceAndJumpListener(new DealVoiceAndJump() {
                @Override
                public void updateVoice(String voice) {
                    speak(voice);
                }

                @Override
                public void jump2HealthHistory(int measureType) {
                    switch (measureType){
                        case IPresenter.MEASURE_TEMPERATURE://体温测量
                            HealthRecordActivity.startActivity(AllMeasureActivity.this,HealthRecordActivity.class,0);
                            break;
                        case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                            HealthRecordActivity.startActivity(AllMeasureActivity.this,HealthRecordActivity.class,1);
                            break;
                        case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                            HealthRecordActivity.startActivity(AllMeasureActivity.this,HealthRecordActivity.class,2);
                            break;
                        case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                            HealthRecordActivity.startActivity(AllMeasureActivity.this,HealthRecordActivity.class,3);
                            break;
                        case IPresenter.MEASURE_WEIGHT://体重
                            HealthRecordActivity.startActivity(AllMeasureActivity.this,HealthRecordActivity.class,8);
                            break;
                        case IPresenter.MEASURE_ECG:
                            HealthRecordActivity.startActivity(AllMeasureActivity.this,HealthRecordActivity.class,7);
                            break;
                        case IPresenter.MEASURE_OTHERS://三合一
                            break;
                    }

                }

                @Override
                public void jump2DemoVideo(int measureType) {

                }
            });
        }
    }

    @Override
    protected void backMainActivity() {
        switch (measure_type) {
            case IPresenter.MEASURE_TEMPERATURE://体温测量
                SPUtil.remove(this, SPUtil.SP_SAVE_TEMPERATURE);
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                SPUtil.remove(this, SPUtil.SP_SAVE_BLOODPRESSURE);
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                SPUtil.remove(this, SPUtil.SP_SAVE_BLOODSUGAR);
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                SPUtil.remove(this, SPUtil.SP_SAVE_BLOODOXYGEN);
                break;
            case IPresenter.MEASURE_WEIGHT://体重
                SPUtil.remove(this, SPUtil.SP_SAVE_WEIGHT);
                break;
            case IPresenter.MEASURE_ECG:
                SPUtil.remove(this, SPUtil.SP_SAVE_ECG);
                break;
            case IPresenter.MEASURE_OTHERS://三合一
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                SPUtil.remove(this, SPUtil.SP_SAVE_FINGERPRINT);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseFragment = null;
    }
}
