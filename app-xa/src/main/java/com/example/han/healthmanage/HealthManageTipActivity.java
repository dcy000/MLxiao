package com.example.han.healthmanage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.sjtu.yifei.annotation.Go;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/health/HealthManageTipActivity/tip")
public class HealthManageTipActivity extends ToolbarBaseActivity {
    TranslucentToolBar tbHealthTip;
    TextView tvHealthTipConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_health_tip);
        initView();
    }

    private void initView() {
        tbHealthTip = findViewById(R.id.tb_health_tip);
        tvHealthTipConfirm = findViewById(R.id.tv_health_tip_confirm);

        tbHealthTip.setData("健 康 管 理",
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
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });

        setWifiLevel(tbHealthTip);

        tvHealthTipConfirm.setOnClickListener(v -> {
            finish();
        });

    }

}
