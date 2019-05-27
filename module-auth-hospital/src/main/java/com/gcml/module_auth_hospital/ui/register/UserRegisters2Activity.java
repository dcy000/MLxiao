package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.kaer.sdk.IDCardItem;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

/**
 * Created by lenovo on 2019/1/17.
 */

public class UserRegisters2Activity extends ToolbarBaseActivity {
    private TranslucentToolBar tb;
    private LinearLayout registers;
    private UserRepository repository = new UserRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_registers);

        tb = findViewById(R.id.tb_registers);
        tb.setData("注 册 账 号",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });

        registers = findViewById(R.id.ll_registers);
        setWifiLevel(tb);
        updatePage();
        AppManager.getAppManager().addActivity(this);
    }

    private void updatePage() {
        registers.getChildAt(0).setVisibility(View.VISIBLE);
        registers.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routerfit.register(AppRouter.class).skipConnectActivity(36, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (data instanceof IDCardItem) {
                            startActivity(new Intent(UserRegisters2Activity.this, SetPassWordActivity.class));
                        }
                    }
                });
            }
        });

        registers.getChildAt(1).setVisibility(View.VISIBLE);
        registers.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegisters2Activity.this, IDCardNuberRegisterActivity.class));
            }
        });

        /*repository.getServer("1")
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
//                                    ToastUtils.showShort("敬请期待");
//                                    CC.obtainBuilder("com.gcml.auth.face2.signup")
//                                            .build()
//                                            .callAsyncCallbackOnMainThread(new IComponentCallback() {
//                                                @Override
//                                                public void onResult(CC cc, CCResult result) {
//                                                    if (result.isSuccess()) {
////                                            login(deviceId);
//                                                        startActivity(new Intent(UserRegisters2Activity.this, RegisterSuccessActivity.class)
//                                                                .putExtra("idcard", "340321199112256551"));
//                                                    } else {
//                                                        ToastUtils.showShort("人脸注册失败");
//                                                    }
//                                                }
//                                            });
                                }
                            });

                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });*/
    }
}

