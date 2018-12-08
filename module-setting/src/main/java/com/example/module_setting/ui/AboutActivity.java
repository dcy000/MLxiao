package com.example.module_setting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.module_setting.R;
import com.example.module_setting.R2;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends ToolbarBaseActivity {

    @BindView(R2.id.tv_version)
    TextView tvVersion;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_about;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mTitleText.setText("关于");
        mToolbar.setVisibility(View.VISIBLE);
        tvVersion.setText("版本:" + AppUtils.getAppInfo().getVersionName());

    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }
}
