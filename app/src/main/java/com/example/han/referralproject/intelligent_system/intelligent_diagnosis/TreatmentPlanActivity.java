package com.example.han.referralproject.intelligent_system.intelligent_diagnosis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Administrator on 2018/5/15.
 */

public class TreatmentPlanActivity extends BaseActivity implements IChangToolbar {
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    private List<Fragment> fragments;
    private LastWeekHealthReportFragment treatmentProgramFragment1;
    private ThisWeekHealthPlanFragment treatmentProgramFragment2;
    private DietPlanFragment treatmentProgramFragment3;
    private WeekDietPlanFragment treatmentProgramFragment4;
    private SportPlanFragment treatmentProgramFragment5;
    public static boolean isSpeaked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_plan);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("生活治疗方案");
        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        treatmentProgramFragment1 = new LastWeekHealthReportFragment();
        fragments.add(treatmentProgramFragment1);
        treatmentProgramFragment1.setOnChangToolbar(this);

        treatmentProgramFragment2 = new ThisWeekHealthPlanFragment();
        fragments.add(treatmentProgramFragment2);
        treatmentProgramFragment2.setOnChangToolbar(this);

        treatmentProgramFragment3 = new DietPlanFragment();
        fragments.add(treatmentProgramFragment3);
        treatmentProgramFragment3.setOnChangToolbar(this);

        treatmentProgramFragment4 = new WeekDietPlanFragment();
        fragments.add(treatmentProgramFragment4);
        treatmentProgramFragment4.setOnChangToolbar(this);

        treatmentProgramFragment5 = new SportPlanFragment();
        fragments.add(treatmentProgramFragment5);
        treatmentProgramFragment5.setOnChangToolbar(this);

        viewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        circleIndicator.setViewPager(viewpage);
    }

    @Override
    public void onChange(Fragment fragment) {
        if (fragment instanceof LastWeekHealthReportFragment) {
            mTitleText.setText("上周健康报告");
        } else if (fragment instanceof ThisWeekHealthPlanFragment) {
            mTitleText.setText("检测计划");
        } else if (fragment instanceof DietPlanFragment) {
            mTitleText.setText("膳食计划");
        } else if (fragment instanceof WeekDietPlanFragment) {
            mTitleText.setText("膳食计划");
        } else if (fragment instanceof SportPlanFragment) {
            mTitleText.setText("运动计划");
        }
    }
}
