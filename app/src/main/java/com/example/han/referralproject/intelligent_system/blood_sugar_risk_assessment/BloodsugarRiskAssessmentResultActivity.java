package com.example.han.referralproject.intelligent_system.blood_sugar_risk_assessment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.littlejie.circleprogress.CircleProgress;

/**
 * Created by Administrator on 2018/5/11.
 */

public class BloodsugarRiskAssessmentResultActivity extends BaseActivity {
    private TextView tvResult;
    private CircleProgress riskDisease;
    private CircleProgress healthScore;
    private PostBloodSugarResult data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodsugar_riskassessment_result);
        initView();
        dealData();
    }

    private void dealData() {
        if (data==null){
            return;
        }
        riskDisease.setMaxValue(100);
        String diseaseProbability = data.getDiseaseProbability();
        if (!TextUtils.isEmpty(diseaseProbability)) {
            riskDisease.setValue(Float.parseFloat(diseaseProbability));
        }
        riskDisease.setUnit(data.getCriticality()+"风险");
        healthScore.setMaxValue(100);
        healthScore.setValue(data.getScore());
        if (data.getScore()>=80){
            healthScore.setUnit("健康");
            healthScore.setGradientColors(new int[]{Color.parseColor("#3F86FC"),Color.parseColor("#3F86FC")});
        }else if (data.getScore()>60){
            healthScore.setUnit("亚健康");
            healthScore.setGradientColors(new int[]{Color.parseColor("#3F86FC"),Color.parseColor("#3F86FC")});
        }else if(data.getScore()>40){
            healthScore.setUnit("不健康");
            healthScore.setGradientColors(new int[]{Color.parseColor("#FF5A5A"),Color.parseColor("#FF5A5A")});
        }else{
            healthScore.setUnit("极不健康");
            healthScore.setGradientColors(new int[]{Color.parseColor("#FF5A5A"),Color.parseColor("#FF5A5A")});
        }
        if (!TextUtils.isEmpty(data.getAssessSuggest())){
            tvResult.setText(data.getAssessSuggest());

        }
        speak("主人，您三年内患糖尿病的概率为"+data.getDiseaseProbability()+"%,"+data.getAssessSuggest()+"本次评估分数为"+data.getScore()+"分");
    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血糖风险结果");
        tvResult = (TextView) findViewById(R.id.tv_reslut);
        riskDisease = (CircleProgress) findViewById(R.id.cp_1);
        healthScore = (CircleProgress) findViewById(R.id.cp_2);
        data = getIntent().getParcelableExtra("data");
    }
}
