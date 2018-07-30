package com.example.han.referralproject.hypertensionmanagement.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.hypertensionmanagement.fragment.BloodPresureMeasuerFragment;
import com.example.han.referralproject.hypertensionmanagement.fragment.WeigtMeasureFragment;
import com.example.han.referralproject.hypertensionmanagement.util.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeightMeasureActivity extends AppCompatActivity {

    @BindView(R.id.container)
    FrameLayout container;
    private WeigtMeasureFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_measure);
        ButterKnife.bind(this);
        initView();
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = new WeigtMeasureFragment();
        manager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
    }
}
