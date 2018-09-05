package com.gcml.health.measure.health_report_form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.health.measure.BuildConfig;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.demo.DemoGoodTipsFragment;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportBean;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.DialogSure;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
    public static final String KEY_TYPE = "key_type";
    private String userId;
    private int checkViewpageState = 4;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HealthReportFormActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_report_form);
        initView();
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，请查看您的疾病风险评估报告，向左滑动查看详情");
        getData();

    }

    @Override
    protected void backMainActivity() {
        CCAppActions.jump2MainActivity();
    }

    @SuppressLint("CheckResult")
    private void getData() {
        //TODO:测试代码
        if (BuildConfig.RUN_APP) {
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
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dismissLoading();
                    }
                })
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
            bundle2.putParcelable(KEY_DATA, firstReportBean);
            bundle2.putString(KEY_TYPE, "糖尿病");
            fragment2.setArguments(bundle2);
            fragments.add(fragment2);
        } else if ("1".equals(dm)) {
            HealthReportFormFragment3 fragment2 = new HealthReportFormFragment3();
            bundle2.putParcelable(KEY_DATA, firstReportBean);
            bundle2.putString(KEY_TYPE, "糖尿病");
            fragment2.setArguments(bundle2);
            fragments.add(fragment2);
        }


        Bundle bundle3 = new Bundle();
        if ("0".equals(htn)) {
            HealthReportFormFragment2 fragment3 = new HealthReportFormFragment2();
            bundle3.putParcelable(KEY_DATA, firstReportBean);
            bundle3.putString(KEY_TYPE, "高血压");
            fragment3.setArguments(bundle3);
            fragments.add(fragment3);
        } else if ("1".equals(htn)) {
            HealthReportFormFragment3 fragment3 = new HealthReportFormFragment3();
            bundle3.putParcelable(KEY_DATA, firstReportBean);
            bundle3.putString(KEY_TYPE, "高血压");
            fragment3.setArguments(bundle3);
            fragments.add(fragment3);
        }


        Bundle bundle4 = new Bundle();
        if ("0".equals(fat)) {
            HealthReportFormFragment2 fragment4 = new HealthReportFormFragment2();
            bundle4.putParcelable(KEY_DATA, firstReportBean);
            bundle4.putString(KEY_TYPE, "肥胖症");
            fragment4.setArguments(bundle4);
            fragments.add(fragment4);
        } else if ("1".equals(fat)) {
            HealthReportFormFragment3 fragment4 = new HealthReportFormFragment3();
            bundle4.putParcelable(KEY_DATA, firstReportBean);
            bundle4.putString(KEY_TYPE, "肥胖症");
            fragment4.setArguments(bundle4);
            fragments.add(fragment4);
        }


        Bundle bundle5 = new Bundle();
        if ("0".equals(icvd)) {
            HealthReportFormFragment2 fragment5 = new HealthReportFormFragment2();
            bundle5.putParcelable(KEY_DATA, firstReportBean);
            bundle5.putString(KEY_TYPE, "心血管病");
            fragment5.setArguments(bundle5);
            fragments.add(fragment5);
        } else if ("1".equals(icvd)) {
            HealthReportFormFragment3 fragment5 = new HealthReportFormFragment3();
            bundle5.putParcelable(KEY_DATA, firstReportBean);
            bundle5.putString(KEY_TYPE, "心血管病");
            fragment5.setArguments(bundle5);
            fragments.add(fragment5);
        }

//        DemoGoodTipsFragment tipsFragment = new DemoGoodTipsFragment();
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

                if (position == 4&& positionOffsetPixels == 0 && checkViewpageState > 0) {
                    checkViewpageState--;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTitleText.setText("健 康 报 告");
                        break;
                    case 1:
                        mTitleText.setText("糖 尿 病 评 估 报 告");
                        break;
                    case 2:
                        mTitleText.setText("高 血 压 评 估 报 告");
                        break;
                    case 3:
                        mTitleText.setText("肥 胖 症 评 估 报 告");
                        break;
                    case 4:
                        mTitleText.setText("心 血 管 病 评 估 报 告");
                        break;
                    case 5:
                        mTitleText.setText("智 能 推 荐");
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

    private DialogSure dialogSure;

    private void showEndDialog() {
        checkViewpageState = 4;
        if (dialogSure == null) {
            dialogSure = new DialogSure(this);
            View inflate = LayoutInflater.from(this).inflate(R.layout.health_measure_dialog_enter_mainactivity, null, false);
            dialogSure.setContentView(inflate);
            TextView btnEnter = inflate.findViewById(R.id.btn_enter);
            btnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.e("点击了主页面");
                    CCAppActions.jump2MainActivity();
                }
            });
        }
        if (!dialogSure.isShowing()) {
            dialogSure.show();
        }

    }

    private void initView() {
        mViewpage = (ViewPager) findViewById(R.id.viewpage);
        mTitleText.setText("健 康 报 告");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    private LoadingDialog mLoadingDialog;

    private void showLoading(String tips) {
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

    private void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }
}
