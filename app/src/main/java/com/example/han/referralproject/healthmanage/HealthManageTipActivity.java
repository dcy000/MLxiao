package com.example.han.referralproject.healthmanage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.utils.UM;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2019/2/27.
 */

public class HealthManageTipActivity extends BaseActivity {
    @BindView(R.id.tb_health_tip)
    TranslucentToolBar tbHealthTip;
    @BindView(R.id.tv_health_tip_confirm)
    TextView tvHealthTipConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tip);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tbHealthTip.setData(UM.getString(R.string.health_management_title),
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

        setWifiLevel(tbHealthTip);

    }

    @OnClick(R.id.tv_health_tip_confirm)
    public void onViewClicked() {
        finish();
    }
}
