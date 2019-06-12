package com.gcml.mod_doc_advisory.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.mod_doc_advisory.R;
import com.gcml.mod_doc_advisory.bean.DiseaseResult;
import com.gcml.mod_doc_advisory.bean.SymptomResultBean;
import com.gcml.mod_doc_advisory.net.QianYueRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/doctor/advisory/disease/details/activity")
public class DiseaseDetailsActivity extends ToolbarBaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private TextView mContent;
    /**
     * 疾病原因
     */
    private RadioButton mRbReason;
    /**
     * 签约医生建议
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
            new QianYueRepository()
                    .getJibing(getIntent().getStringExtra("type"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<DiseaseResult>() {
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
                            MLVoiceSynthetize.startSynthesize(UM.getApp(),diseaseResult.review + "。" + diseaseResult.getSuggest() + "。" + diseaseResult.getSports());
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showShort(e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mContent.setText(mData.getReview());
            MLVoiceSynthetize.startSynthesize(UM.getApp(),mData.getReview() + "。" + mData.getSuggest() + "。" + mData.getSports());
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.rb_reason) {
            mRbReason.setChecked(true);
            if (mData != null)
                mContent.setText(mData.getReview());

        } else if (i == R.id.rb_suggest) {
            if (mRbSuggest != null) {
                mRbSuggest.setChecked(true);
            }
            if (mData != null)
                mContent.setText(mData.getSuggest());

        } else if (i == R.id.rb_eating) {
            mRbEating.setChecked(true);
            if (mData != null)
                mContent.setText(mData.getEat());

        } else if (i == R.id.rb_sport) {
            mRbSport.setChecked(true);
            if (mData != null)
                mContent.setText(mData.getSports());

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_reason) {
            mRbReason.setTextColor(Color.WHITE);
            mRbSuggest.setTextColor(Color.parseColor("#999999"));
            mRbEating.setTextColor(Color.parseColor("#999999"));
            mRbSport.setTextColor(Color.parseColor("#999999"));

        } else if (checkedId == R.id.rb_suggest) {
            mRbReason.setTextColor(Color.parseColor("#999999"));
            mRbSuggest.setTextColor(Color.WHITE);
            mRbEating.setTextColor(Color.parseColor("#999999"));
            mRbSport.setTextColor(Color.parseColor("#999999"));

        } else if (checkedId == R.id.rb_eating) {
            mRbReason.setTextColor(Color.parseColor("#999999"));
            mRbSuggest.setTextColor(Color.parseColor("#999999"));
            mRbEating.setTextColor(Color.WHITE);
            mRbSport.setTextColor(Color.parseColor("#999999"));

        } else if (checkedId == R.id.rb_sport) {
            mRbReason.setTextColor(Color.parseColor("#999999"));
            mRbSuggest.setTextColor(Color.parseColor("#999999"));
            mRbEating.setTextColor(Color.parseColor("#999999"));
            mRbSport.setTextColor(Color.WHITE);

        }
    }
}
