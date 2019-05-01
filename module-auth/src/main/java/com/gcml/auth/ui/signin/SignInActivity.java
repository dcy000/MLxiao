package com.gcml.auth.ui.signin;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivitySignInBinding;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.JpushAliasUtils;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.app.AppUtils;
import com.gcml.common.utils.display.KeyboardUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/auth/signin/activity")
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
        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
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
            Routerfit.register(AppRouter.class).skipFactoryTestActivity();
            return;
        }

        String phone = binding.etPhone.getText().toString().trim();
        String pwd = binding.etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("手机号或身份证号码不能为空");
            return;
        }
        if (phone.length() != 11 && phone.length() != 18) {
            ToastUtils.showShort("请输入正确的手机号或身份证号码");
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
        LoginByPhone(phone, pwd);
    }

    private void LoginByPhone(String phone, String pwd) {
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
                        JpushAliasUtils.setAlias(user.id);
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
            Routerfit.register(AppRouter.class)
                    .skipFaceBdSignUpActivity(UserSpHelper.getUserId(), new ActivityCallback() {
                        @Override
                        public void onActivityResult(int result, Object data) {
                            if (result == Activity.RESULT_OK) {
                                String sResult = data.toString();
                                if (TextUtils.isEmpty(sResult)) return;
                                if (sResult.equals("success")) {
//                                    CC.obtainBuilder("com.gcml.auth.face.joingroup")
//                                            .build()
//                                            .callAsync();
                                } else if (sResult.equals("failed")) {
                                    ToastUtils.showShort("录入人脸失败");
                                }
                                checkProfile1(user);
                            }
                        }
                    });
//            CC.obtainBuilder("com.gcml.auth.face2.signup")
//                    .build()
//                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
//                        @Override
//                        public void onResult(CC cc, CCResult result) {
//                            if (result.isSuccess()) {
//                                CC.obtainBuilder("com.gcml.auth.face.joingroup")
//                                        .build()
//                                        .callAsync();
//                            }
//                            checkProfile1(user);
//                        }
//                    });
        } else {
//            CC.obtainBuilder("com.gcml.auth.face.joingroup")
//                    .build()
//                    .callAsync();
            checkProfile1(user);
        }
    }

    private void checkProfile1(UserEntity user) {
        if (TextUtils.isEmpty(user.idCard)
                && TextUtils.isEmpty(user.name)
                && TextUtils.isEmpty(user.sex)) {
            Routerfit.register(AppRouter.class)
                    .skipSimpleProfileActivity(null, null, new ActivityCallback() {
                        @Override
                        public void onActivityResult(int result, Object data) {
                            if (result == Activity.RESULT_OK) {
                                checkProfile2(user);
                            }
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
            Routerfit.register(AppRouter.class)
                    .skipProfile2Activity(new ActivityCallback() {
                        @Override
                        public void onActivityResult(int result, Object data) {
                            if (result == Activity.RESULT_OK) {
                                Routerfit.register(AppRouter.class).skipMainActivity();
                            }
                        }
                    });
        } else {
            Routerfit.register(AppRouter.class).skipMainActivity();
        }
    }

    public void goSignInByFace() {
        Routerfit.register(AppRouter.class)
                .getFaceProvider()
                .getFaceId(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new io.reactivex.observers.DefaultObserver<String>() {
                    @Override
                    public void onNext(String faceId) {
                        Routerfit.register(AppRouter.class)
                                .skipFaceBdSignInActivity(false, false, faceId, true, new ActivityCallback() {
                                    @Override
                                    public void onActivityResult(int result, Object data) {
                                        if (result == Activity.RESULT_OK) {
                                            String sResult = data.toString();
                                            if (TextUtils.isEmpty(sResult))
                                                return;
                                            if (sResult.equals("success")) {
                                                Routerfit.register(AppRouter.class).skipMainActivity();
                                            } else if (sResult.equals("failed")) {
                                                ToastUtils.showShort("人脸登录失败");
                                            }

                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("请先注册人脸！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void goForgetPassword() {
        String phone = binding.etPhone.getText().toString().trim();
        Routerfit.register(AppRouter.class).skipFindPasswordActivity(phone);
    }


    public void goUserProtocol() {
        Routerfit.register(AppRouter.class).skipUserProtocolActivity();
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
