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

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
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
        binding.tvTitle.setText(String.format("请输入手机%s收到的验证码", mPhone));
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

    private String code = "";

    public void fetchCode() {
        binding.tvCode.setEnabled(false);
        viewModel.fetchCode(mPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        startTimer();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String code) {
                        SetPasswordActivity.this.code = code;
                        ToastUtils.showShort("获取验证码成功");
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "获取验证码成功", true);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("获取验证码失败");
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "获取验证码失败", true);
                    }
                });
    }

    private void startTimer() {
        RxUtils.rxCountDown(1, 3)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        binding.tvCode.setEnabled(false);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        binding.tvCode.setText("获取验证码");
                        binding.tvCode.setEnabled(true);
                    }
                })
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        binding.tvCode.setText(
                                String.format(Locale.getDefault(), "已发送（%d）", integer));
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe();
    }

    public void goNext() {
        String code = binding.etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,请输入验证码", true);
            ToastUtils.showShort("主人,请输入验证码");
            return;
        }

        if (code.equals(this.code)) {
            updatePassword();
        } else {
            ToastUtils.showShort("验证码错误");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "验证码错误", true);
        }
    }

    private void updatePassword() {
        String pwd = binding.etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd) || pwd.length() != 6) {
            ToastUtils.showShort("请输入6位数字密码");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入6位数字密码", true);
            return;
        }
        viewModel.updatePassword(mPhone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            CC.obtainBuilder("com.gcml.user.auth.signin")
                                    .build()
                                    .callAsync();
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort("设置密码失败");
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，请输入验证码及新的6位数字密码", true);
    }

    @Override
    protected void onPause() {
        MLVoiceSynthetize.stop();
        super.onPause();
    }
}
