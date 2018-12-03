package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.MonthlyReport;
import com.example.han.referralproject.service.API;
import com.gcml.lib_widget.circleindicator.CircleIndicator;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        Box.getRetrofit(API.class)
                .getMonthlyReport(Box.getUserId(), "2")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<MonthlyReport>() {
                    @Override
                    public void onNext(MonthlyReport monthlyReport) {
                        if (monthlyReport != null) {
                            fragment1.notifyData(monthlyReport.mapq);
                            fragment2.notifyData(monthlyReport.mapq);
                            fragment3.notifyData(monthlyReport.mapq);
                            fragment4.notifyData(monthlyReport.mapq);
                            fragment5.notifyData(monthlyReport.mapq);
                        } else {
                            ToastUtils.showShort("暂无月报告");
                            MLVoiceSynthetize.startSynthesize("主人，您的测量数据太少，我们还不能为您生成本月的报告。请您坚持每天测量，我们将在每月一号为您生成新报告");
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showShort("暂无月报告");
                        MLVoiceSynthetize.startSynthesize("主人，您的测量数据太少，我们还不能为您生成本月的报告。请您坚持每天测量，我们将在每月一号为您生成新报告");
                    }
                });
    }
}
