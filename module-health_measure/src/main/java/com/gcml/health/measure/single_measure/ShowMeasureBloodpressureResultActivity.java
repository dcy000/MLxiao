package com.gcml.health.measure.single_measure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gcml.common.recommend.fragment.RencommendFragment;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.single_measure.fragment.ShowMeasureBloodpressureResultFragment;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

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
        CCAppActions.jump2MainActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_show_measure_contanier);
        initView();
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
            bundle.putBoolean("isTask",intent.getBooleanExtra("isTask",false));
            resultFragment.setArguments(bundle);
        }

        RencommendFragment rencommendFragment = new RencommendFragment();
        fragments.add(resultFragment);
        fragments.add(rencommendFragment);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }

    private void initView() {
        mViewpage = (ViewPager) findViewById(R.id.viewpage);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血 压 结 果 分 析");

    }
}
