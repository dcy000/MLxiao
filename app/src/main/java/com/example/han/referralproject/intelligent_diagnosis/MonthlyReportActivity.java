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
import com.example.han.referralproject.bean.MonthlyReport;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by gzq on 2018/3/14.
 */

public class MonthlyReportActivity extends BaseActivity {
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    private List<Fragment> fragments;
    private MonthlyReport1Fragment fragment1;
    private MonthlyReport2Fragment fragment2;
    private MonthlyReport3Fragment fragment3;
    private MonthlyReport4Fragment fragment4;
    private MonthlyReport5Fragment fragment5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("月生活记录");
        initFragment();
    }

    private void initFragment() {
        fragments = new ArrayList<>();

        fragments.add(fragment1 = new MonthlyReport1Fragment());
        fragments.add(fragment2 = new MonthlyReport2Fragment());
        fragments.add(fragment3 = new MonthlyReport3Fragment());
        fragments.add(fragment4 = new MonthlyReport4Fragment());
        fragments.add(fragment5 = new MonthlyReport5Fragment());
        viewpage.setOffscreenPageLimit(5);
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
        NetworkApi.getMonthReport(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<MonthlyReport>() {
            @Override
            public void onSuccess(MonthlyReport response) {
                if (response != null) {
                    fragment1.notifyData(response);
                    fragment2.notifyData(response);
                    fragment3.notifyData(response);
                    fragment4.notifyData(response);
                    fragment5.notifyData(response);
                    Logger.e("返回测量数据成功" + response);
                } else {
                    ToastUtils.show("暂无数据");
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                Logger.e("返回测量数据失败" + message);
                ToastUtils.show("暂无数据");
            }
        });
    }
}
