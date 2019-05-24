package com.gcml.auth.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityAuthBinding;
import com.gcml.auth.ui.signin.nonetwork.SignInNoNetworkActivity;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.app.AppUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

@Route(path = "/auth/auth/activity")
//@Route(path = "/auth/hospital/user/logins2/activity")
public class AuthActivity extends BaseActivity<AuthActivityAuthBinding, AuthViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_auth;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        showIpInputDialog();
        init(savedInstanceState);
    }

    private void showIpInputDialog() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_ip_input)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        EditText ip = holder.getView(R.id.et_input);

                        holder.getView(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String trim = ip.getText().toString().trim();
                                if (TextUtils.isEmpty(trim)) {
                                    ToastUtils.showShort("输入的IP不正确");
                                    return;
                                }
                                RetrofitUrlManager.getInstance().setGlobalDomain("http://" + trim);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(300)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
        binding.setAuthViewModel(viewModel);
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

    public void goNoNetwork() {
        Intent intent = new Intent(this, SignInNoNetworkActivity.class);
        startActivity(intent);
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
