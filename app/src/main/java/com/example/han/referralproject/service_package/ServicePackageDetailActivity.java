package com.example.han.referralproject.service_package;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.han.referralproject.R;
import com.gcml.common.utils.base.ToolbarBaseActivity;

public class ServicePackageDetailActivity extends ToolbarBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_package_detail);
        mTitleText.setText("套 餐 详 情");
        mRightView.setVisibility(View.GONE);
    }
}
