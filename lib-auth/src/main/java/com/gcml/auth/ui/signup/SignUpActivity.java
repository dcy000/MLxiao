package com.gcml.auth.ui.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivitySignUpBinding;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.lib_utils.display.KeyboardUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignUpActivity extends BaseActivity<AuthActivitySignUpBinding, SignUpViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_sign_up;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
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
        String phone = binding.etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            binding.tvCode.setEnabled(true);
            ToastUtils.showShort("请输入正确的手机号");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入正确的手机号", false);
            return;
        }
        viewModel.hasAccount(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("正在加载...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean hasAccount) {
                        if (hasAccount) {
                            binding.tvCode.setEnabled(true);
                            ToastUtils.showShort("手机号码已注册");
                            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "手机号码已注册", false);
                        } else {
                            doFetchCode(phone);
                        }
                    }
                });
    }

    private void doFetchCode(String phone) {
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
                        SignUpActivity.this.code = code;
                        ToastUtils.showShort("获取验证码成功");
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "获取验证码成功", false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        countDownDisposable.dispose();
                        binding.tvCode.setEnabled(true);
                        ToastUtils.showShort(throwable.getMessage());
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), throwable.getMessage(), false);
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
        String phone = binding.etPhone.getText().toString().trim();
        binding.tvNext.setEnabled(false);
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            binding.tvNext.setEnabled(true);
            ToastUtils.showShort("请输入正确的手机号");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入正确的手机号");
            return;
        }

        if (!binding.cbAgreeProtocol.isChecked()) {
            binding.tvNext.setEnabled(true);
            ToastUtils.showShort("登录需要勾选同意用户协议");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "登录需要勾选同意用户协议");
            return;
        }

        viewModel.hasAccount(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("正在加载...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean hasAccount) {
                        if (hasAccount) {
                            binding.tvNext.setEnabled(true);
                            ToastUtils.showShort("账号已注册");
                            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "账号已注册", false);
                        } else {
                            trySignUp(phone);
                        }
                    }
                });
    }

    private void trySignUp(String phone) {
        String code = binding.etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code) || !code.equals(this.code)) {
            binding.tvNext.setEnabled(true);
            ToastUtils.showShort("验证码错误");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "验证码错误", false);
            return;
        }
        String password = binding.etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)
                || !TextUtils.isDigitsOnly(password)
                || password.length() != 6) {
            binding.tvNext.setEnabled(true);
            ToastUtils.showShort("主人,请输入6位数字密码");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,请输入6位数字密码", false);
            return;
        }
        doSignUp(phone, password);
    }

    private void doSignUp(String phone, String password) {
        String deviceId = Utils.getDeviceId(getContentResolver());
        viewModel.signUp(deviceId, phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("正在注册...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        binding.tvNext.setEnabled(true);
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        CC.obtainBuilder("com.gcml.auth.face.signup")
                                .build()
                                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                                    @Override
                                    public void onResult(CC cc, CCResult result) {
                                        if (result.isSuccess()) {
                                            CC.obtainBuilder("com.gcml.auth.updateSimpleProfile")
                                                    .build()
                                                    .callAsync();
                                        }
                                    }
                                });
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("注册失败");
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "注册失败");
                    }
                });
    }

    public void goUserProtocol() {
        CC.obtainBuilder("com.gcml.old.user.auth")
                .setActionName("protocol")
                .build()
                .callAsync();
    }

    @Override
    protected void onStop() {
        super.onStop();
        code = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                "请输入您的手机号和密码进行登录。");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
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
