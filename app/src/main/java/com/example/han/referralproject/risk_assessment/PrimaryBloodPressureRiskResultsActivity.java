package com.example.han.referralproject.risk_assessment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

/**
 * Created by Administrator on 2018/5/7.
 */

public class PrimaryBloodPressureRiskResultsActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvTitle;
    private TextView mTvHeadlineInfluencingFactors;
    private TextView mView1;
    private RecyclerView mFactorsList;
    private TextView mTvAdvice;
    private EssentialHypertension data;
    private StringBuffer stringBuffer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_risk_result);
        initView();
        dealData();
    }

    private void dealData() {
        if (data!=null){
            stringBuffer.append("根据您的检测结果，您有可能是<strong><font color='#333333'>原发性高血压</color></strong>。");
            if (data.getTargets()!=null){
                stringBuffer.append("并且有可能存在<strong><font color='#333333'>");
                for (String s:data.getTargets()) {
                    stringBuffer.append(s+"、");
                }
                stringBuffer.deleteCharAt(stringBuffer.length()-1);
                stringBuffer.append("</color></strong>的靶器官的损害。");
            }
            mTvTitle.setText(Html.fromHtml(stringBuffer.toString()));
            if (data.getPrimary().getIllnessFactor()!=null){
                GridLayoutManager manager=new GridLayoutManager(this,2);
                mFactorsList.setLayoutManager(manager);
                mFactorsList.setAdapter(new BaseQuickAdapter<String ,BaseViewHolder>(R.layout.factor_item,data.getPrimary().getIllnessFactor()) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, String s) {
                        baseViewHolder.setText(R.id.text,s);
                    }
                });
            }
        }


    }

    private void initView() {
        data = getIntent().getParcelableExtra("data");
        stringBuffer=new StringBuffer();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血压风险结果");
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvHeadlineInfluencingFactors = (TextView) findViewById(R.id.tv_headline_influencing_factors);
        mTvTitle.setOnClickListener(this);
        mTvHeadlineInfluencingFactors.setOnClickListener(this);
        mView1 = (TextView) findViewById(R.id.view_1);
        mFactorsList = (RecyclerView) findViewById(R.id.factors_list);
        mTvAdvice = (TextView) findViewById(R.id.tv_advice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_title:
                break;
            case R.id.tv_headline_influencing_factors:
                break;
        }
    }
}
