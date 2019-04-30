package com.gcml.auth.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityAuthBinding;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.app.AppUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

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
        binding.tvAppVersion.setText(appInfo == null ? "" : appInfo.getVersionName());
    }

    public void goSignUp() {
        Routerfit.register(AppRouter.class).skipSignUpActivity();
    }

    public void goSignInByPhone() {
        Routerfit.register(AppRouter.class).skipSignInActivity();
    }

    public void goSignInByFace() {
//        CC.obtainBuilder("com.gcml.auth.face2.signin")
//                .build()
//                .callAsyncCallbackOnMainThread(new IComponentCallback() {
//                    @Override
//                    public void onResult(CC cc, CCResult result) {
//                        if (result.isSuccess()) {
//                            Routerfit.register(AppRouter.class).skipMainActivity();
//                        } else {
//                            ToastUtils.showShort(result.getErrorMessage());
//                        }
//                    }
//                });
        Routerfit.register(AppRouter.class)
                .skipFaceBdSignInActivity(false, false, null, true, new ActivityCallback() {
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

    public void goWifi() {
        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
    }

    public void goUserProtocol() {
        Routerfit.register(AppRouter.class).skipUserProtocolActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                "欢迎使用健康管家，如果您已经有账号，请选择手机或人脸登录。如果还没有账号，请点击立即注册。");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }
}
