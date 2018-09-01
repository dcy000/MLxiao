package com.gcml.health.measure.health_report_form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportBean;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
    public static final String KEY_TYPE="key_type";
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HealthReportFormActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_report_form);
        initView();
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),"主人，请查看您的疾病风险评估报告，向左滑动查看详情");
        getData();

    }

    @Override
    protected void backMainActivity() {
        CCAppActions.jump2MainActivity();
    }

    @SuppressLint("CheckResult")
    private void getData() {
        HealthMeasureRepository.getFirstReport("100206")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<FirstReportBean>() {
                    @Override
                    public void onNext(FirstReportBean firstReportBean) {
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

    private void dealData(FirstReportBean firstReportBean) {
        fragments = new ArrayList<>();
        HealthReportFormFragment1 fragment1 = new HealthReportFormFragment1();
        Bundle bundle1 = new Bundle();
        bundle1.putParcelable(KEY_DATA, firstReportBean);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);


        //糖尿病
        String dm = firstReportBean.getDm().getIllnessStatus();
        //高血压
        String htn = firstReportBean.getHtn().getIllnessStatus();
        //肥胖症
        String fat = firstReportBean.getFat().getIllnessStatus();
        //心血管
        String icvd = firstReportBean.getIcvd().getIllnessStatus();
        //0是为未确诊，1是确诊


        Bundle bundle2 = new Bundle();
        if ("0".equals(dm)) {
            HealthReportFormFragment2 fragment2 = new HealthReportFormFragment2();
            bundle2.putParcelable(KEY_DATA,firstReportBean);
            bundle2.putString(KEY_TYPE,"糖尿病");
            fragment2.setArguments(bundle2);
            fragments.add(fragment2);
        } else if ("1".equals(dm)) {
            HealthReportFormFragment3 fragment2 = new HealthReportFormFragment3();
            bundle2.putParcelable(KEY_DATA,firstReportBean);
            bundle2.putString(KEY_TYPE,"糖尿病");
            fragment2.setArguments(bundle2);
            fragments.add(fragment2);
        }


        Bundle bundle3 = new Bundle();
        if ("0".equals(htn)) {
            HealthReportFormFragment2 fragment3 = new HealthReportFormFragment2();
            bundle3.putParcelable(KEY_DATA,firstReportBean);
            bundle3.putString(KEY_TYPE,"高血压");
            fragment3.setArguments(bundle3);
            fragments.add(fragment3);
        } else if ("1".equals(htn)) {
            HealthReportFormFragment3 fragment3 = new HealthReportFormFragment3();
            bundle3.putParcelable(KEY_DATA,firstReportBean);
            bundle3.putString(KEY_TYPE,"高血压");
            fragment3.setArguments(bundle3);
            fragments.add(fragment3);
        }


        Bundle bundle4 = new Bundle();
        if ("0".equals(fat)) {
            HealthReportFormFragment2 fragment4 = new HealthReportFormFragment2();
            bundle4.putParcelable(KEY_DATA,firstReportBean);
            bundle4.putString(KEY_TYPE,"肥胖症");
            fragment4.setArguments(bundle4);
            fragments.add(fragment4);
        } else if ("1".equals(fat)) {
            HealthReportFormFragment3 fragment4 = new HealthReportFormFragment3();
            bundle4.putParcelable(KEY_DATA,firstReportBean);
            bundle4.putString(KEY_TYPE,"肥胖症");
            fragment4.setArguments(bundle4);
            fragments.add(fragment4);
        }


        Bundle bundle5 = new Bundle();
        if ("0".equals(icvd)) {
            HealthReportFormFragment2 fragment5 = new HealthReportFormFragment2();
            bundle5.putParcelable(KEY_DATA,firstReportBean);
            bundle5.putString(KEY_TYPE,"心血管病");
            fragment5.setArguments(bundle5);
            fragments.add(fragment5);
        } else if ("1".equals(icvd)) {
            HealthReportFormFragment3 fragment5 = new HealthReportFormFragment3();
            bundle5.putParcelable(KEY_DATA,firstReportBean);
            bundle5.putString(KEY_TYPE,"心血管病");
            fragment5.setArguments(bundle5);
            fragments.add(fragment5);
        }
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

    private void initView() {
        mViewpage = (ViewPager) findViewById(R.id.viewpage);
        mTitleText.setText("健 康 报 告");
    }
}
