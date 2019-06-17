package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.JpushAliasUtils;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
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

public class RegisterSuccessActivity extends ToolbarBaseActivity implements View.OnClickListener {
    private TranslucentToolBar tbAuthRegisterSuccess;
    private TextView tvAuthRegisterSuccessComplete;
    private UserRepository repository = new UserRepository();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activtiy_register_success);
        initView();
//        toOtherPage();
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

        tbAuthRegisterSuccess.setData("账 户 注 册",
                0, null,
                0, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
        setWifiLevel(tbAuthRegisterSuccess);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_auth_register_success_complete) {
            login();
        }
    }

    private void login() {

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String passWord = intent.getStringExtra("passWord");
        String idCardNumber = intent.getStringExtra("idCardNumber");
        if (TextUtils.isEmpty(idCardNumber)) {
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            return;
        }
        UserPostBody body = new UserPostBody();
        body.password = passWord;
        body.sfz = idCardNumber;
        repository
                .signInByIdCard(body)
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
                        dismissLoading();
                        JpushAliasUtils.setAlias(user.id);
                        startActivity(new Intent(RegisterSuccessActivity.this, ImproveInformationActivity.class));
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

    private void toLogin() {
        //关闭 注册时的 扫面sfz和信息录入页面
        finish();
        startActivity(new Intent(this, ScanIdCardRegisterActivity.class));
    }

}
