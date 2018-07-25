package com.example.han.referralproject.hypertensionmanagement.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.hypertensionmanagement.dialog.TwoChoiceDialog;
import com.gcml.lib_utils.ui.dialog.DialogSureCancel;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlowDiseaseManagementActivity extends AppCompatActivity implements TwoChoiceDialog.OnDialogClickListener {

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
    }

    @OnClick({R.id.iv_Hypertension_manage, R.id.iv_blood_sugar_manage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_Hypertension_manage:
//                hypertensionManage();
                showDialog();
                break;
            case R.id.iv_blood_sugar_manage:
                T.show("敬请期待");
                break;
        }
    }

    private void hypertensionManage() {
        DialogSureCancel sureCancel = new DialogSureCancel(this);
        sureCancel.setContent("您当前测量次数未满足非同日3次测量,高血压诊断条件不足,再测2日即可为您开启方案。");
        sureCancel.setSure("去测量");
        sureCancel.setCancel("下次再说");
        sureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.show("");
            }
        });
        sureCancel.setCancelListener(null);
        sureCancel.show();
        showDialog();
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
