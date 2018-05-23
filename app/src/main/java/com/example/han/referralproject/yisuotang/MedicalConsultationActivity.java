package com.example.han.referralproject.yisuotang;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

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
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("医药咨询");
    }

    @OnClick({R.id.community_drugstore, R.id.community_doctor, R.id.health_mannager, R.id.contract_doctor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.community_drugstore:
                break;
            case R.id.community_doctor:
                break;
            case R.id.health_mannager:
                break;
            case R.id.contract_doctor:
                break;
        }
    }
}
