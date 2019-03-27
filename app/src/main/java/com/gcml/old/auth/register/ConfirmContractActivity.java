package com.gcml.old.auth.register;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.ContractInfo;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.old.auth.personal.PersonDetailActivity;
import com.medlink.danbogh.XDialogFragment;

public class ConfirmContractActivity extends AppCompatActivity {

    public static void start(Context context, String doctorId) {
        Intent intent = new Intent(context, ConfirmContractActivity.class);
        intent.putExtra("doctorId", doctorId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    TextView tvName;

    TextView tvPhone;

    TextView tvIdCard;

    TextView tvContractInterval;

    TextView tvContractDoctor;

    TextView tvDoctorPhone;

    TextView tvContractOrganization;

    TextView tvServiceType;

    TextView tvTab1PersonalInfo;

    TextView tvTab2HealthInfo;

    TextView tvTab3ContractDoctor;

    TextView tvGoBack;

    TextView tvGoForward;

    private String mDoctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_confirm_contract);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvIdCard = (TextView) findViewById(R.id.tv_id_card);
        tvContractInterval = (TextView) findViewById(R.id.tv_contract_interval);
        tvContractDoctor = (TextView) findViewById(R.id.tv_contract_doctor);
        tvDoctorPhone = (TextView) findViewById(R.id.tv_doctor_phone);
        tvContractOrganization = (TextView) findViewById(R.id.tv_contract_organization);
        tvServiceType = (TextView) findViewById(R.id.tv_service_type);
        tvTab1PersonalInfo = (TextView) findViewById(R.id.tv_tab1_personal_info);
        tvTab2HealthInfo = (TextView) findViewById(R.id.tv_tab2_health_info);
        tvTab3ContractDoctor = (TextView) findViewById(R.id.tv_tab3_contract_doctor);
        tvGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        tvTab1PersonalInfo.setTextColor(Color.parseColor("#3f86fc"));
        tvTab3ContractDoctor.setTextColor(Color.parseColor("#ffffff"));
        tvGoForward.setText("完成");
        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoBackClicked();
            }
        });
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });

        mDoctorId = getIntent().getStringExtra("doctorId");
        NetworkApi.getContractInfo(mDoctorId, new NetworkManager.SuccessCallback<ContractInfo>() {
            @Override
            public void onSuccess(ContractInfo response) {
                tvName.setText("姓名： " + response.bname);
                tvPhone.setText("联系方式: " + response.btel);
                tvIdCard.setText("身份证： " + response.sfz);
                //tvContractInterval.setText("绑定周期： " +  response.sfz);
                tvContractInterval.setText("绑定周期： " + "两年");
                tvContractDoctor.setText("绑定健康顾问： " + response.doctername);
                String dtel = response.dtel;
                if (!TextUtils.isEmpty(dtel)) {
                    char[] chars = dtel.toCharArray();
                    if (chars.length == 11) {
                        for (int i = 3; i < 8; i++) {
                            chars[i] = '*';
                        }
                    }
                    dtel = new String(chars);
                }
                tvDoctorPhone.setText("健康顾问联系方式： " + dtel);
                tvContractOrganization.setText("绑定机构： " + response.hosname);
                tvServiceType.setText("健康档案管理费： " + response.amount);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort(message);
            }
        });
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        NetworkApi.bindDoctor(UserSpHelper.getUserId(), Integer.valueOf(mDoctorId), new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                XDialogFragment dialogFragment = new XDialogFragment();
                dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PersonDetailActivity.class);
                        startActivity(intent);
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), XDialogFragment.tag());
            }
        });
    }
}
