package com.gcml.health.measure.health_report_form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gcml.health.measure.R;
import com.gcml.lib_utils.base.ToolbarBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 16:16
 * created by:gzq
 * description:TODO
 */
public class HealthReportFormActivity extends ToolbarBaseActivity {

    private ViewPager mViewpage;
    private List<Fragment> fragments;
    public static void startActivity(){

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_report_form);
        initView();
        initFragments();
        initViewPage();
    }

    private void initViewPage() {
        mViewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        HealthReportFormFragment1 fragment1 = new HealthReportFormFragment1();
        fragments.add(fragment1);
        HealthReportFormFragment2 fragment2 = new HealthReportFormFragment2();
        fragments.add(fragment2);
        HealthReportFormFragment3 fragment3 = new HealthReportFormFragment3();
        fragments.add(fragment3);
    }

    private void initView() {
        mViewpage = (ViewPager) findViewById(R.id.viewpage);
    }
}
