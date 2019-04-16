package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.IConstant;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/14.
 */

public class RegisterSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TranslucentToolBar tbAuthRegisterSuccess;
    private TextView tvAuthRegisterSuccessComplete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_register_success);
        initView();
        toOtherPage();
    }

    private void toOtherPage() {
        RxUtils.rxTimer(3)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(aLong -> {
                    login();
                });


    }

    private void initView() {
        tbAuthRegisterSuccess = (TranslucentToolBar) findViewById(R.id.tb_auth_register_success);
        tvAuthRegisterSuccessComplete = (TextView) findViewById(R.id.tv_auth_register_success_complete);
        tvAuthRegisterSuccessComplete.setOnClickListener(this);

        tbAuthRegisterSuccess.setData(getString(R.string.loign_register_registers_title),
               0, null,
               0, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        onRightClickWithPermission(new IAction() {
                            @Override
                            public void action() {
                                CC.obtainBuilder("com.gcml.old.setting").build().call();
                            }
                        });
                    }
                });
        setWifiLevel(tbAuthRegisterSuccess);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_auth_register_success_complete) {
            login();
        }
    }

    private void login() {

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String idcard = intent.getStringExtra("idcard");
        if (TextUtils.isEmpty(idcard)) {
            return;
        }

        repository
                .signInByIdCard(Utils.getDeviceId(UM.getApp().getContentResolver()), idcard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading(getString(R.string.loign_login_logining_tips));
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
                        dismissLoading();
                        CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                                .addParam("userId", user.id)
                                .build()
                                .callAsync();
                        ToastUtils.showLong(getString(R.string.loign_login_sucess_tips));
                        toHome();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        dismissLoading();
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                        toLogin();
                    }
                });
    }

    UserRepository repository = new UserRepository();

    private void toHome() {
        finish();
//        startActivity(new Intent(this, RegisterSuccessActivity.class));
        CC.obtainBuilder(IConstant.KEY_INUIRY_ENTRY).build().callAsync();
    }

    private void toLogin() {
        //关闭 注册时的 扫面sfz和信息录入页面
        finish();
        startActivity(new Intent(this, ScanIdCardRegisterActivity.class));
    }


}
