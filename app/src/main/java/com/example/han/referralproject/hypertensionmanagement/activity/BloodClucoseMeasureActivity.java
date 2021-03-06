package com.example.han.referralproject.hypertensionmanagement.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.hypertensionmanagement.fragment.BloodClucoseMeasureFragment;
import com.gcml.common.data.AppManager;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BloodClucoseMeasureActivity extends AppCompatActivity {

    @BindView(R.id.container)
    FrameLayout container;
    private BloodClucoseMeasureFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_clucose_measure);
        ButterKnife.bind(this);
        initView();
        MLVoiceSynthetize.startSynthesize(this,"主人，拿出测量设备，我们做个测量",false);
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = new BloodClucoseMeasureFragment();
        manager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
    }
}
