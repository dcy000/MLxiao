package com.example.han.referralproject.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.StatusBarFragment;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.ShowStateBar;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.JpushAliasUtils;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 16:24
 * created by:gzq
 * description:新的主界面
 */
@Route(path = "/app/homepage/main/activity")
public class MainActivity extends ToolbarBaseActivity implements View.OnClickListener {

    private ViewPager mViewpage;
    private View mIndicatorLeft;
    private View mIndicatorMiddle;
    private List<Fragment> fragments;
    private NewMain1Fragment newMain1Fragment;
    private NewMain2Fragment newMain2Fragment;
    private NewMain3Fragment newMain3Fragment;
    private ImageView gotoNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        mToolbar.setVisibility(View.GONE);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), getString(R.string.tips_splash));
        initView();
        initFragments();
        initViewpage();
        //初始化百度语音
        Routerfit.register(AppRouter.class).getBaiduAKProvider().initAK();
        //启动音量控制悬浮按钮
        Routerfit.register(AppRouter.class).getVolumeControlProvider().init(getApplication());
        //检查版本更新
        Routerfit.register(AppRouter.class).getAppUpdateProvider().checkAppVersion(this, false);
    }

    private void initViewpage() {
        mViewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewpage.setCurrentItem(0);
        mViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mIndicatorLeft.setVisibility(View.VISIBLE);
                    mIndicatorMiddle.setVisibility(View.INVISIBLE);
//                    mIndicatorRight.setVisibility(View.INVISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(false);
                    }
                    gotoNext.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    mIndicatorLeft.setVisibility(View.INVISIBLE);
                    mIndicatorMiddle.setVisibility(View.VISIBLE);
//                    mIndicatorRight.setVisibility(View.INVISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(true);
                    }
                    gotoNext.setVisibility(View.GONE);
                } else if (position == 2) {
                    mIndicatorLeft.setVisibility(View.INVISIBLE);
                    mIndicatorMiddle.setVisibility(View.INVISIBLE);
//                    mIndicatorRight.setVisibility(View.VISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(true);
                    }
                    gotoNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void initFragments() {
        fragments = new ArrayList<>();
        newMain1Fragment = new NewMain1Fragment();
        newMain2Fragment = new NewMain2Fragment();
//        newMain3Fragment = new NewMain3Fragment();

        fragments.add(newMain1Fragment);
        fragments.add(newMain2Fragment);
//        fragments.add(newMain3Fragment);
    }

    private void initView() {
        mViewpage = findViewById(R.id.viewpage);
        mIndicatorLeft = findViewById(R.id.indicator_left);
//        mIndicatorRight = findViewById(R.id.indicator_right);
        mIndicatorMiddle = findViewById(R.id.indicator_middle);
        gotoNext = findViewById(R.id.iv_goto_next_page);
        gotoNext.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.i("MainActivity-->onStart");
        getPersonInfo();
    }

    //获取个人信息，得到网易账号登录所需的账号和密码
    private void getPersonInfo() {
        if ("123456".equals(UserSpHelper.getUserId())) {
            return;
        }
        Observable<UserEntity> rxUsers = Routerfit.register(AppRouter.class).getUserProvider().getUserEntity();
        if (rxUsers == null) {
            Routerfit.register(AppRouter.class).skipAuthActivity();
            return;
        }
        rxUsers.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        Timber.i(user.toString());
                        UserSpHelper.setUserHypertensionHand(user.hypertensionHand);
                        UserSpHelper.setUserId(user.id);
                        UserSpHelper.setEqId(user.deviceId);
                        String wyyxId = user.wyyxId;
                        String wyyxPwd = user.wyyxPwd;
                        if (TextUtils.isEmpty(wyyxId) || TextUtils.isEmpty(wyyxPwd)) {
                            Timber.e("获取网易账号信息出错");
                            return;
                        }
                        Routerfit.register(AppRouter.class).getCallProvider().login(wyyxId, wyyxPwd);
                        JpushAliasUtils.setAlias(user.id);
                    }
                });
    }

    private ShowStateBar showStateBar;

    /**
     * 控制状态bar中日期信息的显示或者隐藏
     *
     * @param showStateBar
     */
    public void setShowStateBarListener(ShowStateBar showStateBar) {
        this.showStateBar = showStateBar;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        mViewpage.setCurrentItem(1, true);
    }

}
