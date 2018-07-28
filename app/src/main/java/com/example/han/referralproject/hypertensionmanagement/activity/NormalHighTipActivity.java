package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.hypertensionmanagement.fragment.WarmNoticeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 高血压风险评估 下一步
 */
public class NormalHighTipActivity extends BaseActivity implements WarmNoticeFragment.OnButtonClickListener {
    public static final String CONTENT = " 您好,根据系统中显示的三次数据判定,您的血压疑似在正常高值状态,为给您提供更精确预防高血压方案,以下问题需要您认真作答";
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_high_tip);
        ButterKnife.bind(this);
        initTitle();
        mlSpeak(CONTENT);
        initView();
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        WarmNoticeFragment fragment = WarmNoticeFragment.getInstance(CONTENT, "下一步");
        fragment.setListener(this);
        transaction.replace(R.id.fl_container, fragment).commitAllowingStateLoss();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(v -> startActivity(new Intent(NormalHighTipActivity.this, WifiConnectActivity.class)));
    }

    @Override
    public void onFragmentBtnClick() {

    }

    @Override
    public void onFragmentBtnTimeOut() {

    }
}