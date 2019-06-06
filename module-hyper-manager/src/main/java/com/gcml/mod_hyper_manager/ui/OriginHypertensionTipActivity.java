package com.gcml.mod_hyper_manager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gcml.common.data.AppManager;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.mod_hyper_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * 原发性高血压
 */
public class OriginHypertensionTipActivity extends ToolbarBaseActivity implements WarmNoticeFragment.OnButtonClickListener {

    public static final String CONTENT = "为给您提供更好的方案,以下问题请认真回答。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hypertension_tip);
        initTitle();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "为给您提供更好的方案,以下问题请认真回答。");
        initView();
        AppManager.getAppManager().addActivity(this);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 调 查");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(OriginHypertensionTipActivity.this, WifiConnectActivity.class)));
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
        startActivity(new Intent(this, PrimaryHypertensionActivity.class));
        finish();
    }

    @Override
    public void onFragmentBtnTimeOut() {
        startActivity(new Intent(this, PrimaryHypertensionActivity.class));
        finish();
    }


}
