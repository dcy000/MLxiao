package com.example.han.referralproject.health_manager_program;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.gcml.common.recommend.fragment.IChangToolbar;
import com.gcml.common.recommend.fragment.RencommendForUserFragment;

import java.util.ArrayList;
import java.util.List;

import com.gcml.common.widget.CircleIndicator;

/**
 * Created by Administrator on 2018/5/15.
 */

public class TreatmentPlanActivity extends BaseActivity implements IChangToolbar {
    private List<Fragment> fragments;
    private LastWeekTrendFragment treatmentProgramFragment1;
    private ThisWeekHealthPlanFragment treatmentProgramFragment2;
    private DietPlanFragment treatmentProgramFragment3;
    private WeekDietPlanFragment treatmentProgramFragment4;
    private SportPlanFragment treatmentProgramFragment5;
    private MedicinePlanFragment treatmentProgramFragment6;
    private RencommendForUserFragment treatmentProgramFragment7;
    public static boolean isSpeaked = false;
    private ViewPager viewpage;
    private CircleIndicator circleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_plan);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(R.string.week_blood_pressure_trend_table);
        initView();
        initFragments();
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

    private void initFragments() {
        fragments = new ArrayList<>();
//        treatmentProgramFragment1 = new LastWeekHealthReportFragment();
//        fragments.add(treatmentProgramFragment1);
//        treatmentProgramFragment1.setOnChangToolbar(this);
        treatmentProgramFragment1 = new LastWeekTrendFragment();
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

        treatmentProgramFragment6 = new MedicinePlanFragment();
        fragments.add(treatmentProgramFragment6);
        treatmentProgramFragment6.setOnChangToolbar(this);

        treatmentProgramFragment7 = new RencommendForUserFragment();
        fragments.add(treatmentProgramFragment7);
        treatmentProgramFragment7.setOnChangToolbar(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        speak(R.string.Swipe_the_screen_to_view);
    }

    @Override
    public void onChange(Fragment fragment) {
        if (fragment instanceof LastWeekTrendFragment) {
            mTitleText.setText(R.string.week_blood_pressure_trend_table);
        } else if (fragment instanceof ThisWeekHealthPlanFragment) {
            mTitleText.setText(R.string.Inspection_plan);
        } else if (fragment instanceof DietPlanFragment) {
            mTitleText.setText(R.string.Dietary_plan);
        } else if (fragment instanceof WeekDietPlanFragment) {
            mTitleText.setText(R.string.Recommended_food_spectrum);
        } else if (fragment instanceof SportPlanFragment) {
            mTitleText.setText(R.string.Sports_plan);
        } else if (fragment instanceof MedicinePlanFragment) {
            mTitleText.setText(R.string.Drug_plan);
        } else if (fragment instanceof RencommendForUserFragment) {
            mTitleText.setText(R.string.Intelligent_Recommendation);
        }
    }

    private void initView() {
        viewpage = findViewById(R.id.viewpage);
        circleIndicator = findViewById(R.id.circleIndicator);
    }
}
