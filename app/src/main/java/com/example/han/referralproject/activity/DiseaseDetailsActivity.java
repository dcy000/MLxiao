package com.example.han.referralproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.medlink.danbogh.utils.T;

public class DiseaseDetailsActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private TextView mContent;
    /**
     * 疾病原因
     */
    private RadioButton mRbReason;
    /**
     * 医生建议
     */
    private RadioButton mRbSuggest;
    /**
     * 饮食调理
     */
    private RadioButton mRbEating;
    /**
     * 运动建议
     */
    private RadioButton mRbSport;
    private RadioGroup mRgDisease;
    private SymptomResultBean.bqs mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_details);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("病症详情");
        initView();

    }

    private void initView() {
        mData= (SymptomResultBean.bqs) getIntent().getSerializableExtra("data");
        mContent = (TextView) findViewById(R.id.content);
        mRbReason = (RadioButton) findViewById(R.id.rb_reason);
        mRbReason.setOnClickListener(this);
        mRbSuggest = (RadioButton) findViewById(R.id.rb_suggest);
        mRbSuggest.setOnClickListener(this);
        mRbEating = (RadioButton) findViewById(R.id.rb_eating);
        mRbEating.setOnClickListener(this);
        mRbSport = (RadioButton) findViewById(R.id.rb_sport);
        mRbSport.setOnClickListener(this);
        mRgDisease = (RadioGroup) findViewById(R.id.rg_disease);
        //默认第一个选中
        mRbReason.setChecked(true);
        mContent.setText(mData.getReview());
        mRgDisease.setOnCheckedChangeListener(this);
        speak(mData.getReview()+"。"+mData.getSuggest()+"。"+mData.getSports());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.rb_reason:
                mRbReason.setChecked(true);
                mContent.setText(mData.getReview());
                break;
            case R.id.rb_suggest:
                mRbSuggest.setChecked(true);
                mContent.setText(mData.getSuggest());
                break;
            case R.id.rb_eating:
                mRbEating.setChecked(true);
                mContent.setText(mData.getEat());
                break;
            case R.id.rb_sport:
                mRbSport.setChecked(true);
                mContent.setText(mData.getSports());
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_reason:
                mRbReason.setTextColor(Color.WHITE);
                mRbSuggest.setTextColor(Color.parseColor("#999999"));
                mRbEating.setTextColor(Color.parseColor("#999999"));
                mRbSport.setTextColor(Color.parseColor("#999999"));
                break;
            case R.id.rb_suggest:
                mRbReason.setTextColor(Color.parseColor("#999999"));
                mRbSuggest.setTextColor(Color.WHITE);
                mRbEating.setTextColor(Color.parseColor("#999999"));
                mRbSport.setTextColor(Color.parseColor("#999999"));
                break;
            case R.id.rb_eating:
                mRbReason.setTextColor(Color.parseColor("#999999"));
                mRbSuggest.setTextColor(Color.parseColor("#999999"));
                mRbEating.setTextColor(Color.WHITE);
                mRbSport.setTextColor(Color.parseColor("#999999"));
                break;
            case R.id.rb_sport:
                mRbReason.setTextColor(Color.parseColor("#999999"));
                mRbSuggest.setTextColor(Color.parseColor("#999999"));
                mRbEating.setTextColor(Color.parseColor("#999999"));
                mRbSport.setTextColor(Color.WHITE);
                break;
        }
    }
}
