package com.example.han.referralproject.bluetooth_devices;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bluetooth_devices.base.IPresenter;
import com.example.han.referralproject.bluetooth_devices.bloodsugar_devices.Bloodsugar_Sannuo_Fragment;

public class AllMeasureActivity extends BaseActivity {
    private FrameLayout frame;
    private int measureType;
    private Bloodsugar_Sannuo_Fragment sannuoFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_measure);
        initView();
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (measureType) {
            case IPresenter.MEASURE_TEMPERATURE://体温测量

                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE://血压

                break;
            case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                sannuoFragment = new Bloodsugar_Sannuo_Fragment();
                fragmentTransaction.replace(R.id.frame, sannuoFragment);
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN://血氧

                break;
            case IPresenter.MEASURE_WEIGHT://耳温
                break;
            case IPresenter.MEASURE_OTHERS://三合一

                break;
        }
        fragmentTransaction.commit();
    }

    private void initView() {
        frame = (FrameLayout) findViewById(R.id.frame);
        Intent intent = getIntent();
        if (intent != null) {
            measureType = intent.getIntExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_NULL);
        }
    }
}
