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
import com.gcml.common.data.AppManager;

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
        mlSpeak("您在三日测量有血压异常偏高现象，为避免血压再度偏高,小E为您进一步判断且给您方案。");
        initView();
        AppManager.getAppManager().addActivity(this);
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
        mTitleText.setText("健 康 调 查");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(NormalHighTipActivity.this, WifiConnectActivity.class)));
    }

    @Override
    public void onFragmentBtnClick() {
        startActivity(new Intent(this, NormalHightActivity.class));
        finish();
    }

    @Override
    public void onFragmentBtnTimeOut() {
        startActivity(new Intent(this, NormalHightActivity.class));
        finish();
    }
}
