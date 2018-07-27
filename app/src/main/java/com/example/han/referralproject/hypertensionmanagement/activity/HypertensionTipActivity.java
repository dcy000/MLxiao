package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.hypertensionmanagement.fragment.WarmNoticeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 高血压  下一步 心血管风险 评估
 */
public class HypertensionTipActivity extends BaseActivity implements WarmNoticeFragment.OnButtonClickListener {


    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    public static final String CONTENT = " 您好,根据系统中显示的三次数据判定,您疑似患有高血压,以下问题需要您认真作答";

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
        startActivity(new Intent(this, HasDiseaseOrNotActivity.class));
    }

    @Override
    public void onFragmentBtnTimeOut() {
        startActivity(new Intent(this, HasDiseaseOrNotActivity.class));
    }

}
