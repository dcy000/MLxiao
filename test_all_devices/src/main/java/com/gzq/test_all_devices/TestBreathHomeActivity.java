package com.gzq.test_all_devices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.module_blutooth_devices.others.BreathHome_Fragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/11 17:18
 * created by:gzq
 * description:TODO
 */
public class TestBreathHomeActivity extends ToolbarBaseActivity {
    private FrameLayout mFrame;
    private BreathHome_Fragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_breath_home);
        initView();
        fragment=new BreathHome_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();
    }

    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
    }

}
