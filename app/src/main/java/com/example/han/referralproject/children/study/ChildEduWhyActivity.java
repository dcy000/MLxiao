package com.example.han.referralproject.children.study;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class ChildEduWhyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_why);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("十  万  个  为  什  么");
    }
}
