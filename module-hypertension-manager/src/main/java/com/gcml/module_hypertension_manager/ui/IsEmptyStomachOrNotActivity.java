package com.gcml.module_hypertension_manager.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_hypertension_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.Arrays;

@Route(path = "/app/hypertension/is/empty/stomach/or/not")
public class IsEmptyStomachOrNotActivity extends ToolbarBaseActivity implements MultipleChoiceStringFragment.OnButtonClickListener {

    public static final String CONTENT = "您当前是否空腹?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_empty_stomach_or_not);
        initTitle();
        initView();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"您当前是否空腹");
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MultipleChoiceStringFragment fragment = MultipleChoiceStringFragment
                .getInstance(CONTENT, "",
                        Arrays.asList("是", "否")
                        , true);
        fragment.setListener(this);
        transaction.replace(R.id.fl_container, fragment).commitAllowingStateLoss();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 调 查");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(IsEmptyStomachOrNotActivity.this, WifiConnectActivity.class)));
    }

    String[] itmes = {"是", "否"};

    @Override
    public void onNextStep(int[] checked) {
        if ("是".equals(itmes[checked[0]])) {
//            startActivity(new Intent(this, BloodClucoseMeasureActivity.class));
//            CC.obtainBuilder("health_measure")
//                    .setActionName("To_BloodsugarManagerActivity")
//                    .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//                @Override
//                public void onResult(CC cc, CCResult result) {
//                    CC.obtainBuilder("health_measure")
//                            .setActionName("To_WeightManagerActivity")
//                            .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//                        @Override
//                        public void onResult(CC cc, CCResult result) {
//                            startActivity(new Intent(IsEmptyStomachOrNotActivity.this, TreatmentPlanActivity.class));
//                        }
//                    });
//                }
//            });

            Routerfit.register(AppRouter.class).skipDetecteTipActivity("2");

        } else {
            Routerfit.register(AppRouter.class).skipAlarmDetail2Activity(-1);
            AppManager.getAppManager().finishAllActivity();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
