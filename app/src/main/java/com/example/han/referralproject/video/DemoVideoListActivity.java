package com.example.han.referralproject.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.gcml.common.router.AppRouter;
import com.sjtu.yifei.route.Routerfit;

public class DemoVideoListActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_video_list);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 课 堂");
        mRightView.setImageResource(R.drawable.white_wifi_3);
    }

    @Override
    protected void backMainActivity() {
        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
    }
}
