package com.gcml.module_doctor_advisory.ui;

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

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_doctor_advisory.R;
import com.gcml.module_doctor_advisory.bean.ContractInfo;
import com.gcml.module_doctor_advisory.net.QianYueRepository;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

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
        new QianYueRepository()
                .getContractInfo(UserSpHelper.getUserId(), mDoctorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<ContractInfo>() {
                    @Override
                    public void onNext(ContractInfo contractInfo) {
                        tvName.setText("姓名： " + contractInfo.bname);
                        tvPhone.setText("联系方式: " + contractInfo.btel);
                        tvIdCard.setText("身份证： " + contractInfo.sfz);
                        //tvContractInterval.setText("绑定周期： " +  response.sfz);
                        tvContractInterval.setText("绑定周期： " + "两年");
                        tvContractDoctor.setText("绑定健康顾问： " + contractInfo.doctername);
                        String dtel = contractInfo.dtel;
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
                        tvContractOrganization.setText("绑定机构： " + contractInfo.hosname);
                        tvServiceType.setText("健康档案管理费： " + contractInfo.amount);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        new QianYueRepository()
                .bindDoctor(UserSpHelper.getUserId(), mDoctorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        XDialogFragment dialogFragment = new XDialogFragment();
                        dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Routerfit.register(AppRouter.class).skipPersonDetailActivity();
                                finish();
                            }
                        });
                        dialogFragment.show(getSupportFragmentManager(), XDialogFragment.tag());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
