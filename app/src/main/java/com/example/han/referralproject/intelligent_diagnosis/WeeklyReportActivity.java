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
import com.example.han.referralproject.bean.WeeklyReport;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.ToastUtils;
import com.example.han.referralproject.util.ToastTool;
import com.orhanobut.logger.Logger;

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
        NetworkApi.getWeekReport(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<WeeklyReport>() {
            @Override
            public void onSuccess(WeeklyReport response) {
                if (response != null) {
                    fragment1.notifyData(response);
                    fragment2.notifyData(response);
                    fragment3.notifyData(response);
                    Logger.e("返回测量数据成功" + response);
                } else {
                    ToastTool.showShort("暂无周报告");
                    finish();
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                Logger.e("返回测量数据失败" + message);
                ToastTool.showShort("暂无周报告");
                finish();
            }
        });
    }


}