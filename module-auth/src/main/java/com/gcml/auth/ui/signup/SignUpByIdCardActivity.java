package com.gcml.auth.ui.signup;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivitySignUpByIdCardBinding;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.KeyboardUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignUpByIdCardActivity extends BaseActivity<AuthActivitySignUpByIdCardBinding, SignUpByIdCardViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_sign_up_by_id_card;
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
        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
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
                        SignUpByIdCardActivity.this.code = code;
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
        if (TextUtils.isEmpty(phone) || phone.length() != 18) {
            binding.tvNext.setEnabled(true);
            ToastUtils.showShort("请输入正确的身份证号码");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入正确的身份证号码");
            return;
        }

        if (!binding.cbAgreeProtocol.isChecked()) {
            binding.tvNext.setEnabled(true);
            ToastUtils.showShort("注册需要勾选同意用户协议");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "注册需要勾选同意用户协议");
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
                        binding.tvNext.setEnabled(true);
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean hasAccount) {
                        if (hasAccount) {
                            ToastUtils.showShort("账号已注册");
                            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "账号已注册", false);
                        } else {
                            trySignUp(phone);
                        }
                    }
                });
    }

    private void trySignUp(String phone) {
        String name = binding.etCode.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            binding.tvNext.setEnabled(true);
            ToastUtils.showShort("请输入真实姓名");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入真实姓名", false);
            return;
        }
        String password = binding.etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)
                || !TextUtils.isDigitsOnly(password)
                || password.length() != 6) {
            binding.tvNext.setEnabled(true);
            ToastUtils.showShort("请输入6位数字密码");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入6位数字密码", false);
            return;
        }


        checkIdCard(phone, password, name);
    }

    private void checkIdCard(String phone, String password, String name) {
        Routerfit.register(AppRouter.class)
                .getBusinessControllerProvider()
                .isIdCardNotExit(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        doSignUp(phone, password, name);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }

    public static final String IDCARD = "idcard";

    private void doSignUp(String phone, String password, String name) {
        String deviceId = Utils.getDeviceId(getApplicationContext().getContentResolver());
        viewModel.signUp(deviceId, phone, password, name)
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
                        Routerfit.register(AppRouter.class)
                                .skipFaceBdSignUpActivity(UserSpHelper.getUserId(), new ActivityCallback() {
                                    @Override
                                    public void onActivityResult(int result, Object data) {
                                        if (result == Activity.RESULT_OK) {
                                            String sResult = data.toString();
                                            if (TextUtils.isEmpty(sResult)) return;
                                            if (sResult.equals("success")) {
                                                Routerfit.register(AppRouter.class)
                                                        .skipSimpleProfileActivity(IDCARD, binding.etPhone.getText().toString().trim(),
                                                                new ActivityCallback() {
                                                                    @Override
                                                                    public void onActivityResult(int result, Object data) {
                                                                        if (result == Activity.RESULT_OK) {
                                                                            String sResult = data.toString();
                                                                            if (TextUtils.equals("success", sResult)) {
                                                                                Routerfit.register(AppRouter.class).skipProfile2Activity(
                                                                                        new ActivityCallback() {
                                                                                            @Override
                                                                                            public void onActivityResult(int result, Object data) {
                                                                                                if (result == Activity.RESULT_OK) {
                                                                                                    if (TextUtils.equals(data.toString(), "success")) {
                                                                                                        Routerfit.register(AppRouter.class).skipOnlineDoctorListActivity("contract");
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                );
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                            } else if (sResult.equals("failed")) {
                                                ToastUtils.showShort("录入人脸失败");
                                            }
                                        }
                                    }
                                });
//                        CC.obtainBuilder("com.gcml.auth.face2.signup")
//                                .build()
//                                .callAsyncCallbackOnMainThread(new IComponentCallback() {
//                                    @Override
//                                    public void onResult(CC cc, CCResult result) {
//                                        if (result.isSuccess()) {
//
//
//                                        }
//                                    }
//                                });
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        String message = throwable.getMessage();
                        ToastUtils.showShort(message);
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), message);
                    }
                });
    }

    public void goUserProtocol() {
        Routerfit.register(AppRouter.class).skipUserProtocolActivity();
    }

    public void goIdCardRegister() {
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
                "请输入身份证号进行注册。");
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