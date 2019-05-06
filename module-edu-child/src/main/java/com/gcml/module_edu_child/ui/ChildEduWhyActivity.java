package com.gcml.module_edu_child.ui;

import android.os.Bundle;
import android.view.View;

import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_edu_child.R;

public class ChildEduWhyActivity extends ToolbarBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_why);
        mToolbar.setVisibility(View.GONE);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("十  万  个  为  什  么");
    }
}
