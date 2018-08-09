package com.example.han.referralproject.blood_pressure_risk_assessment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import java.util.List;

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
            String target = "";
            String stringSpeak ="";
            if (data.getTargets() != null) {
                for (String s:data.getTargets()) {
                    target+=s+"、";
                }
            }
            if (!TextUtils.isEmpty(target)&&target.length()>1){
                stringSpeak="主人，根据您的评估结果，您有可能是原发性高血压，并有可能存在对"+target.substring(0,target.length()-1)+"等靶器官的损害。";
            }else{
                stringSpeak="主人，根据您的评估结果，您有可能是原发性高血压。";
            }
            List<String> illnessFactor = data.getPrimary().getIllnessFactor();
            if (illnessFactor !=null&&illnessFactor.size()>0){
                stringSpeak+="影响因素可能有：";
                for(int i=0;i<illnessFactor.size();i++){
                    stringSpeak+=(i+1)+illnessFactor.get(i)+";";
                }
            }
            stringSpeak+="建议您健康饮食，合理运动，根据康复疗程进行生活干预，必要时请咨询医生";
            speak(stringSpeak);

            stringBuffer.append("根据您的评估结果，您有可能是<strong><font color='#333333'>原发性高血压</color></strong>。");
            if (target!=null){
                stringBuffer.append("并且有可能存在对<strong><font color='#333333'>");
                stringBuffer.append(target);
                stringBuffer.deleteCharAt(stringBuffer.length()-1);
                stringBuffer.append("</color></strong>的靶器官的损害。");
            }
            mTvTitle.setText(Html.fromHtml(stringBuffer.toString()));
            if (illnessFactor!=null){
                GridLayoutManager manager=new GridLayoutManager(this,2);
                mFactorsList.setLayoutManager(manager);
                mFactorsList.setAdapter(new BaseQuickAdapter<String ,BaseViewHolder>(R.layout.factor_item,illnessFactor) {
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
        mTvTitle = findViewById(R.id.tv_title);
        mTvHeadlineInfluencingFactors = findViewById(R.id.tv_headline_influencing_factors);
        mTvTitle.setOnClickListener(this);
        mTvHeadlineInfluencingFactors.setOnClickListener(this);
        mView1 = findViewById(R.id.view_1);
        mFactorsList = findViewById(R.id.factors_list);
        mTvAdvice = findViewById(R.id.tv_advice);
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
