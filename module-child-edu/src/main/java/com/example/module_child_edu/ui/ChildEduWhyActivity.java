package com.example.module_child_edu.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.module_child_edu.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;

public class ChildEduWhyActivity extends ToolbarBaseActivity {

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.ce_activity_why;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mTitleText.setText("十  万  个  为  什  么");
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }
}
