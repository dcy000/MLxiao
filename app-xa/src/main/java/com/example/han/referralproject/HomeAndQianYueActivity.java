package com.example.han.referralproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gcml.common.router.AppRouter;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.lib_widget.EclipseImageView;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/app/homepage/mainorqianyue/activity")
public class HomeAndQianYueActivity extends AppCompatActivity implements View.OnClickListener {

    private EclipseImageView qianyue;
    private EclipseImageView home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_or_qianyue);
        initView();
    }

    private void initView() {
        TranslucentToolBar title = findViewById(R.id.activity_home_or_qianyue);
        title.setData("手 环 设 备",
                0, null,
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });

        qianyue = findViewById(R.id.iv_home);
        home = findViewById(R.id.iv_qianyue);
        qianyue.setOnClickListener(this);
        home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == qianyue) {
            //签约
        } else if (v == home) {
            Routerfit.register(AppRouter.class).skipMainActivity();
        }
    }

}
