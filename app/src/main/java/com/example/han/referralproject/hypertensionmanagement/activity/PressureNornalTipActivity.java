package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.hypertensionmanagement.fragment.WarmNoticeFragment;
import com.gcml.common.data.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 正常 生成方案
 */
public class PressureNornalTipActivity extends BaseActivity implements WarmNoticeFragment.OnButtonClickListener {
    public static final String CONTENT = "您好,根据系统中显示的三次数据判定,您血压正常,为预防高血压,为您生成预防方案";
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_high_tip);
        ButterKnife.bind(this);
        initTitle();
        mlSpeak("您好，您在三日测量有血压异常偏低现象，为避免血压再度异常，小E为您进一步判断且给您方案。");
        initView();
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        WarmNoticeFragment fragment = WarmNoticeFragment.getInstance(CONTENT, "生成健康方案");
        fragment.setListener(this);
        transaction.replace(R.id.fl_container, fragment).commitAllowingStateLoss();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 调 查");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(PressureNornalTipActivity.this, WifiConnectActivity.class)));
    }

    @Override
    public void onFragmentBtnClick() {
//        CC.obtainBuilder("health_measure")
//                .setActionName("To_WeightManagerActivity")
//                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//            @Override
//            public void onResult(CC cc, CCResult result) {
//                startActivity(new Intent(PressureNornalTipActivity.this, TreatmentPlanActivity.class));
//                finish();
//            }
//        });

        startActivity(new Intent(this, DetecteTipActivity.class)
                .putExtra("fromWhere", "3"));


    }

    @Override
    public void onFragmentBtnTimeOut() {
//        CC.obtainBuilder("health_measure")
//                .setActionName("To_WeightManagerActivity")
//                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//            @Override
//            public void onResult(CC cc, CCResult result) {
//                startActivity(new Intent(PressureNornalTipActivity.this, TreatmentPlanActivity.class));
//                finish();
//            }
//        });
        startActivity(new Intent(PressureNornalTipActivity.this, DetecteTipActivity.class)
                .putExtra("fromWhere","3"));

    }
}
