package com.example.han.healthmanage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.ITaskProvider;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.FilterClickListener;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/2/27.
 */

public class HealthManageActivity extends ToolbarBaseActivity implements View.OnClickListener {
    TranslucentToolBar tbHealthManage;
    LinearLayout llHealthTask;
    LinearLayout llBloodManager;
    LinearLayout llRisk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_health_mannage);
        initView();
        initEvent();
    }

    private void initEvent() {
        llHealthTask.setOnClickListener(new FilterClickListener(this));
        llBloodManager.setOnClickListener(new FilterClickListener(this));
        llRisk.setOnClickListener(new FilterClickListener(this));
    }

    private void initView() {
        tbHealthManage = findViewById(R.id.tb_health_manage);
        llHealthTask = findViewById(R.id.ll_health_task);
        llBloodManager = findViewById(R.id.ll_health_blood_manager);
        llRisk = findViewById(R.id.ll_risk);

        tbHealthManage.setData("健 康 管 理",
                com.gcml.module_auth_hospital.R.drawable.common_btn_back, "返回",
                com.gcml.module_auth_hospital.R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                       /* onRightClickWithPermission(new IAction() {
                            @Override
                            public void action() {
                                CC.obtainBuilder("com.gcml.old.setting").build().call();
                            }
                        });*/
                        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);

                    }
                });
        setWifiLevel(tbHealthManage);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == llHealthTask) {

            Routerfit.register(AppRouter.class).getUserProvider().getUserEntity()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity user) {
                            if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                                ToastUtils.showShort("请先去个人中心完善体重和身高信息");
                                MLVoiceSynthetize.startSynthesize(
                                        HealthManageActivity.this.getApplicationContext(),
                                        "请先去个人中心完善体重和身高信息");
                            } else {
                                ITaskProvider taskProvider = Routerfit.register(AppRouter.class).getTaskProvider();
                                if (taskProvider != null) {
                                    taskProvider
                                            .isTaskHealth()
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new io.reactivex.observers.DefaultObserver<Object>() {
                                                @Override
                                                public void onNext(Object o) {
                                                    Routerfit.register(AppRouter.class).skipTaskActivity("MLMain");
                                                }

                                                @Override
                                                public void onError(Throwable throwable) {
                                                    if (throwable instanceof NullPointerException) {
                                                        Routerfit.register(AppRouter.class).skipTaskActivity("MLMain");
                                                    } else {
                                                        Routerfit.register(AppRouter.class).skipTaskComplyActivity();
                                                    }
                                                }

                                                @Override
                                                public void onComplete() {

                                                }
                                            });
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (v == llRisk) {
            Routerfit.register(AppRouter.class).getUserProvider().getUserEntity()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity user) {
                            if (TextUtils.isEmpty(user.sex) || TextUtils.isEmpty(user.birthday) ||
                                    TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                                ToastUtils.showShort("请确保年龄性别身高体重信息已完善");
                                MLVoiceSynthetize.startSynthesize(
                                        getApplicationContext(),
                                        "请确保年龄性别身高体重信息已完善");
                            } else {
                                toRisk();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } else if (v == llBloodManager) {
            Routerfit.register(AppRouter.class).skipSlowDiseaseManagementActivity();
        }

    }

    private void toRisk() {
      /*  CC.obtainBuilder("health_measure")
                .setActionName("To_HealthInquiryActivity")
                .build()
                .call();*/
//        Routerfit.register(AppRouter.class).skipFirstDiagnosisActivity();
        Routerfit.register(AppRouter.class).skipHealthInquiryActivity();
    }
}