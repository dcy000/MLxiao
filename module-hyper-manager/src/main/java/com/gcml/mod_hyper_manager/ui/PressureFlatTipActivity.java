package com.gcml.mod_hyper_manager.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.mod_hyper_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

/**
 * 偏低 生成方案
 */
public class PressureFlatTipActivity extends ToolbarBaseActivity implements WarmNoticeFragment.OnButtonClickListener {
    public static final String CONTENT = "  您好,根据系统中显示的三次数据判定,您血压偏低,现系统正为您生成预防高血压方案";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_high_tip);
        initTitle();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"您在三日测量有血压异常偏低现象，为避免血压再度异常,小E为您进一步判断且给您方案。");
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
//        mRightView.setOnClickListener(v -> startActivity(new Intent(PressureFlatTipActivity.this, WifiConnectActivity.class)));
    }

    @Override
    public void onFragmentBtnClick() {
//        startActivity(new Intent(this, WeightMeasureActivity.class));
//        CC.obtainBuilder("health_measure")
//                .setActionName("To_WeightManagerActivity")
//                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//            @Override
//            public void onResult(CC cc, CCResult result) {
//                startActivity(new Intent(PressureFlatTipActivity.this, TreatmentPlanActivity.class));
//            }
//        });

        Routerfit.register(AppRouter.class).skipDetecteTipActivity("3");
    }

    @Override
    public void onFragmentBtnTimeOut() {
        Routerfit.register(AppRouter.class).skipWeightManagerActivity("PressureFlatTipActivity", "TreatmentPlanActivity");
        AppManager.getAppManager().finishAllActivity();
    }
}
