package com.medlink.danbogh.register;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ContractInfo;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.medlink.danbogh.call.XDialogFragment;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ConfirmContractActivity extends AppCompatActivity {

    public static void start(Context context, String doctorId) {
        Intent intent = new Intent(context, ConfirmContractActivity.class);
        intent.putExtra("doctorId", doctorId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

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
    private Unbinder mUnbinder;

    private String mDoctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_contract);
        mUnbinder = ButterKnife.bind(this);
        tvTab1PersonalInfo.setTextColor(Color.parseColor("#3f86fc"));
        tvTab3ContractDoctor.setTextColor(Color.parseColor("#ffffff"));
        tvGoForward.setText("完成");

        mDoctorId = getIntent().getStringExtra("doctorId");
        NetworkApi.getContractInfo(mDoctorId, new NetworkManager.SuccessCallback<ContractInfo>() {
            @Override
            public void onSuccess(ContractInfo response) {
                tvName.setText("姓名： " +  response.bname);
                tvPhone.setText("联系方式: " + response.btel);
                tvIdCard.setText("身份证： " +  response.sfz);
                tvIdCard.setText("签约周期： " +  response.sfz);
                tvIdCard.setText("签约医生： " +  response.doctername);
                tvIdCard.setText("医生联系方式： " +  response.dtel);
                tvIdCard.setText("签约机构： " +  response.hosname);
                tvIdCard.setText("服务费用： " +  response.amount);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        NetworkApi.bindDoctor(MyApplication.getInstance().userId, Integer.valueOf(mDoctorId), new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                XDialogFragment dialogFragment = new XDialogFragment();
                dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), XDialogFragment.tag());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
