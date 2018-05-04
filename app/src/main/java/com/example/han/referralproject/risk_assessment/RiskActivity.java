package com.example.han.referralproject.risk_assessment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

/**
 * Created by Administrator on 2018/5/4.
 */

public class RiskActivity extends BaseActivity {
    private TextView tvTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk);
        initView();
        tvTest.setTypeface(Typeface.createFromAsset(getAssets(),"font/DINEngschrift-Alternate.otf"));
        tvTest.setText("测试专用Abc12345");
    }

    private void initView() {
        tvTest = (TextView) findViewById(R.id.tv_test);
    }
}
