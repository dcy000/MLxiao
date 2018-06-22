package com.example.han.referralproject.yisuotang;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.util.LocalShared;
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
        getSignedDoctorInfo();
    }

    private String doctorId;
    private String doctorTel;

    private void getSignedDoctorInfo() {
        NetworkApi.DoctorInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<Doctor>() {
            @Override
            public void onSuccess(Doctor response) {
                if (response != null) {
                    doctorId = response.docterid + "";
                    doctorTel = response.tel;
                    LocalShared.getInstance(MedicalConsultationActivity.this).setDoctorId(doctorId);
                    LocalShared.getInstance(MedicalConsultationActivity.this).setDoctorTel( doctorTel);
                }
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
            }
        });

    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("医药咨询");
    }

    @OnClick({R.id.community_drugstore, R.id.community_doctor, R.id.health_mannager, R.id.contract_doctor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //社区药店
            case R.id.community_drugstore:
                startActivity(new Intent(this, SheQuYiShengActivity.class));
                break;
            //心理咨询师
            case R.id.community_doctor:
                startActivity(new Intent(this, OnlineDoctorListActivity.class));
                break;
            //健康管理师
            case R.id.health_mannager:
                startActivity(new Intent(this, SheQuYiShengActivity.class).putExtra("status", "4"));
                break;
            //签约医生
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
                            DoctorappoActivity.class).
                            putExtra("doctorId", doctorId).
                            putExtra("doctorTel", doctorTel));
                } else if ("0".equals(response.getState())
                        && (TextUtils.isEmpty(response.getDoctername()))) {
                    //未签约
                    Intent intent = new Intent(MedicalConsultationActivity.this,
                            OnlineDoctorListActivity.class);
                    intent.putExtra("flag", "contract");//在线医生和签约医生(没有发起过签约)跳转区分
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
