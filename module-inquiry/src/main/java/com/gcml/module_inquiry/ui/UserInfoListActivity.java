package com.gcml.module_inquiry.ui;

import android.os.Bundle;
import android.view.View;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.module_inquiry.R;
import com.sjtu.yifei.route.Routerfit;

public class UserInfoListActivity extends ToolbarBaseActivity implements View.OnClickListener {

    private com.gcml.common.widget.toolbar.TranslucentToolBar tb_inquiry_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mToolbar.setVisibility(View.GONE);
//        ActivityHelper.addActivity(this);
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
                        Routerfit.register(AppRouter.class).skipSettingActivity();
//                                CC.obtainBuilder("com.gcml.old.setting").build().call();
//                        onRightClickWithPermission(new IAction() {
//                            @Override
//                            public void action() {
//                            }
//                        });
//                        startActivity(new Intent(UserInfoListActivity.this, BindDoctorActivity.class));
                    }
                });
        setWifiLevel(tb_inquiry_home);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
    }
}
