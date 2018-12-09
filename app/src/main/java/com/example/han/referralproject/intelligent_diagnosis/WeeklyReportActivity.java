package com.example.han.referralproject.intelligent_diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.model.WeekReportModel;
import com.example.han.referralproject.service.API;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.circleindicator.CircleIndicator;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
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
 * Created by gzq on 2018/3/9.
 */

public class WeeklyReportActivity extends ToolbarBaseActivity {
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
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_weekly_report;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
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

    @NonNull
    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    private void getData() {
        Box.getRetrofit(API.class)
                .getWeeklyReport(Box.getUserId(), "1")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<WeekReportModel>() {
                    @Override
                    public void onNext(WeekReportModel weekReportModel) {
                        if (weekReportModel != null) {
                            fragment1.notifyData(weekReportModel.lastWeek);
                            fragment2.notifyData(weekReportModel.lastWeek);
                            fragment3.notifyData(weekReportModel.lastWeek);
                        } else {
                            ToastUtils.showShort("暂无周报告");
                            MLVoiceSynthetize.startSynthesize("主人，您的测量数据太少，" +
                                    "我们还不能为您生成本周的报告。请您坚持每天测量，" +
                                    "我们将在每周一为您生成新报告");
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showShort("暂无周报告");
                        MLVoiceSynthetize.startSynthesize("主人，您的测量数据太少，" +
                                "我们还不能为您生成本周的报告。请您坚持每天测量，我们将在每周一为您生成新报告");

                    }
                });
    }


}
