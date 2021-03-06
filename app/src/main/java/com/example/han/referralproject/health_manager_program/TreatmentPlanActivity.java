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

import me.relex.circleindicator.CircleIndicator;

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
        mTitleText.setText("一周血压趋势表");
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
        speak("主人，小E已为你生成具体计划方案，滑动屏幕可进行查看。");
    }

    @Override
    public void onChange(Fragment fragment) {
        if (fragment instanceof LastWeekTrendFragment) {
            mTitleText.setText("一 周 血 压 趋 势 表");
        } else if (fragment instanceof ThisWeekHealthPlanFragment) {
            mTitleText.setText("检 测 方 案");
        } else if (fragment instanceof DietPlanFragment) {
            mTitleText.setText("膳 食 方 案");
        } else if (fragment instanceof WeekDietPlanFragment) {
            mTitleText.setText("推 荐 食 谱");
        } else if (fragment instanceof SportPlanFragment) {
            mTitleText.setText("运 动 方 案");
        } else if (fragment instanceof MedicinePlanFragment) {
            mTitleText.setText("药 物 方 案");
        } else if (fragment instanceof RencommendForUserFragment) {
            mTitleText.setText("智 能 推 荐");
        }
    }

    private void initView() {
        viewpage = findViewById(R.id.viewpage);
        circleIndicator = findViewById(R.id.circleIndicator);
    }
}
