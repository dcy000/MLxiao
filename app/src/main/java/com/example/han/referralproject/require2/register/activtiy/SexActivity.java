package com.example.han.referralproject.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SexActivity extends BaseActivity {

    @BindView(R.id.tv_sex_man)
    TextView tvSexMan;
    @BindView(R.id.tv_sex_women)
    TextView tvSexWomen;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        ButterKnife.bind(this);
        initTitle();
        initView();
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
                startActivity(new Intent(SexActivity.this, WifiConnectActivity.class));
            }
        });
    }


    private void clickMan(boolean man) {
        tvSexMan.setSelected(man);
        tvSexWomen.setSelected(!man);
    }

    private void initView() {
        clickMan(true);
    }

    @OnClick({R.id.tv_sex_man, R.id.tv_sex_women, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sex_man:
                clickMan(true);
                break;
            case R.id.tv_sex_women:
                clickMan(false);
                break;
            case R.id.tv_next:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
    }
}
