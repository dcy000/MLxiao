package com.gcml.module_health_profile.checklist;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.billy.cc.core.component.CC;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_health_profile.R;

/**
 * Created by lenovo on 2019/4/23.
 */

public class HealthCheckListActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_health_check_list);

        TranslucentToolBar tb = findViewById(R.id.tb_check_list);
        tb.setData("健 康 体 检 表",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
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
        setWifiLevel(tb);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_contaniner_check_list,
                        CheckListFragment.newInstance(null, null))
                .commitAllowingStateLoss();
    }
}