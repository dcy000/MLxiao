package com.example.han.referralproject.require2.register.activtiy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;

public class InputFaceActivity extends BaseActivity {

    private String registerIdCardNumber;
    private String registerPhoneNumber;
    private String registeRrealName;
    private String registerSex;
    private String registerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_face);
        initTitle();
        initData();
    }

    public static final String REGISTER_IDCARD_NUMBER = "registerIdCardNumber";
    public static final String REGISTER_PHONE_NUMBER = "registerPhoneNumber";
    public static final String REGISTER_REAL_NAME = "registeRrealName";
    public static final String REGISTER_SEX = "registerSex";
    public static final String REGISTER_ADDRESS = "registerAddress";

    private void initData() {
        registerIdCardNumber = getIntent().getStringExtra(REGISTER_IDCARD_NUMBER);
        registerPhoneNumber = getIntent().getStringExtra(REGISTER_PHONE_NUMBER);
        registeRrealName = getIntent().getStringExtra(REGISTER_REAL_NAME);
        registerSex = getIntent().getStringExtra(REGISTER_SEX);
        registerAddress = getIntent().getStringExtra(REGISTER_ADDRESS);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("人 脸 录 入");

        mLeftText.setVisibility(View.VISIBLE);
        mLeftView.setVisibility(View.VISIBLE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.yiyua_wifi_icon);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputFaceActivity.this, WifiConnectActivity.class));
            }
        });
    }
}
