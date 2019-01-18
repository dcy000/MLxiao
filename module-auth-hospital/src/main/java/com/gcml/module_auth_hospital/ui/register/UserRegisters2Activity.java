package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.billy.cc.core.component.CC;
import com.gcml.common.server.ServerBean;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.login.IDCardNuberLoginActivity;
import com.gcml.module_auth_hospital.ui.login.ScanIdCardLoginActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/17.
 */

public class UserRegisters2Activity extends AppCompatActivity {
    private TranslucentToolBar tb;
    private LinearLayout registers;
    private UserRepository repository = new UserRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registers);

        tb = findViewById(R.id.tb_registers);
        tb.setData("居 民 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.auth_hospital_ic_setting, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.setting").build().call();
                    }
                });

        registers = findViewById(R.id.ll_registers);
        updatePage();
    }

    private void updatePage() {
        repository.getServer("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<ServerBean>() {
                    @Override
                    public void onNext(ServerBean serverBean) {
                        findViewById(R.id.ll_registers).setVisibility(View.VISIBLE);

                        tb.setStrLeft("   " + serverBean.serverName);
                        String userLogin = serverBean.userLogin;
                        if (userLogin.contains("1")) {
                            registers.getChildAt(0).setVisibility(View.VISIBLE);
                            registers.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(UserRegisters2Activity.this, ScanIdCardRegisterActivity.class));
                                }
                            });
                        }
                        if (userLogin.contains("2")) {
                            registers.getChildAt(1).setVisibility(View.VISIBLE);
                            registers.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(UserRegisters2Activity.this, IDCardNuberRegisterActivity.class));
                                }
                            });

                        }
                        if (userLogin.contains("3")) {
                            registers.getChildAt(2).setVisibility(View.VISIBLE);
                            registers.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ToastUtils.showShort("敬请期待");
                                }
                            });

                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }
}

