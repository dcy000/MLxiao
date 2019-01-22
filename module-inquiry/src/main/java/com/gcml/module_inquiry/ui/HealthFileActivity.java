package com.gcml.module_inquiry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;

/**
 * Created by lenovo on 2019/1/16.
 */

public class HealthFileActivity extends BaseActivity {

    private TranslucentToolBar tb;
    private TextView confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_file);
        initView();
        ActivityHelper.addActivity(this);
    }

    private void initView() {
        tb = findViewById(R.id.tb_health_file);
        tb.setData("居 民 健 康 档 案",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        onRightClickWithPermission(new BaseActivity.IAction() {
                            @Override
                            public void action() {
                                onRightClickWithPermission(new IAction() {
                                    @Override
                                    public void action() {
                                        CC.obtainBuilder("com.gcml.old.setting").build().call();
                                    }
                                });
                            }
                        });
                    }

                });
        setWifiLevel(tb);
    }

    private void toUserInfo() {
        startActivity(new Intent(HealthFileActivity.this, UserInfoListActivity.class));
    }
}
