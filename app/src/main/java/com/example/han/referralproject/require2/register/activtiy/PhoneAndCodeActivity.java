package com.example.han.referralproject.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.require2.wrap.PhoneVerificationCodeView;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneAndCodeActivity extends BaseActivity implements PhoneVerificationCodeView.OnSendClickListener {
    public static final String FROM_WHERE = "from_where";
    public static final String FROM_REGISTER_BY_IDCARD = "register_by_idCard";
    public static final String FROM_REGISTER_BY_IDCARD_NUMBER = "register_by_idCard_number";
    @BindView(R.id.phone_view)
    PhoneVerificationCodeView phoneView;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_and_code);
        ButterKnife.bind(this);
        intTitle();
        initEvent();
    }

    private void intTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 注 册");

        mLeftText.setVisibility(View.VISIBLE);
        mLeftView.setVisibility(View.VISIBLE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.yiyua_wifi_icon);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneAndCodeActivity.this, WifiConnectActivity.class));
            }
        });
    }

    private void initEvent() {
        phoneView.setListener(this);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String code = phoneView.getCode();
        if (TextUtils.isEmpty(code)) {
            speak("请输入验证码");
            return;
        }

        String fromWhere = getIntent().getStringExtra(FROM_WHERE);
        if (fromWhere.equals(FROM_REGISTER_BY_IDCARD)) {
            // TODO: 2018/7/12  录入人脸 
        } else if (fromWhere.equals(FROM_REGISTER_BY_IDCARD_NUMBER)) {
            startActivity(new Intent(PhoneAndCodeActivity.this, RealNameActivity.class));
        }


    }


    @Override
    public void onSendCode(String phone) {
        //验证手机号注册与否

    }


    public void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
    }
}