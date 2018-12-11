package com.gcml.auth.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityAuthBinding;
import com.gcml.auth.model.ServerBean;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.app.AppUtils;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入服务商名称和登陆密码");
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText et1 = new EditText(this);
        et1.setHint("请输入名称");
        et1.setSingleLine(true);
        final EditText et2 = new EditText(this);
        et2.setHint("请输入密码");
        et2.setSingleLine(true);
        linearLayout.addView(et1);
        linearLayout.addView(et2);
        builder.setView(linearLayout);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = et1.getText().toString().trim();
                String pwd = et2.getText().toString().trim();
                if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)){
                    return;
                }
                UserRepository userRepository = new UserRepository();
                userRepository.getServiceProvider(name, pwd)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxUtils.autoDisposeConverter(AuthActivity.this))
                        .subscribe(new DefaultObserver<ServerBean>() {
                            @Override
                            public void onNext(ServerBean serverBean) {
                                SPUtil.put("header-server",serverBean.getServerId()+"");
                                CC.obtainBuilder("com.gcml.auth.signup")
                                        .build()
                                        .callAsync();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e);
                            }

                            @Override
                            public void onComplete() {
                                Timber.e("onComplete");
                            }
                        });
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void goSignInByPhone() {
        CC.obtainBuilder("com.gcml.auth.signin")
                .build()
                .callAsync();
    }

    public void goSignInByFace() {
        CC.obtainBuilder("com.gcml.auth.face.signin")
                .build()
                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        if (result.isSuccess()) {
                            CC.obtainBuilder("com.gcml.old.home")
                                    .build()
                                    .callAsync();
                        } else {
                            ToastUtils.showShort(result.getErrorMessage());
                        }
                    }
                });
    }

    public void goWifi() {
        CC.obtainBuilder("com.gcml.old.wifi")
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
                "主人，欢迎使用健康管家，如果您已经有账号，请选择手机或人脸登录。如果还没有账号，请点击立即注册。");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }
}
