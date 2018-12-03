package com.example.han.referralproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DiseaseResult;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.service.API;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

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
        mRgDisease.setOnCheckedChangeListener(this);
        //默认第一个选中
        mRbReason.setChecked(true);
        mData = (SymptomResultBean.bqs) getIntent().getSerializableExtra("data");
        if (mData == null) {
            Box.getRetrofit(API.class)
                    .queryDisableDetailByName(getIntent().getStringExtra("type"))
                    .compose(RxUtils.httpResponseTransformer())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new CommonObserver<DiseaseResult>() {
                        @Override
                        public void onNext(DiseaseResult diseaseResult) {
                            mData = new SymptomResultBean.bqs();
                            mData.setBname(diseaseResult.bname);
                            mData.setEat(diseaseResult.eat);
                            mData.setReview(diseaseResult.review);
                            mData.setSuggest(diseaseResult.suggest);
                            mData.setSports(diseaseResult.sports);
                            mData.setGl("0");

                            mContent.setText(mData.getReview());
                            MLVoiceSynthetize.startSynthesize(diseaseResult.review + "。" + diseaseResult.getSuggest() + "。" + diseaseResult.getSports());
                        }
                    });
        } else {
            mContent.setText(mData.getReview());
            MLVoiceSynthetize.startSynthesize(mData.getReview() + "。" + mData.getSuggest() + "。" + mData.getSports());
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
