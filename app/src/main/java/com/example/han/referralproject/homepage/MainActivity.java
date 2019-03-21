package com.example.han.referralproject.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.StatusBarFragment;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.example.module_control_volume.VolumeControlFloatwindow;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.medlink.danbogh.call2.NimAccountHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 16:24
 * created by:gzq
 * description:新的主界面
 */
public class MainActivity extends BaseActivity implements HttpListener<AccessToken> {

    private ViewPager mViewpage;
    private LinearLayout mNewmainBottomIndicator;
    private View mIndicatorLeft;
//    private View mIndicatorRight;
    private View mIndicatorMiddle;
    private List<Fragment> fragments;
    private NewMain1Fragment newMain1Fragment;
    private NewMain2Fragment newMain2Fragment;
    private NewMain3Fragment newMain3Fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        speak(R.string.tips_splash);
        initView();
        initFragments();
        initViewpage();
        initAToken();
        //启动音量控制悬浮按钮
        VolumeControlFloatwindow.init(this.getApplicationContext());
    }

    private void initAToken() {
        AccessTokenModel tokenModel = new AccessTokenModel();
        tokenModel.getAccessToken(this);
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
                } else if (position == 1) {
                    mIndicatorLeft.setVisibility(View.INVISIBLE);
                    mIndicatorMiddle.setVisibility(View.VISIBLE);
//                    mIndicatorRight.setVisibility(View.INVISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(true);
                    }
                } else if (position == 2) {
                    mIndicatorLeft.setVisibility(View.INVISIBLE);
                    mIndicatorMiddle.setVisibility(View.INVISIBLE);
//                    mIndicatorRight.setVisibility(View.VISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(true);
                    }
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
        mNewmainBottomIndicator = findViewById(R.id.newmain_bottom_indicator);
        mIndicatorLeft = findViewById(R.id.indicator_left);
//        mIndicatorRight = findViewById(R.id.indicator_right);
        mIndicatorMiddle = findViewById(R.id.indicator_middle);
    }


    @Override
    protected void onResume() {
        setEnableListeningLoop(false);
        super.onResume();
        getPersonInfo();
    }

    //获取个人信息，得到网易账号登录所需的账号和密码
    private void getPersonInfo() {
        if ("123456".equals(UserSpHelper.getUserId())) {
            return;
        }
        Observable<UserEntity> rxUsers = CC.obtainBuilder("com.gcml.auth.getUser")
                .build()
                .call()
                .getDataItem("data");
        if (rxUsers == null) {
            CC.obtainBuilder("com.gcml.auth")
                    .build()
                    .call();
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
                        NimAccountHelper.getInstance().login(wyyxId, wyyxPwd, null);
                        CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                                .addParam("userId", user.id)
                                .build()
                                .callAsync();
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

    public interface ShowStateBar {
        void showStateBar(boolean isshow);
    }

    @Override
    public void onSuccess(AccessToken data) {
        EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
    }

    @Override
    public void onError() {
        ToastUtils.showShort("初始化AK失败");
    }

    @Override
    public void onComplete() {

    }


}
