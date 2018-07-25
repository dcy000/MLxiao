package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.hypertensionmanagement.dialog.TwoChoiceDialog;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.DialogSureCancel;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlowDiseaseManagementActivity extends BaseActivity implements TwoChoiceDialog.OnDialogClickListener {

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
        mlSpeak(CONTENT);
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
                showDialog();
                break;
            case R.id.iv_blood_sugar_manage:
                T.show("敬请期待");
                break;
        }
    }

    private void hypertensionManage() {
        DialogSureCancel sureCancel = new DialogSureCancel(this);
        sureCancel.setContent(CONTENT);
        sureCancel.setSure("去测量");
        sureCancel.setCancel("下次再说");
        sureCancel.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {

                dialog.dismiss();
            }
        });
        sureCancel.show();

    }

    private void showDialog() {
        TwoChoiceDialog dialog = new TwoChoiceDialog(CONTENT, "去测量", "下次再说");
        dialog.setListener(this);
        dialog.show(getFragmentManager(), "tip");
    }


    @Override
    public void onClickConfirm(String content) {
// TODO: 2018/7/25 去测量 
    }
}
