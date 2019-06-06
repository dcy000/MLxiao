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
import com.sjtu.yifei.annotation.Route;

/**
 * 高血压  下一步 心血管风险 评估
 */
@Route(path = "/app/hypertension/tip/activity")
public class HypertensionTipActivity extends ToolbarBaseActivity implements WarmNoticeFragment.OnButtonClickListener {


    public static final String CONTENT = "小E在您测量的日期中发现您在三天有血压异常偏高现象，可能患有高血压，为更好提供方案，以下问题需您认真作答";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hypertension_tip);
        initTitle();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"您在三日测量有血压异常偏高现象，可能患有高血压，小E为您进一步判断且给您方案。");
        initView();
        AppManager.getAppManager().addActivity(this);

    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 调 查");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(HypertensionTipActivity.this, WifiConnectActivity.class)));
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
        startActivity(new Intent(HypertensionTipActivity.this, HasDiseaseOrNotActivity.class));
        finish();
    }

    @Override
    public void onFragmentBtnTimeOut() {
        startActivity(new Intent(HypertensionTipActivity.this, HasDiseaseOrNotActivity.class));
        finish();
    }

}
