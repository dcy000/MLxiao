package com.gcml.auth.ui.findpassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivitySetPasswordBinding;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.display.KeyboardUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SetPasswordActivity extends BaseActivity<AuthActivitySetPasswordBinding, SetPasswordViewModel> {

    private String mPhone;

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_set_password;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
        mPhone = getIntent().getStringExtra("phone");
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

    public void rootOnClick() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(this, view);
        }
    }

    public void goBack() {
        finish();
    }

    public void goWifi() {
        CC.obtainBuilder("com.gcml.old.wifi").build().callAsync();
    }

    public void goNext() {
        String pwd = binding.etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd) || pwd.length() != 6) {
            ToastUtils.showShort("请输入6位数字密码");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入6位数字密码", false);
            return;
        }
        viewModel.updatePassword(mPhone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>(){
                    @Override
                    public void onNext(Object o) {
                        CC.obtainBuilder("com.gcml.auth.signin")
                                .build()
                                .callAsync();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("设置密码失败");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "您好，请输入新的6位数字密码", false);
    }

    @Override
    protected void onPause() {
        MLVoiceSynthetize.stop();
        super.onPause();
    }
}
