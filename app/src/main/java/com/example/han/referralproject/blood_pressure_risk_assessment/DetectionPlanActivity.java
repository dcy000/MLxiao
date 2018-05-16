package com.example.han.referralproject.blood_pressure_risk_assessment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

/**
 * Created by Administrator on 2018/5/7.
 */

public class DetectionPlanActivity extends BaseActivity {
    private TextView mTvXueyaDetectionFrequency;
    private TextView mTvXuetangDetectionFrequency;
    private TextView mWeight;
    private TextView mTvXueyaTitle;
    private TextView mTvGaoya;
    private TextView mTvDiya;
    private TextView mTvXuetangTitle;
    private TextView mTvXuetangEmpty;
    private TextView mTvXuetangOne;
    private TextView mTvXuetangTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_plan);
        initView();

    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("检测计划");
        mTvXueyaDetectionFrequency = (TextView) findViewById(R.id.tv_xueya_detection_frequency);
        mTvXuetangDetectionFrequency = (TextView) findViewById(R.id.tv_xuetang_detection_frequency);
        mWeight = (TextView) findViewById(R.id.weight);
        mTvXueyaTitle = (TextView) findViewById(R.id.tv_xueya_title);
        mTvGaoya = (TextView) findViewById(R.id.tv_gaoya);
        mTvDiya = (TextView) findViewById(R.id.tv_diya);
        mTvXuetangTitle = (TextView) findViewById(R.id.tv_xuetang_title);
        mTvXuetangEmpty = (TextView) findViewById(R.id.tv_xuetang_empty);
        mTvXuetangOne = (TextView) findViewById(R.id.tv_xuetang_one);
        mTvXuetangTwo = (TextView) findViewById(R.id.tv_xuetang_two);
        mWeight.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvGaoya.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiya.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangEmpty.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangOne.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvXuetangTwo.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
    }
}
