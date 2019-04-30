package com.gcml.auth.ui.signup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.billy.cc.core.component.CC;

import com.gcml.auth.R;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/auth/user/protocol/activity")
public class UserProtocolActivity extends AppCompatActivity {

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_user_protocol);
        TranslucentToolBar toolBar = findViewById(R.id.toolbar);
        toolBar.setData("用户协议", R.drawable.common_icon_back, "返回",
                0, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
//                        CC.obtainBuilder("com.gcml.old.home")
//                                .build().callAsync();
//                        finish();
                    }
                });
    }
}
