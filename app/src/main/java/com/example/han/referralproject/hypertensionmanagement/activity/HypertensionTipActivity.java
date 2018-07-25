package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.hypertensionmanagement.fragment.WarmNoticeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 疑似患有高血压
 */
public class HypertensionTipActivity extends BaseActivity implements WarmNoticeFragment.OnButtonClickListener {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    public static final String CONTENT = " 您好，根据系统中显示的3次数据判定,您疑似患有高血压,以下问题需要您认真作答";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hypertension_tip);
        ButterKnife.bind(this);
        initTitle();
        mlSpeak(CONTENT);
        initView();

    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(v -> startActivity(new Intent(HypertensionTipActivity.this, WifiConnectActivity.class)));
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        WarmNoticeFragment fragment = WarmNoticeFragment.getInstance(CONTENT, "下一步");
        fragment.setListener(this);
        transaction.replace(R.id.fl_container, fragment, "notice").commitAllowingStateLoss();
    }


    @Override
    public void onFragmentBtnClick() {
        Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentBtnTimeOut() {
        Toast.makeText(this, "时间到了", Toast.LENGTH_SHORT).show();
    }



}
