package com.gcml.auth.ui;

import android.os.Bundle;

import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityAuthBinding;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.RxUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AuthActivity extends BaseActivity<AuthActivityAuthBinding, AuthViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_auth;
    }

    @Override
    protected int variableId() {
        return BR.authViewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        binding.ivWifiState.setImageLevel(integer);
                    }
                });
    }
}
