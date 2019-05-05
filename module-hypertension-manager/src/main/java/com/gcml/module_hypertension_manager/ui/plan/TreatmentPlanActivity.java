package com.gcml.module_hypertension_manager.ui.plan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.recommend.fragment.IChangToolbar;
import com.gcml.common.recommend.fragment.RencommendForUserFragment;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.CircleIndicator;
import com.gcml.common.widget.dialog.CustomDialog;
import com.gcml.module_hypertension_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/15.
 */
@Route(path = "/app/health/manager/treatment")
public class TreatmentPlanActivity extends ToolbarBaseActivity implements IChangToolbar, View.OnClickListener {
    private List<Fragment> fragments;
    private LastWeekTrendFragment treatmentProgramFragment1;
    private ThisWeekHealthPlanFragment treatmentProgramFragment2;
    private DietPlanFragment treatmentProgramFragment3;
    private WeekDietPlanFragment treatmentProgramFragment4;
    private SportPlanFragment treatmentProgramFragment5;
    private MedicinePlanFragment treatmentProgramFragment6;
    private RencommendForUserFragment treatmentProgramFragment7;
    public static boolean isSpeaked = false;
    private ViewPager viewpage;
    private CircleIndicator circleIndicator;
    private ImageView ivAni;
    private int pageSelected;
//    private Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_plan);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("一周血压趋势表");
        initView();
        initFragments();
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

        viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageSelected = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragments() {
        fragments = new ArrayList<>();
//        treatmentProgramFragment1 = new LastWeekHealthReportFragment();
//        fragments.add(treatmentProgramFragment1);
//        treatmentProgramFragment1.setOnChangToolbar(this);
        treatmentProgramFragment1 = new LastWeekTrendFragment();
        fragments.add(treatmentProgramFragment1);
        treatmentProgramFragment1.setOnChangToolbar(this);

        treatmentProgramFragment2 = new ThisWeekHealthPlanFragment();
        fragments.add(treatmentProgramFragment2);
        treatmentProgramFragment2.setOnChangToolbar(this);

        treatmentProgramFragment3 = new DietPlanFragment();
        fragments.add(treatmentProgramFragment3);
        treatmentProgramFragment3.setOnChangToolbar(this);

        treatmentProgramFragment4 = new WeekDietPlanFragment();
        fragments.add(treatmentProgramFragment4);
        treatmentProgramFragment4.setOnChangToolbar(this);

        treatmentProgramFragment5 = new SportPlanFragment();
        fragments.add(treatmentProgramFragment5);
        treatmentProgramFragment5.setOnChangToolbar(this);

        treatmentProgramFragment6 = new MedicinePlanFragment();
        fragments.add(treatmentProgramFragment6);
        treatmentProgramFragment6.setOnChangToolbar(this);

        treatmentProgramFragment7 = new RencommendForUserFragment();
        fragments.add(treatmentProgramFragment7);
        treatmentProgramFragment7.setOnChangToolbar(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"小E已为你生成具体计划方案，滑动屏幕可进行查看。");
    }

    @Override
    public void onChange(Fragment fragment) {
        if (fragment instanceof LastWeekTrendFragment) {
            mTitleText.setText("一 周 血 压 趋 势 表");
            ivAni.setVisibility(View.VISIBLE);
//            ivAni.startAnimation(animation);
        } else if (fragment instanceof ThisWeekHealthPlanFragment) {
            mTitleText.setText("检 测 方 案");
            ivAni.setVisibility(View.VISIBLE);
//            ivAni.startAnimation(animation);
        } else if (fragment instanceof DietPlanFragment) {
            mTitleText.setText("膳 食 方 案");
            ivAni.setVisibility(View.VISIBLE);
//            ivAni.startAnimation(animation);
        } else if (fragment instanceof WeekDietPlanFragment) {
            mTitleText.setText("推 荐 食 谱");
            ivAni.setVisibility(View.VISIBLE);
//            ivAni.startAnimation(animation);
        } else if (fragment instanceof SportPlanFragment) {
            mTitleText.setText("运 动 方 案");
            ivAni.setVisibility(View.VISIBLE);
//            ivAni.startAnimation(animation);
        } else if (fragment instanceof MedicinePlanFragment) {
            mTitleText.setText("药 物 方 案");
            ivAni.setVisibility(View.VISIBLE);
//            ivAni.startAnimation(animation);
        } else if (fragment instanceof RencommendForUserFragment) {
            mTitleText.setText("智 能 推 荐");
            ivAni.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        viewpage = findViewById(R.id.viewpage);
        circleIndicator = findViewById(R.id.circleIndicator);
        ivAni = findViewById(R.id.iv_ani);
        ivAni.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (pageSelected) {
            case 0:
                viewpage.setCurrentItem(1);
                break;
            case 1:
                viewpage.setCurrentItem(2);
                break;
            case 2:
                viewpage.setCurrentItem(3);
                break;
            case 3:
                viewpage.setCurrentItem(4);
                break;
            case 4:
                viewpage.setCurrentItem(5);
                break;
            case 5:
                viewpage.setCurrentItem(6);
                break;
            case 6:
                showEndDialog();
                break;

        }
    }

    private void showEndDialog() {
        new CustomDialog(this).builder()
                .setImg(0)
                .setMsg("为了更好的了解您的情况，建议完成为您量身定制的每日任务")
                .setPositiveButton("每日任务", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toTask();
                    }
                }).show();
    }

    private void toTask() {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                            ToastUtils.showShort("请先去个人中心完善体重和身高信息");
                            MLVoiceSynthetize.startSynthesize(UM.getApp(),
                                    "请先去个人中心完善体重和身高信息");
                        } else {
                            Routerfit.register(AppRouter.class).getTaskProvider()
                                    .isTaskHealth()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new io.reactivex.observers.DefaultObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            Routerfit.register(AppRouter.class).skipTaskActivity("MLMain");

                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            if (throwable instanceof NullPointerException) {
                                                Routerfit.register(AppRouter.class).skipTaskActivity("MLMain");
                                            } else {
                                                Routerfit.register(AppRouter.class).skipTaskComplyActivity();
                                            }
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    }
                });
    }
}
