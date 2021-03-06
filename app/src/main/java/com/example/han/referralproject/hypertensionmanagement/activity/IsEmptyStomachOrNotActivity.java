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
import com.example.han.referralproject.hypertensionmanagement.fragment.MultipleChoiceStringFragment;
import com.gcml.common.data.AppManager;
import com.medlink.danbogh.alarm.AlarmDetail2Activity;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IsEmptyStomachOrNotActivity extends BaseActivity implements MultipleChoiceStringFragment.OnButtonClickListener {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    public static final String CONTENT = "您当前是否空腹?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_empty_stomach_or_not);
        ButterKnife.bind(this);
        initTitle();
        initView();
        mlSpeak("主人，您当前是否空腹");
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

            startActivity(new Intent(this, DetecteTipActivity.class)
                    .putExtra("fromWhere", "2"));

        } else {
            AlarmDetail2Activity.newLaunchIntent(this, -1);
            AppManager.getAppManager().finishAllActivity();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
