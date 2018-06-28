package com.example.han.referralproject.measure;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.blood_sugar_risk_assessment.BloodsugarRiskAssessmentActivity;
import com.littlejie.circleprogress.WaveProgress;

/**
 * Created by Administrator on 2018/5/9.
 */

public class New_MeasureXuetangResultActivity extends BaseActivity implements View.OnClickListener {
    private WaveProgress mWaveProgressBar;
    private TextView mTvAdvice;
    private TextView mRiskAssessment;
    private TextView mHealthReport;
    private TextView mTvCurrentXuetang;
    private TextView mTvCurrentXuetangUnit;
    private String measure_piangao_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measure_xuetang);
        initView();
    }

    private void initView() {
        mWaveProgressBar = (WaveProgress) findViewById(R.id.wave_progress_bar);
        mTvAdvice = (TextView) findViewById(R.id.tv_advice);
        mRiskAssessment = (TextView) findViewById(R.id.risk_assessment);
        mRiskAssessment.setOnClickListener(this);
        mHealthReport = (TextView) findViewById(R.id.health_report);
        mHealthReport.setOnClickListener(this);
        mTvCurrentXuetang = (TextView) findViewById(R.id.tv_current_xuetang);
        mTvCurrentXuetang.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvCurrentXuetangUnit = (TextView) findViewById(R.id.tv_current_xuetang_unit);
        mTvCurrentXuetangUnit.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));

        Intent intent = getIntent();
        if (intent != null) {
            measure_piangao_num = intent.getStringExtra("measure_piangao_num");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.risk_assessment://风险评估
                startActivity(new Intent(this, BloodsugarRiskAssessmentActivity.class));
                break;
            case R.id.health_report:
                break;
        }
    }
}
