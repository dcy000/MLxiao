package com.example.module_register.ui.normal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.module_register.R;
import com.example.module_register.R2;
import com.example.module_register.bean.ContractInfo;
import com.example.module_register.dialog.XDialogFragment;
import com.example.module_register.service.RegisterAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConfirmContractActivity extends ToolbarBaseActivity {

    public static void start(Context context, String doctorId) {
        Intent intent = new Intent(context, ConfirmContractActivity.class);
        intent.putExtra("doctorId", doctorId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_phone)
    TextView tvPhone;
    @BindView(R2.id.tv_id_card)
    TextView tvIdCard;
    @BindView(R2.id.tv_contract_interval)
    TextView tvContractInterval;
    @BindView(R2.id.tv_contract_doctor)
    TextView tvContractDoctor;
    @BindView(R2.id.tv_doctor_phone)
    TextView tvDoctorPhone;
    @BindView(R2.id.tv_contract_organization)
    TextView tvContractOrganization;
    @BindView(R2.id.tv_service_type)
    TextView tvServiceType;
    @BindView(R2.id.tv_tab1_personal_info)
    TextView tvTab1PersonalInfo;
    @BindView(R2.id.tv_tab2_health_info)
    TextView tvTab2HealthInfo;
    @BindView(R2.id.tv_tab3_contract_doctor)
    TextView tvTab3ContractDoctor;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    private Unbinder mUnbinder;

    private String mDoctorId;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_confirm_contract;
    }

    @Override
    public void initParams(Intent intentArgument) {
        mDoctorId = getIntent().getStringExtra("doctorId");


        Box.getRetrofit(RegisterAPI.class)
                .getContractInfo(Box.getUserId(), mDoctorId)
                .compose(RxUtils.<ContractInfo>httpResponseTransformer())
                .as(RxUtils.<ContractInfo>autoDisposeConverter(this))
                .subscribe(new CommonObserver<ContractInfo>() {
                    @Override
                    public void onNext(ContractInfo response) {
                        tvName.setText("姓名： " + response.bname);
                        tvPhone.setText("联系方式: " + response.btel);
                        tvIdCard.setText("身份证： " + response.sfz);
                        //tvContractInterval.setText("签约周期： " +  response.sfz);
                        tvContractInterval.setText("签约周期： " + "两年");
                        tvContractDoctor.setText("签约医生： " + response.doctername);
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
                        tvDoctorPhone.setText("医生联系方式： " + dtel);
                        tvContractOrganization.setText("签约机构： " + response.hosname);
                        tvServiceType.setText("健康档案管理费： " + response.amount);
                    }
                });
    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
        tvTab1PersonalInfo.setTextColor(Color.parseColor("#3f86fc"));
        tvTab3ContractDoctor.setTextColor(Color.parseColor("#ffffff"));
        tvGoForward.setText("完成");
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String userId = ((UserInfoBean) Box.getSessionManager().getUser()).bid;
        Box.getRetrofit(RegisterAPI.class)
                .bindDoctor(userId, mDoctorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        XDialogFragment dialogFragment = new XDialogFragment();
                        dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                emitEvent("skip2MainActivity");
                                finish();
                            }
                        });
                        dialogFragment.show(getSupportFragmentManager(), XDialogFragment.tag());
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        ToastUtils.showShort(ex.getMessage());
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
