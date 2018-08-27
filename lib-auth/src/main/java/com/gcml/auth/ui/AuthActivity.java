package com.gcml.auth.ui;

import android.os.Bundle;

import com.billy.cc.core.component.CC;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityAuthBinding;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.app.AppUtils;

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
        binding.setVariable(BR.presenter, this);
        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer level) throws Exception {
                        binding.ivWifiState.setImageLevel(level);
                    }
                });
        AppUtils.AppInfo appInfo = AppUtils.getAppInfo();
        binding.tvAppVersion.setText(appInfo == null ? "": appInfo.getVersionName());
    }

    public void goSignUp(){
        CC.obtainBuilder("com.gcml.old.user.auth")
                .setActionName("signup")
                .build()
                .callAsync();
    }

    public void goSignInByPhone(){
//        CC.obtainBuilder("com.gcml.old.user.auth").setActionName("signin").build().callAsync();
        CC.obtainBuilder("com.gcml.auth.signin")
                .build()
                .callAsync();
    }

    public void goSignInByFace(){
        CC.obtainBuilder("face_recognition")
                .setActionName("To_FaceRecognitionActivity")
                .build()
                .callAsync();
    }

    public void goWifi(){
        CC.obtainBuilder("com.gcml.old.wifi")
                .build()
                .callAsync();
    }

    public void goUserProtocol(){
        CC.obtainBuilder("com.gcml.old.user.auth")
                .setActionName("protocol")
                .build()
                .callAsync();
    }
}
