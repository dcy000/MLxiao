package com.example.han.referralproject.risk_assessment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.GridViewDividerItemDecoration;

/**
 * Created by Administrator on 2018/5/7.
 */

public class SecondaryBloodPressureRiskResultsActivity extends BaseActivity {
    private TextView mTvTitle;

    private TextView mTvHeadlineInfluencingFactors;
    private RecyclerView mFactorsList;
    private RecyclerView mFactorsListDetails;
    private SecondaryHypertension data;
    private StringBuffer stringBuffer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_blood_pressure_risk_result);
        initView();
        dealData();
    }

    private void dealData() {
        if (data != null) {
            stringBuffer.append("根据您的检测结果，您有可能是<strong><font color='#333333'>继发性高血压</color></strong>。");
            if (data.getTargets() != null) {
                stringBuffer.append("并且有可能存在<strong><font color='#333333'>");
                for (String s : data.getTargets()) {
                    stringBuffer.append(s + "、");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                stringBuffer.append("</color></strong>的靶器官的损害。");
            }
            mTvTitle.setText(Html.fromHtml(stringBuffer.toString()));
            if (data.getSecondary().getIllnessFactor() != null) {
                GridLayoutManager manager = new GridLayoutManager(this, 2);
                manager.setSmoothScrollbarEnabled(true);
                mFactorsList.setLayoutManager(manager);
                mFactorsList.setNestedScrollingEnabled(false);
                mFactorsList.setHasFixedSize(true);
                mFactorsList.addItemDecoration(new GridViewDividerItemDecoration(100,26));
                mFactorsList.setAdapter(new BaseQuickAdapter<SecondaryHypertension.SecondaryBean.IllnessFactorBean, BaseViewHolder>(R.layout.factor_item, data.getSecondary().getIllnessFactor()) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, SecondaryHypertension.SecondaryBean.IllnessFactorBean s) {
                        baseViewHolder.setText(R.id.text, s.getName());
                    }
                });
                LinearLayoutManager manager1 = new LinearLayoutManager(this);
                manager1.setSmoothScrollbarEnabled(true);
                mFactorsListDetails.setLayoutManager(manager1);
                mFactorsListDetails.setHasFixedSize(true);
                mFactorsListDetails.setNestedScrollingEnabled(false);
                mFactorsListDetails.setAdapter(new BaseQuickAdapter<SecondaryHypertension.SecondaryBean.IllnessFactorBean, BaseViewHolder>(R.layout.factor_details_item, data.getSecondary().getIllnessFactor()) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, SecondaryHypertension.SecondaryBean.IllnessFactorBean illnessFactorBean) {
                        baseViewHolder.setText(R.id.tv_headline_influencing_factors, illnessFactorBean.getName());
                        baseViewHolder.setText(R.id.tv_content, illnessFactorBean.getInfo());
                    }
                });
            }
        }
    }

    private void initView() {
        stringBuffer = new StringBuffer();
        data = getIntent().getParcelableExtra("data");
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血压风险结果");
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvHeadlineInfluencingFactors = (TextView) findViewById(R.id.tv_headline_influencing_factors);
        mFactorsList = (RecyclerView) findViewById(R.id.factors_list);
        mFactorsListDetails = (RecyclerView) findViewById(R.id.factors_list_details);
    }
}
