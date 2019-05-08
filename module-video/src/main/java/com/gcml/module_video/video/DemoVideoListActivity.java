package com.gcml.module_video.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_video.R;
import com.sjtu.yifei.route.Routerfit;

public class DemoVideoListActivity extends ToolbarBaseActivity {
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
