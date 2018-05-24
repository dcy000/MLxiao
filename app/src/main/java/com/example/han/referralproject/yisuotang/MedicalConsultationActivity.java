package com.example.han.referralproject.yisuotang;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedicalConsultationActivity extends BaseActivity {


    @BindView(R.id.community_drugstore)
    ImageView communityDrugstore;
    @BindView(R.id.community_doctor)
    ImageView communityDoctor;
    @BindView(R.id.health_mannager)
    ImageView healthMannager;
    @BindView(R.id.contract_doctor)
    ImageView contractDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_consultation);
        ButterKnife.bind(this);
        initTitle();
        speak("主人,欢迎来到医药咨询");
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("医药咨询");
    }

    @OnClick({R.id.community_drugstore, R.id.community_doctor, R.id.health_mannager, R.id.contract_doctor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.community_drugstore:
                startActivity(new Intent(this, SheQuYiShengActivity.class));
                break;
            case R.id.community_doctor:
                startActivity(new Intent(this, OnlineDoctorListActivity.class));
                break;
            case R.id.health_mannager:
                startActivity(new Intent(this, SheQuYiShengActivity.class).putExtra("status", "4"));
                break;
            case R.id.contract_doctor:
                gotoQianyueYiSheng();
                break;
        }
    }

    private void gotoQianyueYiSheng() {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if ("1".equals(response.getState())) {
                    //已签约
                    startActivity(new Intent(MedicalConsultationActivity.this,
                            DoctorappoActivity.class));
                } else if ("0".equals(response.getState())
                        && (TextUtils.isEmpty(response.getDoctername()))) {
                    //未签约
                    Intent intent = new Intent(MedicalConsultationActivity.this,
                            OnlineDoctorListActivity.class);
                    intent.putExtra("flag", "contract");
                    startActivity(intent);
                } else {
                    // 待审核
                    Intent intent = new Intent(MedicalConsultationActivity.this,
                            CheckContractActivity.class);
                    startActivity(intent);
                }
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }

}
