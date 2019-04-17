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
import com.gcml.common.utils.UM;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 原发性高血压
 */
public class OriginHypertensionTipActivity extends BaseActivity implements WarmNoticeFragment.OnButtonClickListener {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    public static final String CONTENT = "为给您提供更好的方案,以下问题请认真回答。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hypertension_tip);
        ButterKnife.bind(this);
        initTitle();
        mlSpeak(UM.getString(R.string.answer_the_question_carefully));
        initView();
        AppManager.getAppManager().addActivity(this);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(R.string.title_health_survey);
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(OriginHypertensionTipActivity.this, WifiConnectActivity.class)));
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        WarmNoticeFragment fragment = WarmNoticeFragment.getInstance(UM.getString(R.string.answer_the_question_carefully), UM.getString(R.string.next_step));
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
