package com.example.module_login.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.module_login.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;

public class AgreementActivity extends ToolbarBaseActivity implements View.OnClickListener {


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_agreement;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mLeftText.setText("用户协议");
        findViewById(R.id.btn_sure).setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_sure) {
            finish();
        }
    }
}
