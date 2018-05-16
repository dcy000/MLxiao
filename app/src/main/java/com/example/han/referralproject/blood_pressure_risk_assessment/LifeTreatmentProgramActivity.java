package com.example.han.referralproject.blood_pressure_risk_assessment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.megvii.faceppidcardui.imageview.CircleImageView;

/**
 * Created by Administrator on 2018/5/5.
 */

public class LifeTreatmentProgramActivity extends BaseActivity {
    private CircleImageView mHead;
    private TextView mName;
    private LinearLayout mLlAgeSex;
    private ConstraintLayout mCl1;
    private TextView mWeight;
    private TextView mTvGaoya;
    private TextView mTvDiya;
    /**
     * 6.12
     */
    private TextView mTvXuetangEmpty;
    /**
     * 6.12
     */
    private TextView mTvXuetangOne;
    /**
     * 6.12
     */
    private TextView mTvXuetangTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_treatment_program);
        initView();
    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("生活治疗方案");
        mHead = (CircleImageView) findViewById(R.id.head);
        mName = (TextView) findViewById(R.id.name);
        mLlAgeSex = (LinearLayout) findViewById(R.id.ll_age_sex);
        mCl1 = (ConstraintLayout) findViewById(R.id.cl_1);
        mWeight = (TextView) findViewById(R.id.weight);
        mTvGaoya = (TextView) findViewById(R.id.tv_gaoya);
        mTvDiya = (TextView) findViewById(R.id.tv_diya);
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
