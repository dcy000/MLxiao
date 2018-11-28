package com.example.module_blood_pressure.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.module_blood_pressure.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_bluetooth.bean.DetectionData;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/30 9:19
 * created by:gzq
 * description:单次血压测量结果展示页面
 */
public class ShowMeasureBloodpressureResultActivity extends ToolbarBaseActivity implements View.OnClickListener {


    private ViewPager mViewpage;
    private List<Fragment> fragments;

    /**
     * @param context
     * @param state       血压状态：五种的一种
     * @param score       健康分数
     * @param currentHigh 当前高压
     * @param currentLow  当前低压
     * @param suggest     健康建议
     */
    public static void startActivity(Context context, String state, int score, int currentHigh,
                                     int currentLow, String suggest) {
        context.startActivity(new Intent(context, ShowMeasureBloodpressureResultActivity.class)
                .putExtra("health_state", state)
                .putExtra("health_score", score)
                .putExtra("high_bloodpressure", currentHigh)
                .putExtra("low_bloodpressure", currentLow)
                .putExtra("suggest", suggest));
    }

    public static void startActivity(Context context, String state, int score, int currentHigh,
                                     int currentLow, String suggest, boolean isTask) {
        context.startActivity(new Intent(context, ShowMeasureBloodpressureResultActivity.class)
                .putExtra("health_state", state)
                .putExtra("health_score", score)
                .putExtra("high_bloodpressure", currentHigh)
                .putExtra("low_bloodpressure", currentLow)
                .putExtra("suggest", suggest)
                .putExtra("isTask", isTask));
    }


    @Override
    protected void backMainActivity() {
        createEvent("HealthRecord>skip2MainActivity");
    }


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_measure_activity_show_measure_contanier;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mTitleText.setText("血 压 结 果 分 析");
        mViewpage = (ViewPager) findViewById(R.id.viewpage);
        initFragment();
        initViewPage();
    }

    private void initViewPage() {
        mViewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mTitleText.setText("血 压 结 果 分 析");
                } else if (position == 1) {
                    mTitleText.setText("智 能 推 荐");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        ShowMeasureBloodpressureResultFragment resultFragment = new ShowMeasureBloodpressureResultFragment();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = new Bundle();
            bundle.putString("health_state", intent.getStringExtra("health_state"));
            bundle.putInt("health_score", intent.getIntExtra("health_score", 0));
            bundle.putInt("high_bloodpressure", intent.getIntExtra("high_bloodpressure", 120));
            bundle.putInt("low_bloodpressure", intent.getIntExtra("low_bloodpressure", 80));
            bundle.putString("suggest", intent.getStringExtra("suggest"));
            bundle.putBoolean("isTask", intent.getBooleanExtra("isTask", false));
            resultFragment.setArguments(bundle);
        }
        fragments.add(resultFragment);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }
}
