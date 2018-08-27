package com.gcml.health.measure.first_diagnosis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.FirstDiagnosisBean;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodDetectionUiFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthBloodOxygenDetectionFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.first_diagnosis.fragment.HealthTemperatureDetectionFragment;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.health.measure.R;

import java.util.ArrayList;
import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 13:52
 * created by:gzq
 * description:TODO
 */
public class FirstDiagnosisActivity extends ToolbarBaseActivity {
    private List<FirstDiagnosisBean> firstDiagnosisBeans;
    private FrameLayout mFrame;
    private int showPosition=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_diagnosis);
        initView();
        initFirstDiagnosis();
        showFragment(showPosition);
    }

    private void showFragment(int showPosition) {
        Fragment fragment = firstDiagnosisBeans.get(showPosition).getFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();
    }

    private void initFirstDiagnosis() {
        firstDiagnosisBeans=new ArrayList<>();
        FirstDiagnosisBean firstTip=new FirstDiagnosisBean(new HealthFirstTipsFragment(),new ArrayList<DetectionData>());
        firstDiagnosisBeans.add(firstTip);

        FirstDiagnosisBean bloodpressure=new FirstDiagnosisBean(new HealthBloodDetectionUiFragment(),new ArrayList<DetectionData>());
        firstDiagnosisBeans.add(bloodpressure);

        FirstDiagnosisBean bloodoxygen=new FirstDiagnosisBean(new HealthBloodOxygenDetectionFragment(),new ArrayList<DetectionData>());
        firstDiagnosisBeans.add(bloodoxygen);

        FirstDiagnosisBean temperature=new FirstDiagnosisBean(new HealthTemperatureDetectionFragment(),new ArrayList<DetectionData>());
        firstDiagnosisBeans.add(temperature);

    }

    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
    }
}
