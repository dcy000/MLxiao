package com.gcml.auth.ui.findpassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityFindPasswordBinding;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.KeyboardUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FindPasswordActivity extends BaseActivity<AuthActivityFindPasswordBinding, FindPasswordViewModel> {

    private String mPhone = "";

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_find_password;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
        mPhone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(mPhone)) {
            binding.etPhone.setText(mPhone);
            binding.etPhone.setSelection(mPhone.length());
        }
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
        final String phone = binding.etPhone.getText().toString().trim();
        if (!Utils.isValidPhone(phone)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，请输入正确的手机号码", false);
            ToastUtils.showShort("主人，请输入正确的手机号码");
            return;
        }
        viewModel.hasAccount(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean has) {
                        if (has) {
                            fetchCodeInternal(phone);
                        } else {
                            ToastUtils.showShort("账号不存在！");
                        }
                    }
                });
    }

    private void fetchCodeInternal(String phone) {
        binding.tvCode.setEnabled(false);
        viewModel.fetchCode(phone)
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
                        FindPasswordActivity.this.code = code;
                        ToastUtils.showShort("获取验证码成功");
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "获取验证码成功", false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("获取验证码失败");
                        countDownDisposable.dispose();
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "获取验证码失败", false);
                    }
                });
    }

    private Disposable countDownDisposable = Disposables.empty();

    private void startTimer() {
        countDownDisposable = RxUtils.rxCountDown(1, 60)
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
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        binding.tvCode.setText("获取验证码");
                        binding.tvCode.setEnabled(true);
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        binding.tvCode.setText(
                                String.format(Locale.getDefault(), "已发送（%d）", integer));
                    }
                });
    }

    public void goNext() {
        final String phone = binding.etPhone.getText().toString().trim();
        if (!Utils.isValidPhone(phone)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，请输入正确的手机号码", false);
            ToastUtils.showShort("主人，请输入正确的手机号码");
            return;
        }
        viewModel.hasAccount(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean has) {
                        if (has) {
                            checkCode(phone);
                        } else {
                            ToastUtils.showShort("账号不存在！");
                        }
                    }
                });
    }

    private void checkCode(String phone) {
        String code = binding.etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,请输入验证码", false);
            ToastUtils.showShort("主人,请输入验证码");
            return;
        }

        if (!code.equals(this.code)) {
            ToastUtils.showShort("验证码错误");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "验证码错误", false);
            return;
        }

        CC.obtainBuilder("com.gcml.auth.setpassword")
                .addParam("phone", phone)
                .setContext(FindPasswordActivity.this)
                .build()
                .callAsync();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onStop() {
        super.onStop();
        code = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，请输入您的手机号码");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }
}
