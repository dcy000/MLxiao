package com.example.han.referralproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.SymptomResultBean;

public class DiseaseDetails2Activity extends Activity implements View.OnClickListener {

    private SymptomResultBean.bqs mData;
    private ImageView mIconBack;
    private LinearLayout mLinearlayou;
    private ImageView mIconHome;
    /**
     * 冠心病
     */
    private TextView mTitle;
    private TextView mReason;
    private TextView mSuggest;
    private TextView mEating;
    private TextView mSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_details2);
        mData = (SymptomResultBean.bqs) getIntent().getSerializableExtra("data");
        initView();
    }

    private void initView() {
        mIconBack = (ImageView) findViewById(R.id.icon_back);
        mIconBack.setOnClickListener(this);
        mLinearlayou = (LinearLayout) findViewById(R.id.linearlayou);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.title);
        mReason = (TextView) findViewById(R.id.reason);
        mSuggest = (TextView) findViewById(R.id.suggest);
        mEating = (TextView) findViewById(R.id.eating);
        mSport = (TextView) findViewById(R.id.sport);

        mTitle.setText(mData.getBname());
        mReason.setText(mData.getReview());
        mSuggest.setText(mData.getSuggest());
        mEating.setText(mData.getEat());
        mSport.setText(mData.getSports());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.icon_back:
                finish();
                break;
            case R.id.icon_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
