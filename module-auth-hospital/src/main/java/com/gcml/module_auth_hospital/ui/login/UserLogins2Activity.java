package com.gcml.module_auth_hospital.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.IConstant;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.server.ServerBean;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.register.RegisterSuccessActivity;
import com.gcml.module_auth_hospital.ui.register.UserRegisters2Activity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/17.
 */

public class UserLogins2Activity extends BaseActivity {

    private TranslucentToolBar tb;
    private LinearLayout lllogins;
    private TextView tvRegister;

    UserRepository repository = new UserRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_2_logins);

        lllogins = findViewById(R.id.ll_logins);
        tvRegister = findViewById(R.id.tv_to_register);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLogins2Activity.this, UserRegisters2Activity.class));
            }
        });

        tb = findViewById(R.id.tb_logins);
        tb.setData("用 户 登 录", 0, "  杭州", R.drawable.auth_hospital_ic_setting, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                onRightClickWithPermission(new BaseActivity.IAction() {
                    @Override
                    public void action() {
                        CC.obtainBuilder("com.gcml.old.setting").build().call();
                    }
                });
            }
        });
//        tb.layLeft.setOnClickListener(null);
        tb.layLeft.setClickable(false);
//        updatePage();
        ActivityHelper.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                                    CC.obtainBuilder("com.gcml.auth.face2.signin")
                                            .addParam("currentUser", false)
                                            .build()
                                            .callAsyncCallbackOnMainThread(new IComponentCallback() {
                                                @Override
                                                public void onResult(CC cc, CCResult result) {
                                                    boolean skip = "skip".equals(result.getErrorMessage());
                                                    if (result.isSuccess() || skip) {
//                                                        startActivity(new Intent(UserLogins2Activity.this, RegisterSuccessActivity.class));
                                                        CC.obtainBuilder(IConstant.KEY_INUIRY_ENTRY).build().callAsync();
                                                    } else {
                                                        ToastUtils.showShort(result.getErrorMessage());
                                                    }
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
