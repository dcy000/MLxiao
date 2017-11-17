package com.medlink.danbogh.register;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ConfirmContractActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_id_card)
    TextView tvIdCard;
    @BindView(R.id.tv_contract_interval)
    TextView tvContractInterval;
    @BindView(R.id.tv_contract_doctor)
    TextView tvContractDoctor;
    @BindView(R.id.tv_doctor_phone)
    TextView tvDoctorPhone;
    @BindView(R.id.tv_contract_organization)
    TextView tvContractOrganization;
    @BindView(R.id.tv_service_type)
    TextView tvServiceType;
    @BindView(R.id.tv_tab1_personal_info)
    TextView tvTab1PersonalInfo;
    @BindView(R.id.tv_tab2_health_info)
    TextView tvTab2HealthInfo;
    @BindView(R.id.tv_tab3_contract_doctor)
    TextView tvTab3ContractDoctor;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    public Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_contract);
        mUnbinder = ButterKnife.bind(this);
        tvTab1PersonalInfo.setTextColor(Color.parseColor("#3f86fc"));
        tvTab3ContractDoctor.setTextColor(Color.parseColor("#ffffff"));
        tvGoForward.setText("完成");
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {

    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
