package com.example.han.referralproject.bluetooth_devices;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bluetooth_devices.base.IPresenter;
import com.example.han.referralproject.bluetooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.example.han.referralproject.bluetooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.example.han.referralproject.bluetooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.example.han.referralproject.bluetooth_devices.temperature_devices.Temperature_Fragment;
import com.example.han.referralproject.bluetooth_devices.weight_devices.Weight_Fragment;

public class AllMeasureActivity extends BaseActivity {
    public static final String[] BLOODOXYGEN_BRANDS = {"HUA_DAI_FU", "KANG_TAI"};
    public static final String[] BLOODPRESSURE_BRANDS = {"MAI_LIAN"};
    public static final String[] BLOODSUGAR_BRANDS = {"WEI_YI", "SAN_NUO"};
    public static final String[] TEMPERATURE_BRANDS = {"AI_LI_KANG", "FU_DA_KANG", "MEI_DI_LIAN", "MAI_LIAN"};
    public static final String[] WEIGHT_BRANDS = {"QING_HUA_TONG_FANG", "YI_KE"};
    private FrameLayout frame;
    private int measureType;
    private Bloodsugar_Fragment bloodsugarFragment;
    private Temperature_Fragment temperatureFragment;
    private Bloodpressure_Fragment bloodpressureFragment;
    private Bloodoxygen_Fragment bloodoxygenFragment;
    private Weight_Fragment weightFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_measure);
        initView();
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        switch (measureType) {
            case IPresenter.MEASURE_TEMPERATURE://体温测量
                mTitleText.setText("体温测量");
                if (temperatureFragment == null) {
                    temperatureFragment = new Temperature_Fragment();
                    bundle.putString(IPresenter.BRAND, TEMPERATURE_BRANDS[3]);
                    temperatureFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.frame, temperatureFragment);
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                mTitleText.setText("血压测量");
                if (bloodpressureFragment == null) {
                    bloodpressureFragment = new Bloodpressure_Fragment();
                    bundle.putString(IPresenter.BRAND, BLOODPRESSURE_BRANDS[0]);
                    bloodpressureFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.frame, bloodpressureFragment);
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                mTitleText.setText("血糖测量");
                if (bloodsugarFragment == null) {
                    bloodsugarFragment = new Bloodsugar_Fragment();
                    bundle.putString(IPresenter.BRAND, BLOODSUGAR_BRANDS[1]);
                    bloodsugarFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.frame, bloodsugarFragment);
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                mTitleText.setText("血氧测量");
                if (bloodoxygenFragment == null) {
                    bloodoxygenFragment = new Bloodoxygen_Fragment();
                    bundle.putString(IPresenter.BRAND, BLOODOXYGEN_BRANDS[0]);
                    bloodoxygenFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.frame, bloodoxygenFragment);
                break;
            case IPresenter.MEASURE_WEIGHT://体重
                mTitleText.setText("体重测量");
                if (weightFragment == null) {
                    weightFragment = new Weight_Fragment();
                    bundle.putString(IPresenter.BRAND, WEIGHT_BRANDS[0]);
                    weightFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.frame, weightFragment);
                break;
            case IPresenter.MEASURE_OTHERS://三合一
                mTitleText.setText("三合一");
                break;
        }
        fragmentTransaction.commit();
    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        frame = (FrameLayout) findViewById(R.id.frame);
        Intent intent = getIntent();
        if (intent != null) {
            measureType = intent.getIntExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_PRESSURE);
        }
    }
}
