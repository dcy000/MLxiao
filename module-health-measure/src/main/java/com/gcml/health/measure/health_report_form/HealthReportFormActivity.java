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

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.CustomDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.health.measure.BuildConfig;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportReceiveBean;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

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

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HealthReportFormActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_report_form);
        initView();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), UM.getString(R.string.disease_risk_assessment_report_slide_left));
        getData();

    }

    @Override
    protected void backMainActivity() {
        CCAppActions.jump2MainActivity();
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
                        showLoading(UM.getString(R.string.dialog_loading));
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
                            ToastUtils.showShort(R.string.toast_empty_data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(R.string.toast_empty_data);
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
//        RencommendForUserFragment tipsFragment = new RencommendForUserFragment();
//        fragments.add(tipsFragment);
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

                switch (position) {
                    case 0:
                        mTitleText.setText(R.string.title_health_report);
                        break;
                    case 1:
                        mTitleText.setText(R.string.title_high_blood_pressure_assessment_report);

                        break;
                    case 2:
                        mTitleText.setText(R.string.title_diabetes_assessment_report);
                        break;
                    case 3:
                        mTitleText.setText(R.string.title_fatty_assessment_report);
                        break;
                    case 4:
                        mTitleText.setText(R.string.title_hemolytic_vascular_disease_report);
                        break;
                    case 5:
                        mTitleText.setText(R.string.title_intelligent_recommendation);
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
                .setMsg(UM.getString(R.string.dialog_complete_risk_assessment_start_other_travel))
                .setPositiveButton(UM.getString(R.string.dialog_button_start_the_experience), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CCAppActions.jump2MainActivity();
                    }
                }).show();
    }

    private void initView() {
        mViewpage = (ViewPager) findViewById(R.id.viewpage);
        mTitleText.setText(R.string.title_health_report);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    private LoadingDialog mLoadingDialog;

    public void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    public void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }
}
