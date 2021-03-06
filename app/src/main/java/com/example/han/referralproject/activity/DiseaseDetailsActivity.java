package com.example.han.referralproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DiseaseResult;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.lib_utils.display.ToastUtils;

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
        mTitleText.setText("病  症  详  情");
        initView();

    }

    private void initView() {
        mContent = findViewById(R.id.content);
        mRbReason = findViewById(R.id.rb_reason);
        mRbReason.setOnClickListener(this);
        mRbSuggest = findViewById(R.id.rb_suggest);
        mRbSuggest.setOnClickListener(this);
        mRbEating = findViewById(R.id.rb_eating);
        mRbEating.setOnClickListener(this);
        mRbSport = findViewById(R.id.rb_sport);
        mRbSport.setOnClickListener(this);
        mRgDisease = findViewById(R.id.rg_disease);
        mRgDisease.setOnCheckedChangeListener(this);
        //默认第一个选中
        mRbReason.setChecked(true);
        mData = (SymptomResultBean.bqs) getIntent().getSerializableExtra("data");
        if (mData == null) {
            NetworkApi.getJibing(getIntent().getStringExtra("type"), new NetworkManager.SuccessCallback<DiseaseResult>() {
                @Override
                public void onSuccess(DiseaseResult response) {
                    mData = new SymptomResultBean.bqs();
                    mData.setBname(response.bname);
                    mData.setEat(response.eat);
                    mData.setReview(response.review);
                    mData.setSuggest(response.suggest);
                    mData.setSports(response.sports);
                    mData.setGl("0");

                    mContent.setText(mData.getReview());
                    speak(response.review + "。" + response.getSuggest() + "。" + response.getSports());
                }
            }, new NetworkManager.FailedCallback() {
                @Override
                public void onFailed(String message) {
                    ToastUtils.showShort(message);
                }
            });
        } else {
            mContent.setText(mData.getReview());
            speak(mData.getReview() + "。" + mData.getSuggest() + "。" + mData.getSports());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                if (mData != null)
                    mContent.setText(mData.getReview());
                break;
            case R.id.rb_suggest:
                if (mRbSuggest != null) {
                    mRbSuggest.setChecked(true);
                }
                if (mData != null)
                    mContent.setText(mData.getSuggest());
                break;
            case R.id.rb_eating:
                mRbEating.setChecked(true);
                if (mData != null)
                    mContent.setText(mData.getEat());
                break;
            case R.id.rb_sport:
                mRbSport.setChecked(true);
                if (mData != null)
                    mContent.setText(mData.getSports());
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
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
