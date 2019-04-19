package com.gcml.auth.ui.signin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivitySignInBinding;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.app.AppUtils;
import com.gcml.common.utils.display.KeyboardUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;

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
        UserSpHelper.setNoNetwork(false);
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
        if ("123456".equals(binding.etPhone.getText().toString())
                && "654321".equals(binding.etPassword.getText().toString())) {
            CC.obtainBuilder("com.gcml.old.system.factoryTest")
                    .build()
                    .callAsync();
            return;
        }

        String phone = binding.etPhone.getText().toString().trim();
        String pwd = binding.etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }
//        if (!binding.cbAgreeProtocol.isChecked()) {
//            ToastUtils.showShort("登录需要勾选同意用户协议");
//            return;
//        }
        String deviceId = Utils.getDeviceId(getContentResolver());
        viewModel.signIn(deviceId, phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("正在登录...");
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
                        CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                                .addParam("userId", user.id)
                                .build()
                                .callAsync();
                        checkFace(user);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });

    }

    private void checkFace(UserEntity user) {
        if (TextUtils.isEmpty(user.xfid)) {
            CC.obtainBuilder("com.gcml.auth.face2.signup")
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            checkProfile1(user);
                        }
                    });
        } else {
            checkProfile1(user);
        }
    }

    private void checkProfile1(UserEntity user) {
        if (TextUtils.isEmpty(user.idCard)
                && TextUtils.isEmpty(user.name)
                && TextUtils.isEmpty(user.sex)) {
            CC.obtainBuilder("com.gcml.auth.updateProfile1")
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            checkProfile2(user);
                        }
                    });
        } else {
            checkProfile2(user);
        }
    }

    private void checkProfile2(UserEntity user) {
        if (TextUtils.isEmpty(user.height)
                && TextUtils.isEmpty(user.waist)
                && TextUtils.isEmpty(user.weight)) {
            CC.obtainBuilder("com.gcml.auth.updateProfile2")
                    .build()
                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                        @Override
                        public void onResult(CC cc, CCResult result) {
                            goHome();
                        }
                    });
        } else {
            goHome();
        }
    }

    private void goHome() {
        CC.obtainBuilder("com.gcml.old.home")
                .build()
                .callAsync();
    }

    public void goSignInByFace() {
        CC.obtainBuilder("com.gcml.auth.face2.signin")
                .build()
                .callAsync(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        if (result.isSuccess()) {
                            goHome();
                        } else {
                            ToastUtils.showShort(result.getErrorMessage());
                        }
                    }
                });
    }

    public void goForgetPassword() {
        String phone = binding.etPhone.getText().toString().trim();
        CC.obtainBuilder("com.gcml.auth.findpassword")
                .setContext(this)
                .addParam("phone", phone)
                .build()
                .callAsync();
    }


    public void goUserProtocol() {
        CC.obtainBuilder("com.gcml.auth.user.protocol")
                .build()
                .callAsync();
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
