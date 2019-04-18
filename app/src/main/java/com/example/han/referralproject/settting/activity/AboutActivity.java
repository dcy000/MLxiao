package com.example.han.referralproject.settting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mTitleText.setText(getString(R.string.about_title));
        mToolbar.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        tvVersion.setText(getString(R.string.about_version) + Utils.getLocalVersionName(this));

    }
}
