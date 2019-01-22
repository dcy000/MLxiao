package com.gcml.module_inquiry.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.billy.cc.core.component.CC;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.module_inquiry.R;

public class UserInfoListActivity extends BaseActivity implements View.OnClickListener {

    private com.gcml.common.widget.toolbar.TranslucentToolBar tb_inquiry_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActivityHelper.addActivity(this);
        bindViews();
    }

    private void bindViews() {
        tb_inquiry_home = (com.gcml.common.widget.toolbar.TranslucentToolBar) findViewById(R.id.tb_user_info);
        tb_inquiry_home.setData("个 人 信 息 列 表",
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
//                        startActivity(new Intent(UserInfoListActivity.this, BindDoctorActivity.class));
                    }
                });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}
