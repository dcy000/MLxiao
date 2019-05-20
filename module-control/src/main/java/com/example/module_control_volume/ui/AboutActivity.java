package com.example.module_control_volume.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.module_control_volume.R;
import com.gcml.common.utils.AppUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/module/control/about/activity")
public class AboutActivity extends ToolbarBaseActivity {

    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();

    }

    private void initView() {
        tvVersion= findViewById(R.id.tv_version);
        mTitleText.setText("关于");
        mToolbar.setVisibility(View.VISIBLE);
        tvVersion.setText("版本:" + AppUtils.getAppInfo().getVersionName());

    }
}
