package com.gcml.module_auth_hospital.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.menu.EMenu;
import com.gcml.common.menu.MenuEntity;
import com.gcml.common.menu.MenuHelperProviderImp;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.ChannelUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.FilterClickListener;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.ui.register.UserRegisters2Activity;
import com.kaer.sdk.IDCardItem;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

/**
 * Created by lenovo on 2019/1/17.
 */
@Route(path = "/auth/hospital/user/logins2/activity")
public class UserLogins2Activity extends ToolbarBaseActivity {

    private TranslucentToolBar tb;
    private LinearLayout lllogins;
    private TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_doctor_2_logins);

        lllogins = findViewById(R.id.ll_logins);
        tvRegister = findViewById(R.id.tv_to_register);

        tvRegister.setOnClickListener(
                v -> startActivity(new Intent(UserLogins2Activity.this, UserRegisters2Activity.class)));
        tb = findViewById(R.id.tb_logins);
        tb.setData("登 录 注 册",
//                R.drawable.common_btn_back, "返回",
                0, null,
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
//                        finish();
                    }

                    @Override
                    public void onRightClick() {
//                        Routerfit.register(AppRouter.class).skipSettingActivity();
                        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
                    }
                });
        setWifiLevel(tb);
        AppManager.getAppManager().addActivity(this);
        getMenu();
    }

    private void getMenu() {
        Routerfit.register(AppRouter.class).getMenuHelperProvider()
                .menu(ChannelUtils.isXiongAn() || ChannelUtils.isBj(), EMenu.LOGIN, new MenuHelperProviderImp.MenuResult() {
                    @Override
                    public void onSuccess(List<MenuEntity> menus) {
                        dealMenu(menus);
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                    }
                });
    }

    private void dealMenu(List<MenuEntity> menus) {
        for (MenuEntity entity : menus) {
            String name = entity.getMenuLabel();
            if (TextUtils.isEmpty(name)) continue;
            switch (name) {
                case "身份证扫描":
                    lllogins.getChildAt(0).setVisibility(View.VISIBLE);
                    break;
                case "身份证号输入":
                    lllogins.getChildAt(1).setVisibility(View.VISIBLE);
                    break;
                case "人脸识别":
                    lllogins.getChildAt(2).setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updatePage();
        updatePage2();
    }

    private void updatePage2() {
        lllogins.getChildAt(0).setOnClickListener(
                v ->
                        Routerfit.register(AppRouter.class).skipConnectActivity(36,true, (result, data) -> {
                            if (data instanceof IDCardItem) {
                                IDCardItem cardItem = ((IDCardItem) data);
                                if (cardItem != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", cardItem.partyName);
                                    bundle.putString("gender", cardItem.gender);
                                    bundle.putString("nation", cardItem.nation);
                                    bundle.putString("address", cardItem.certAddress);
                                    bundle.putParcelable("profile", cardItem.picBitmap);
                                    bundle.putString("idCard", cardItem.certNumber);
                                    goIdCardInfo(bundle);
                                }
                            }
                        }));


        lllogins.getChildAt(1).setOnClickListener(v ->
                Routerfit.register(AppRouter.class).skipIDCardNuberLoginActivity(new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (result == Activity.RESULT_OK) {
                            dealVertifySuccess();
                        } else {
                            Routerfit.setResult(Activity.RESULT_CANCELED, false);
                        }
                    }
                }))
        ;

        lllogins.getChildAt(2).setOnClickListener(new FilterClickListener(v ->
                Routerfit.register(AppRouter.class).skipFaceBd3SignInActivity(false, false, "", false, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (result == Activity.RESULT_OK) {
                            String sResult = data.toString();
                            if (sResult.equals("success") || sResult.equals("skip")) {
                                dealVertifySuccess();
                            } else if (sResult.equals("failed")) {
                                Routerfit.setResult(Activity.RESULT_CANCELED, false);
                            }

                        }
                    }
                })));
    }

    private void goIdCardInfo(Bundle bundle) {
        Routerfit.register(AppRouter.class).skipIdCardInfoActivity("login", bundle, new ActivityCallback() {
            @Override
            public void onActivityResult(int result, Object data) {
                if (result == Activity.RESULT_OK) {
                    dealVertifySuccess();
                } else {
                    Routerfit.setResult(Activity.RESULT_CANCELED, false);
                }
            }
        });
    }

    private void dealVertifySuccess() {
        Intent extra = getIntent();
        if (extra != null) {
            if (!extra.getBooleanExtra("isInterceptor", false)) {
                Routerfit.register(AppRouter.class).skipMainActivity();
            } else {
                Routerfit.setResult(Activity.RESULT_OK, true);
            }
        } else {
            Routerfit.register(AppRouter.class).skipMainActivity();
        }
    }

}
