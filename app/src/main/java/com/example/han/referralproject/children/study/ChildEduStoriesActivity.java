package com.example.han.referralproject.children.study;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class ChildEduStoriesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_stories);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("故  事  会");
    }
}
