package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.hypertensionmanagement.dialog.FllowUpTimesDialog;
import com.gcml.lib_utils.display.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlowDiseaseManagementActivity extends BaseActivity implements FllowUpTimesDialog.OnDialogClickListener {

    @BindView(R.id.iv_Hypertension_manage)
    ImageView ivHypertensionManage;
    @BindView(R.id.iv_blood_sugar_manage)
    ImageView ivBloodSugarManage;

    public static final String CONTENT = "您当前测量次数未满足非同日3次测量,高血压诊断条件不足,再测2日即可为您开启方案";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slow_disease_management);
        ButterKnife.bind(this);
        initTitle();
//        mlSpeak(CONTENT);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
    }

    @OnClick({R.id.iv_Hypertension_manage, R.id.iv_blood_sugar_manage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_Hypertension_manage:
                // TODO: 2018/7/27 非同日三次 是否满足  接口 2分支 
                startActivity(new Intent(this, SlowDiseaseManagementTipActivity.class));
                showNotSameDayInfoDialog();
                break;
            case R.id.iv_blood_sugar_manage:
                ToastUtils.showShort("敬请期待");
                break;
        }
    }

    private void showNotSameDayInfoDialog() {
        FllowUpTimesDialog dialog = new FllowUpTimesDialog();
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "sameTime");
    }


    @Override
    public void onClickConfirm() {
        // TODO: 2018/7/27 去测量
        ToastUtils.showShort("去测量");
    }
}
