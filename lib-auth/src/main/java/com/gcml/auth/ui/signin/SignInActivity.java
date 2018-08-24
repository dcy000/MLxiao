package com.gcml.auth.ui.signin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.billy.cc.core.component.CC;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivitySignInBinding;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.lib_utils.app.AppUtils;
import com.gcml.lib_utils.display.KeyboardUtils;
import com.gcml.lib_utils.display.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends BaseActivity<AuthActivitySignInBinding, SignInViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_sign_in;
    }

    @Override
    protected int variableId() {
        return BR.signInViewModel;
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
        AppUtils.AppInfo appInfo = AppUtils.getAppInfo();
        binding.tvAppVersion.setText(appInfo == null ? "" : appInfo.getVersionName());
        binding.etPhone.addTextChangedListener(inputWatcher);
        binding.etPassword.addTextChangedListener(inputWatcher);
        binding.cbAgreeProtocol.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkInput();
                }
            };

    private TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkInput();
        }
    };

    private boolean checkInput() {
        boolean validPassword = false;
        boolean validPhone = false;
        boolean checked = binding.cbAgreeProtocol.isChecked();
        String phone = binding.etPhone.toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            validPassword = true;
        }
        String password = binding.etPassword.toString().trim();
        if (!TextUtils.isEmpty(password)) {
            validPhone = true;
        }
        boolean enabled = validPassword && validPhone && checked;
        binding.tvSignIn.setEnabled(enabled);
        return enabled;
    }


    public void goBack() {
        finish();
    }

    public void goWifi() {
        CC.obtainBuilder("com.gcml.old.wifi").build().callAsync();
    }

    public void rootOnClick() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(this, view);
        }
    }

    public void signIn() {
//        if ("123456".equals(binding.etPhone.getText().toString()) && "654321".equals(binding.etPassword.getText().toString())) {
//            Bundle bundle = new Bundle();
//            bundle.putBoolean("isTest",true);
////            MyApplication.getInstance().userId = "123456";
////            CCFaceRecognitionActions.jump2FaceRecognitionActivity(this,bundle);
//
//
//            finish();
//            return;
//        }

        String phone = binding.etPhone.getText().toString().trim();
        String pwd = binding.etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("手机号或密码不能为空");
            return;
        }
        if (!binding.cbAgreeProtocol.isChecked()) {
            ToastUtils.showShort("登录需要勾选同意用户协议");
            return;
        }

        viewModel.signIn(phone, pwd)
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
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        CC.obtainBuilder("com.gcml.old.home")
                                .addParam("userId", user.getId())
                                .build()
                                .callAsync();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("手机号或密码错误");
                    }
                });

    }

    public void goSignInByFace() {
        CC.obtainBuilder("face_recognition")
                .setActionName("To_FaceRecognitionActivity")
                .build()
                .callAsync();
    }

    public void goForgetPassword() {
        String phone = binding.etPhone.getText().toString().trim();
        CC.obtainBuilder("com.gcml.user.auth.findpassword")
                .setContext(this)
                .addParam("phone", phone)
                .build()
                .callAsync();
    }

    public void goUserProtocol() {
        CC.obtainBuilder("com.gcml.old.user.auth")
                .setActionName("protocol")
                .build()
                .callAsync();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
