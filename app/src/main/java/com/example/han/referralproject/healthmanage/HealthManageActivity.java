package com.example.han.referralproject.healthmanage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.hypertensionmanagement.activity.SlowDiseaseManagementActivity;
import com.example.han.referralproject.tcm.SymptomCheckActivity;
import com.gcml.common.FilterClickListener;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/2/27.
 */

public class HealthManageActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tb_health_manage)
    TranslucentToolBar tbHealthManage;
    @BindView(R.id.ll_health_task)
    LinearLayout llHealthTask;
    @BindView(R.id.ll_health_blood_manager)
    LinearLayout llBloodManager;
    @BindView(R.id.ll_risk)
    LinearLayout llRisk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_mannage);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        llHealthTask.setOnClickListener(new FilterClickListener(this));
        llBloodManager.setOnClickListener(new FilterClickListener(this));
        llRisk.setOnClickListener(new FilterClickListener(this));
    }

    private void initView() {
        tbHealthManage.setData(UM.getString(R.string.title_health_manager),
                com.gcml.module_auth_hospital.R.drawable.common_btn_back, UM.getString(R.string.toolbar_back),
                com.gcml.module_auth_hospital.R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        onRightClickWithPermission(new IAction() {
                            @Override
                            public void action() {
                                CC.obtainBuilder("com.gcml.old.setting").build().call();
                            }
                        });

                    }
                });
        setWifiLevel(tbHealthManage);
    }

    @Override
    public void onClick(View v) {
        if (v == llHealthTask) {
            CCResult result;
            Observable<UserEntity> rxUser;
            result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
            rxUser = result.getDataItem("data");
            rxUser.subscribeOn(Schedulers.io())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity user) {
                            if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                                ToastUtils.showShort(R.string.improve_weight_and_height_information);
                                MLVoiceSynthetize.startSynthesize(
                                        HealthManageActivity.this.getApplicationContext(),
                                        UM.getString(R.string.improve_weight_and_height_information));
                            } else {
                                CC.obtainBuilder("com.gcml.task.isTask")
                                        .build()
                                        .callAsync(new IComponentCallback() {
                                            @Override
                                            public void onResult(CC cc, CCResult result) {
                                                if (result.isSuccess()) {
                                                    CC.obtainBuilder("app.component.task").addParam("startType", "MLMain").build().callAsync();
                                                } else {
                                                    CC.obtainBuilder("app.component.task.comply").build().callAsync();
                                                }
                                            }
                                        });
                            }
                        }
                    });
        } else if (v == llRisk) {
            CCResult result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
            Observable<UserEntity> rxUser = result.getDataItem("data");
            rxUser.subscribeOn(Schedulers.io())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity user) {
                            if (TextUtils.isEmpty(user.sex) || TextUtils.isEmpty(user.birthday) ||
                                    TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                                ToastUtils.showShort(R.string.age_gender_height_weight_information_is_perfect);
                                MLVoiceSynthetize.startSynthesize(
                                        getApplicationContext(),
                                        UM.getString(R.string.age_gender_height_weight_information_is_perfect));
                            } else {
                                toRisk();
                            }
                        }
                    });
        }else if (v==llBloodManager){
            Intent intent = new Intent(HealthManageActivity.this, SlowDiseaseManagementActivity.class);
            startActivity(intent);
        }

    }

    private void toRisk() {
        CC.obtainBuilder("health_measure")
                .setActionName("To_HealthInquiryActivity")
                .build()
                .call();
    }
}