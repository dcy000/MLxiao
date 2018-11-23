package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.health.model.WeekReportModel;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by gzq on 2018/3/9.
 */

public class WeeklyReportActivity extends BaseActivity {
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    private List<Fragment> fragments;
    private WeeklyReport1Fragment fragment1;
    private WeeklyReport2Fragment fragment2;
    private WeeklyReport3Fragment fragment3;
    private LifeRecordWeeklyFragment fragment4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_report);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("周生活记录");
        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        fragment1 = new WeeklyReport1Fragment();
//        fragment1.setArguments(bundle);
        fragments.add(fragment1);


        fragment2 = new WeeklyReport2Fragment();
//        fragment2.setArguments(bundle);
        fragments.add(fragment2);


        fragment3 = new WeeklyReport3Fragment();
//        fragment3.setArguments(bundle);
        fragments.add(fragment3);

        fragment4 = new LifeRecordWeeklyFragment();
//        fragment3.setArguments(bundle);
        fragments.add(fragment4);

        viewpage.setOffscreenPageLimit(3);
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
        getData();

    }

    private void getData() {
        NetworkApi.getWeekReport(Box.getUserId(), new NetworkManager.SuccessCallback<WeekReportModel>() {
            @Override
            public void onSuccess(WeekReportModel response) {
                if (response != null) {
                    fragment1.notifyData(response.lastWeek);
                    fragment2.notifyData(response.lastWeek);
                    fragment3.notifyData(response.lastWeek);
                } else {
                    ToastUtils.showShort("暂无周报告");
                    MLVoiceSynthetize.startSynthesize("主人，您的测量数据太少，我们还不能为您生成本周的报告。请您坚持每天测量，我们将在每周一为您生成新报告");
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort("暂无周报告");
                MLVoiceSynthetize.startSynthesize("主人，您的测量数据太少，我们还不能为您生成本周的报告。请您坚持每天测量，我们将在每周一为您生成新报告");
            }
        });
    }


}
