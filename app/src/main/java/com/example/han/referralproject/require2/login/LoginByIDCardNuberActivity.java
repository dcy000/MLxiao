package com.example.han.referralproject.require2.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.example.han.referralproject.yiyuan.activity.InquiryAndFileActivity;
import com.example.han.referralproject.yiyuan.activity.InquiryAndFileEndActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginByIDCardNuberActivity extends BaseActivity {

    @BindView(R.id.tv_phone_number_notice)
    CanClearEditText ccetIdNumber;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.textView17)
    TextView textView17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_idcard_nuber);
        ButterKnife.bind(this);
        intTitle();
    }

    private void intTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 登 录");

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.yiyua_wifi_icon);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginByIDCardNuberActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入身份证号码登录");
    }


    @Override
    protected void onResume() {
        super.onResume();
        setEnableListeningLoop(false);
        setDisableGlobalListen(true);
    }

    @OnClick({R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                startActivity(new Intent(this, InquiryAndFileActivity.class));
                break;
        }
    }
}
