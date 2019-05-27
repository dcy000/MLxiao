package com.gcml.module_auth_hospital.ui.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import com.gcml.common.LazyFragment;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model2.UserRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserInfoActivity extends ToolbarBaseActivity {

    private RadioGroup rgTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("个  人  资  料");

        rgTabs = findViewById(R.id.rgTabs);


        rgTabs.setOnCheckedChangeListener(onCheckedChangeListener);
        rgTabs.check(R.id.rbUserBaseInfo);
        showFragment(0);
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rbUserBaseInfo) {
                showFragment(0);
            } else if (checkedId == R.id.rbAccountSettings) {
                showFragment(1);
            }
        }
    };

    private int i = 0;

    private LazyFragment userInfoBaseFragment;
    private LazyFragment userInfoAccountFragment;

    private void showFragment(int i) {
        this.i = i;
        FragmentManager fm = getSupportFragmentManager();
        String tag = null;
        LazyFragment fragmentToShow = null;
        LazyFragment lastFragment = null;
        if (i == 0) {
            tag = UserInfoBaseFragment.class.getName();
            if (userInfoBaseFragment == null) {
                fragmentToShow = (LazyFragment) fm.findFragmentByTag(tag);
                if (fragmentToShow == null) {
                    fragmentToShow = new UserInfoBaseFragment();
                }
                userInfoBaseFragment = fragmentToShow;
            } else {
                fragmentToShow = userInfoBaseFragment;
            }
            lastFragment = userInfoAccountFragment;
        } else if (i == 1) {
            tag = UserInfoAccountFragment.class.getName();
            if (userInfoAccountFragment == null) {
                fragmentToShow = (LazyFragment) fm.findFragmentByTag(tag);
                if (fragmentToShow == null) {
                    fragmentToShow = new UserInfoAccountFragment();
                }
                userInfoAccountFragment = fragmentToShow;
            } else {
                fragmentToShow = userInfoAccountFragment;
            }
            lastFragment = userInfoBaseFragment;
        }

        if (fragmentToShow != null) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (lastFragment != null && lastFragment.isAdded()) {
                transaction.hide(lastFragment);
            }
            if (fragmentToShow.isAdded()) {
                transaction.show(fragmentToShow);
            } else {
                transaction.add(R.id.flContainer, fragmentToShow, tag);
            }
            transaction.commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
    }

    private UserRepository userRepository = new UserRepository();

    private UserEntity mUser;

    void showUser(UserEntity user) {

    }

    private void getData() {
        userRepository.getUserInfoByToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        showUser(user);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
