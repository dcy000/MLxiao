package com.gcml.module_auth_hospital.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.face.VertifyFaceProviderImp;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.ServerBean;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.register.UserRegisters2Activity;
import com.kaer.sdk.IDCardItem;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/17.
 */
@Route(path = "/auth/hospital/user/logins2/activity")
public class UserLogins2Activity extends ToolbarBaseActivity {

    private TranslucentToolBar tb;
    private LinearLayout lllogins;
    private TextView tvRegister;

    UserRepository repository = new UserRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_doctor_2_logins);

        lllogins = findViewById(R.id.ll_logins);
        tvRegister = findViewById(R.id.tv_to_register);

        tvRegister.setOnClickListener(v -> startActivity(new Intent(UserLogins2Activity.this, UserRegisters2Activity.class)));

        tb = findViewById(R.id.tb_logins);
        tb.setData("登 陆 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });
        setWifiLevel(tb);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updatePage();
        updatePage2();
    }

    private void updatePage2() {
        lllogins.getChildAt(0).setVisibility(View.VISIBLE);
        lllogins.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routerfit.register(AppRouter.class).skipConnectActivity(36, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (data instanceof IDCardItem) {
// TODO: 2019/5/22  身份证信息确认界面
                        }
                    }
                });
            }
        });

        lllogins.getChildAt(1).setVisibility(View.VISIBLE);
        lllogins.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLogins2Activity.this, IDCardNuberLoginActivity.class));
            }
        });

        lllogins.getChildAt(2).setVisibility(View.VISIBLE);
        lllogins.getChildAt(2).setOnClickListener(v ->
                Routerfit.register(AppRouter.class)
                        .getVertifyFaceProvider()
                        .onlyVertifyFace(false, false, true, new VertifyFaceProviderImp.VertifyFaceResult() {
                            @Override
                            public void success() {
                                Routerfit.register(AppRouter.class).skipMainActivity();
                            }

                            @Override
                            public void failed(String msg) {
                                ToastUtils.showShort("人脸登录失败");
                            }
                        }));


    }

    private void updatePage() {
        repository.getServer("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<ServerBean>() {
                    @Override
                    public void onNext(ServerBean serverBean) {
                        findViewById(R.id.ll_to_register).setVisibility(View.VISIBLE);

                        tb.setStrLeft("   " + serverBean.serverName);
                        String userLogin = serverBean.userLogin;
                        if (userLogin.contains("1")) {
                            lllogins.getChildAt(0).setVisibility(View.VISIBLE);
                            lllogins.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(UserLogins2Activity.this, ScanIdCardLoginActivity.class));
                                }
                            });
                        }
                        if (userLogin.contains("2")) {
                            lllogins.getChildAt(1).setVisibility(View.VISIBLE);
                            lllogins.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(UserLogins2Activity.this, IDCardNuberLoginActivity.class));
                                }
                            });

                        }
                        if (userLogin.contains("3")) {
                            lllogins.getChildAt(2).setVisibility(View.VISIBLE);
                            lllogins.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Routerfit.register(AppRouter.class)
                                            .getVertifyFaceProvider()
                                            .onlyVertifyFace(false, false, true, new VertifyFaceProviderImp.VertifyFaceResult() {
                                                @Override
                                                public void success() {
                                                    Routerfit.register(AppRouter.class).skipMainActivity();
                                                }

                                                @Override
                                                public void failed(String msg) {
                                                    ToastUtils.showShort(msg);
                                                }
                                            });
                                }
                            });

                        }
                        if (userLogin.contains("4")) {
                            lllogins.getChildAt(3).setVisibility(View.VISIBLE);
                            lllogins.getChildAt(3).setOnClickListener(new View.OnClickListener() {
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
