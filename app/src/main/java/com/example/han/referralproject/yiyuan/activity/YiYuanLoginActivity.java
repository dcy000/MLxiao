package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.idcard.SignInIdCardActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YiYuanLoginActivity extends BaseActivity {

    @BindView(R.id.login_idcard)
    ImageView loginIdcard;
    @BindView(R.id.login_youke)
    ImageView loginYouke;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_yuan_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_idcard, R.id.login_youke})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_idcard:
                startActivity(new Intent(YiYuanLoginActivity.this, SignInIdCardActivity.class));
                break;
            case R.id.login_youke:

                break;
        }
    }
}
