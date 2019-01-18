package com.gcml.module_auth_hospital.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.ui.register.UserRegisters2Activity;

public class UserLoginsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imLoginByIdcard;
    private ImageView imLoginByFinger;
    private ImageView imLoginByFace;
    private ImageView imLoginByIdNumber;
    private TextView tvToRegister;
    private TranslucentToolBar authLoginsToobar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogins);
        initView();
    }

    private void initView() {
        authLoginsToobar = (TranslucentToolBar) findViewById(R.id.auth_logins_tb);
        imLoginByIdcard = (ImageView) findViewById(R.id.im_login_by_idcard);
        imLoginByIdcard.setOnClickListener(this);
        imLoginByFinger = (ImageView) findViewById(R.id.im_login_by_finger);
        imLoginByFinger.setOnClickListener(this);
        imLoginByFace = (ImageView) findViewById(R.id.im_login_by_face);
        imLoginByFace.setOnClickListener(this);
        imLoginByIdNumber = (ImageView) findViewById(R.id.im_login_by_id_number);
        imLoginByIdNumber.setOnClickListener(this);
        tvToRegister = (TextView) findViewById(R.id.tv_to_register);
        tvToRegister.setOnClickListener(this);

        authLoginsToobar.setData("居 民 登 录", 0, "  医生:白求恩", R.drawable.auth_hospital_ic_setting, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                CC.obtainBuilder("com.gcml.old.setting").build().call();
            }
        });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.im_login_by_idcard) {
            startActivity(new Intent(this, ScanIdCardLoginActivity.class));
        } else if (id == R.id.im_login_by_finger) {
        } else if (id == R.id.im_login_by_face) {
        } else if (id == R.id.im_login_by_id_number) {
            startActivity(new Intent(this, IDCardNuberLoginActivity.class));
        } else if (id == R.id.tv_to_register) {
            startActivity(new Intent(this, UserRegisters2Activity.class));
        }
    }
}
