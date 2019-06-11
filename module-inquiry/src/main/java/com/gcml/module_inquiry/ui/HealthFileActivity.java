package com.gcml.module_inquiry.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

/**
 * Created by lenovo on 2019/1/16.
 */
@Route(path = "/inquiry/health/file/activity")
public class HealthFileActivity extends ToolbarBaseActivity {

    private TranslucentToolBar tb;
    private TextView confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_file);
        mToolbar.setVisibility(View.GONE);
        initView();
//        ActivityHelper.addActivity(this);
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
                        Routerfit.register(AppRouter.class).skipSettingActivity();
//                                        CC.obtainBuilder("com.gcml.old.setting").build().call();

                    }

                });
        setWifiLevel(tb);
    }

}
