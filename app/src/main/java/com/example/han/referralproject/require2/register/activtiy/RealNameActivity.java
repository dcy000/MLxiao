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
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.han.referralproject.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_REAL_NAME;

public class RealNameActivity extends BaseActivity {

    @BindView(R.id.ccet_name)
    CanClearEditText ccetName;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    private void initView() {
        ccetName.setIsChinese(true);
    }

    private void initTitle() {
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
                startActivity(new Intent(RealNameActivity.this, WifiConnectActivity.class));
            }
        });

    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        String realName = ccetName.getPhone();
        if (TextUtils.isEmpty(realName)) {
            mlSpeak("请输入姓名");
            return;
        }
        startActivity(new Intent(this, SexActivity.class)
                .putExtra(REGISTER_REAL_NAME, realName)
                .putExtras(getIntent()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
    }
}
