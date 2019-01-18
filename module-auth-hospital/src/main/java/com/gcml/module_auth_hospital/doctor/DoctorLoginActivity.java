package com.gcml.module_auth_hospital.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.server.ServerBean;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.login.UserLogins2Activity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/14.
 */

public class DoctorLoginActivity extends AppCompatActivity implements View.OnClickListener {
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
     * 登陆
     */
    private TextView tvDoctorLoginLogin, serverName;

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
        serverName = findViewById(R.id.tv_server_name);
        tvDoctorLoginLogin.setOnClickListener(this);

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
        int id = v.getId();
        if (id == R.id.iv_auth_doctor_login_setting) {
            toSetting();
        } else if (id == R.id.tv_doctor_login_login) {
            toLogin();
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
        repository.serverSignIn(deviceId, account, passWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<ServerBean>() {
                    @Override
                    public void onNext(ServerBean serverBean) {
                        super.onNext(serverBean);
                        UserSpHelper.setDoctorId(serverBean.serverId + "");
                        startActivity(new Intent(DoctorLoginActivity.this, UserLogins2Activity.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
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

    private void toSetting() {
        CC.obtainBuilder("com.gcml.old.setting").build().call();
    }
}
