package com.gcml.health.measure.health_report_form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.fragment.RencommendForUserFragment;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.CustomDialog;
import com.gcml.health.measure.BuildConfig;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportReceiveBean;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

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
    public static final String KEY_DATA = "key_data";
    public static final String KEY_TYPE = "key_type";
    private String userId;
    private int checkViewpageState = 5;
    private int currentViewPagePosition = 0;
    private ImageView nextIndicator;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HealthReportFormActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_report_form);
        initView();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请查看您的疾病风险评估报告，向左滑动查看详情");
        getData();

    }

    @SuppressLint("CheckResult")
    private void getData() {
        //TODO:测试代码
        if (BuildConfig.RUN_AS_APP) {
            userId = " 100217";
        } else {
            userId = UserSpHelper.getUserId();
        }
        HealthMeasureRepository.getFirstReport(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("加载中");
                    }
                })
                .doOnNext(new Consumer<FirstReportReceiveBean>() {
                    @Override
                    public void accept(FirstReportReceiveBean firstReportBean) throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<FirstReportReceiveBean>() {
                    @Override
                    public void onNext(FirstReportReceiveBean firstReportBean) {
                        if (firstReportBean != null) {
                            dealData(firstReportBean);
                        } else {
                            ToastUtils.showShort("数据为空");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("数据为空");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void dealData(FirstReportReceiveBean firstReportBean) {
        fragments = new ArrayList<>();
        HealthReportFormFragment1 fragment1 = new HealthReportFormFragment1();
        Bundle bundle1 = new Bundle();
        bundle1.putParcelable(KEY_DATA, firstReportBean);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);

        List<FirstReportReceiveBean.ReportListBean> reportList = firstReportBean.getReportList();
        for (FirstReportReceiveBean.ReportListBean reportListBean : reportList) {
            String illnessStatus = reportListBean.getIllnessStatus();
            Bundle bundle = new Bundle();
            if ("0".equals(illnessStatus)) {
                //未确诊
                HealthReportFormFragment2 fragment2 = new HealthReportFormFragment2();
                bundle.putParcelable(KEY_DATA, firstReportBean);
                bundle.putString(KEY_TYPE, reportListBean.getIllnessName());
                fragment2.setArguments(bundle);
                fragments.add(fragment2);
            } else if ("1".equals(illnessStatus)) {
                //已确诊
                HealthReportFormFragment3 fragment3 = new HealthReportFormFragment3();
                bundle.putParcelable(KEY_DATA, firstReportBean);
                bundle.putString(KEY_TYPE, reportListBean.getIllnessName());
                fragment3.setArguments(bundle);
                fragments.add(fragment3);
            }

        }
        RencommendForUserFragment tipsFragment = new RencommendForUserFragment();
        fragments.add(tipsFragment);
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
        mViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == fragments.size() - 1 && positionOffsetPixels == 0 && checkViewpageState > 0) {
                    checkViewpageState--;
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentViewPagePosition = position;
                switch (position) {
                    case 0:
                        mTitleText.setText("健 康 报 告");
                        nextIndicator.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mTitleText.setText("高 血 压 评 估 报 告");
                        nextIndicator.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mTitleText.setText("糖 尿 病 评 估 报 告");
                        nextIndicator.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        mTitleText.setText("肥 胖 症 评 估 报 告");
                        nextIndicator.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        mTitleText.setText("缺 血 性 心 血 管 病 评 估 报 告");
                        nextIndicator.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        mTitleText.setText("智 能 推 荐");
                        nextIndicator.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0 && checkViewpageState <= 0) {
                    showEndDialog();
                }
            }
        });
    }

    private void showEndDialog() {
        checkViewpageState = fragments.size() - 1;
        new CustomDialog(this).builder()
                .setImg(0)
                .setMsg("您已完成风险评估，为了更好的了解您的情况，建议您开启健康方案")
                .setPositiveButton("健康方案", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Routerfit.register(AppRouter.class).skipSlowDiseaseManagementActivity();
                    }
                }).show();
    }

    private void initView() {
        mViewpage = (ViewPager) findViewById(R.id.viewpage);
        mTitleText.setText("健 康 报 告");
        nextIndicator = findViewById(R.id.iv_ani);
        nextIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentViewPagePosition) {
                    case 0:
                        mViewpage.setCurrentItem(1);
                        break;
                    case 1:
                        mViewpage.setCurrentItem(2);
                        break;
                    case 2:
                        mViewpage.setCurrentItem(3);
                        break;
                    case 3:
                        mViewpage.setCurrentItem(4);
                        break;
                    case 4:
                        mViewpage.setCurrentItem(5);
                        break;
                    case 5:
                        showEndDialog();
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }
}
