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
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_auth_hospital.R;

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
    private TextView tvDoctorLoginLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        initView();
    }

    private void initView() {
        ivAuthDoctorLoginSetting = (ImageView) findViewById(R.id.iv_auth_doctor_login_setting);
        ivAuthDoctorLoginSetting.setOnClickListener(this);
        etDoctorLoginAccunt = (EditText) findViewById(R.id.et_doctor_login_accunt);
        etDoctorLoginPassword = (EditText) findViewById(R.id.et_doctor_login_password);
        tvDoctorLoginLogin = (TextView) findViewById(R.id.tv_doctor_login_login);
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
    }

    private void toSetting() {
        startActivity(new Intent(this, DoctorSettingActivity.class));
    }
}
