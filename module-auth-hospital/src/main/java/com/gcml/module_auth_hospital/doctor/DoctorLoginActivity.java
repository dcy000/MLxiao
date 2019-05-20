package com.gcml.module_auth_hospital.doctor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.DoctorEntity;
import com.gcml.module_auth_hospital.model.ServerBean;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.login.UserLogins2Activity;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/14.
 */

public class DoctorLoginActivity extends ToolbarBaseActivity implements View.OnClickListener {
    private ImageView ivAuthDoctorLoginSetting;
    /**
     * 请输入您的医生账号
     */
    private EditText etDoctorLoginAccunt;
    /**
     * 请输入你的密码
     */
    private EditText etDoctorLoginPassword;
    /**
     * 登录
     */
    private TextView tvDoctorLoginLogin, serverName, tvDoctorChangeLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        initView();
        serverInfo();
    }

    private void initView() {
        ivAuthDoctorLoginSetting = (ImageView) findViewById(R.id.iv_auth_doctor_login_setting);
        ivAuthDoctorLoginSetting.setOnClickListener(this);
        etDoctorLoginAccunt = (EditText) findViewById(R.id.et_doctor_login_accunt);
        etDoctorLoginPassword = (EditText) findViewById(R.id.et_doctor_login_password);
        tvDoctorLoginLogin = (TextView) findViewById(R.id.tv_doctor_login_login);
        tvDoctorChangeLogin = (TextView) findViewById(R.id.tv_change_doctor_login_type);
        serverName = findViewById(R.id.tv_server_name);
        tvDoctorLoginLogin.setOnClickListener(this);
        tvDoctorChangeLogin.setOnClickListener(this);
        etDoctorLoginAccunt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable account) {
                String trim = account.toString().trim();
                String passWord = etDoctorLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || TextUtils.isEmpty(passWord)) {
                    tvDoctorLoginLogin.setEnabled(false);
                } else {
                    tvDoctorLoginLogin.setEnabled(true);
                }
            }
        });


        etDoctorLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable passWord) {
                String trim = passWord.toString().trim();
                String account = tvDoctorLoginLogin.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || TextUtils.isEmpty(account)) {
                    tvDoctorLoginLogin.setEnabled(false);
                } else {
                    tvDoctorLoginLogin.setEnabled(true);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.iv_auth_doctor_login_setting) {
            Routerfit.register(AppRouter.class).skipSettingActivity();
        } else if (id == R.id.tv_doctor_login_login) {
            toLogin();
        } else if (id == R.id.tv_change_doctor_login_type) {
            Routerfit.register(AppRouter.class).getFaceProvider().getFaceId(UserSpHelper.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.observers.DefaultObserver<String>() {
                        @Override
                        public void onNext(String faceId) {
                            Routerfit.register(AppRouter.class).skipFaceBdSignInActivity(
                                    false, false, faceId, true, new ActivityCallback() {
                                        @Override
                                        public void onActivityResult(int result, Object data) {
                                            if (result == Activity.RESULT_OK) {
                                                String sResult = data.toString();
                                                if (TextUtils.isEmpty(sResult))
                                                    return;
                                                if (sResult.equals("success") || sResult.equals("skip")) {
                                                    Routerfit.register(AppRouter.class).skipUserLogins2Activity();
                                                    finish();
                                                } else if (sResult.equals("failed")) {
                                                    ToastUtils.showShort("人脸验证失败");
                                                }

                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }


    }

    UserRepository repository = new UserRepository();

    private void toLogin() {
        String account = etDoctorLoginAccunt.getText().toString().trim();
        String passWord = etDoctorLoginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShort("请输入用户名");
            return;
        }

        if (TextUtils.isEmpty(passWord)) {
            ToastUtils.showShort("请输入密码");
            return;
        }

        String deviceId = Utils.getDeviceId(getContentResolver());
        repository.doctorSignIn(deviceId, account, passWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("登录中");
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<DoctorEntity>() {
                    @Override
                    public void onNext(DoctorEntity serverBean) {
                        super.onNext(serverBean);
                        dismissLoading();
//                        UserSpHelper.setDoctorId(serverBean.docterid + "");
                        startActivity(new Intent(DoctorLoginActivity.this, UserLogins2Activity.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissLoading();
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });

    }


    private void serverInfo() {
        repository.getServer("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<ServerBean>() {
                    @Override
                    public void onNext(ServerBean serverBean) {
                        super.onNext(serverBean);
                        serverName.setText(serverBean.serverName);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }
}
