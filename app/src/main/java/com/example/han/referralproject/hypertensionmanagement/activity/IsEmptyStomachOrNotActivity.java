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
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.hypertensionmanagement.fragment.MultipleChoiceStringFragment;
import com.example.han.referralproject.hypertensionmanagement.util.AppManager;

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
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(v -> startActivity(new Intent(IsEmptyStomachOrNotActivity.this, WifiConnectActivity.class)));
    }

    String[] itmes = {"是", "否"};

    @Override
    public void onNextStep(int[] checked) {
        if ("是".equals(itmes[checked[0]])) {
            startActivity(new Intent(this, BloodClucoseMeasureActivity.class));
        } else {

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
