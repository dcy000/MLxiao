package com.example.han.referralproject.hypertensionmanagement.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.intelligentdetection.HealthBloodDetectionFragment;
import com.example.han.referralproject.hypertensionmanagement.fragment.BloodPresureMeasuerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BloodPressureMeasureActivity extends AppCompatActivity {

    @BindView(R.id.container)
    FrameLayout container;
    private HealthBloodDetectionFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_measure);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = new BloodPresureMeasuerFragment();
        manager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
    }


}
