package com.example.han.referralproject.measure;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.intelligent_diagnosis.MonthlyReportActivity;
import com.example.han.referralproject.intelligent_diagnosis.WeeklyReportActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.view.progress.RxRoundProgressBar;
import com.littlejie.circleprogress.WaveProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasureXueyaResultActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_gao)
    TextView tvGao;
    @BindView(R.id.rpb_gao)
    RxRoundProgressBar rpbGao;
    @BindView(R.id.tv_di)
    TextView tvDi;
    @BindView(R.id.rpb_di)
    RxRoundProgressBar rpbDi;
    @BindView(R.id.tv_gaoya)
    TextView tvGaoya;
    @BindView(R.id.rpb_gaoya)
    RxRoundProgressBar rpbGaoya;
    @BindView(R.id.tv_diya)
    TextView tvDiya;
    @BindView(R.id.rpb_diya)
    RxRoundProgressBar rpbDiya;
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.tv_result_title)
    TextView tvResultTitle;
    @BindView(R.id.current_gao)
    TextView currentGao;
    @BindView(R.id.ll_gaoya)
    LinearLayout llGaoya;
    @BindView(R.id.current_di)
    TextView currentDi;
    @BindView(R.id.ll_gaodi)
    ConstraintLayout llGaodi;
    @BindView(R.id.tv_score_title)
    TextView tvScoreTitle;
    @BindView(R.id.wave_progress_bar)
    WaveProgress waveProgressBar;
    @BindView(R.id.ll_fenshu)
    ConstraintLayout llFenshu;
    @BindView(R.id.tv_suggest_title)
    TextView tvSuggestTitle;
    @BindView(R.id.tv_suggest)
    TextView tvSuggest;
    @BindView(R.id.tv_something_advice)
    TextView tvSomethingAdvice;
    @BindView(R.id.health_knowledge)
    TextView healthKnowledge;
    @BindView(R.id.ll_right)
    ConstraintLayout llRight;

    private Intent intent;
    private String weekGaoyaAvg, weekDiyaAvg, fenshu, suggest, mb_gaoya, mb_diya;
    private String currentGaoya, currentDiya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_result);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血压结果分析");
        intent = getIntent();
        initProgressBar();
        initOther();
        String weekMeasureGaoya = String.format("%.0f", Float.parseFloat(weekGaoyaAvg));
        String weekMeasureDiya = String.format("%.0f", Float.parseFloat(weekDiyaAvg));
        speak("主人，您本次测量的高压是" + intent.getStringExtra("current_gaoya") + ",低压是" + intent.getStringExtra("current_diya") +
                "，本周平均高压" + (weekMeasureGaoya.equals("-1") ? "暂无数据" : weekMeasureGaoya) + ",低压" +
                (weekMeasureDiya.equals("-1") ? "暂无数据" : weekMeasureDiya) + ",健康分数" + fenshu + "分。" + suggest);
        tvSomethingAdvice.setOnClickListener(this);
        healthKnowledge.setOnClickListener(this);
    }

    private void initOther() {
        fenshu = intent.getStringExtra("fenshu");
        suggest = intent.getStringExtra("suggest");


        float fenshuNum = Float.parseFloat(fenshu);

        if (fenshuNum >= 80) {
            waveProgressBar.setWaveDarkColor(Color.parseColor("#5BD78C"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#86F77D"));
            waveProgressBar.setValueColor(Color.parseColor("#ffffff"));
        } else if (fenshuNum >= 60) {
            waveProgressBar.setWaveDarkColor(Color.parseColor("#F78237"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#FBBF81"));
            waveProgressBar.setValueColor(Color.parseColor("#ffffff"));
        } else {
            waveProgressBar.setWaveDarkColor(Color.parseColor("#FE5848"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#F88A78"));
            waveProgressBar.setValueColor(Color.parseColor("#FE5848"));
        }
        waveProgressBar.setMaxValue(100.0f);
        waveProgressBar.setValue(Float.parseFloat(fenshu));
        waveProgressBar.setHealthValue(fenshu + "分");
        tvSuggest.setText(suggest);

        currentGaoya = intent.getStringExtra("current_gaoya");
        currentDiya = intent.getStringExtra("current_diya");

        currentGao.setText(currentGaoya);
        currentDi.setText(currentDiya);
        int oldScore = LocalShared.getInstance(this).getHealthScore();
        if (oldScore == 0) {

        }
        LocalShared.getInstance(this).setHealthScore((int) fenshuNum);
    }

    private void initProgressBar() {

        weekGaoyaAvg = intent.getStringExtra("week_avg_gaoya");
        weekDiyaAvg = intent.getStringExtra("week_avg_diya");
        mb_gaoya = intent.getStringExtra("mb_gaoya");
        mb_diya = intent.getStringExtra("mb_diya");

        rpbGao.setMax(180);
        rpbDi.setMax(100);
        float mb_gaoya_f = Float.parseFloat(mb_gaoya);
        float mb_diya_f = Float.parseFloat(mb_diya);
        rpbGao.setProgress(mb_gaoya_f);
        rpbDi.setProgress(mb_diya_f);
        if (!TextUtils.isEmpty(mb_gaoya)) {
            if ("-1".equals(mb_gaoya))
                tvGao.setText("未测量");
            else
                tvGao.setText(String.format("%.0f", mb_gaoya_f));
        }
        if (!TextUtils.isEmpty(mb_diya)) {
            if ("-1".equals(mb_diya))
                tvDi.setText("未测量");
            else
                tvDi.setText(String.format("%.0f", mb_diya_f));
        }


        rpbGaoya.setMax(180);
        rpbDiya.setMax(100);
        float avg_gaoya_f = Float.parseFloat(weekGaoyaAvg);
        float avg_diya_f = Float.parseFloat(weekDiyaAvg);
        rpbGaoya.setProgress(avg_gaoya_f);
        rpbDiya.setProgress(avg_diya_f);
        if (!TextUtils.isEmpty(weekGaoyaAvg)) {
            if ("-1".equals(weekGaoyaAvg))
                tvGaoya.setText("未测量");
            else
                tvGaoya.setText(String.format("%.0f", Float.parseFloat(weekGaoyaAvg)));
        }
        if (!TextUtils.isEmpty(weekDiyaAvg)) {
            if ("-1".equals(weekDiyaAvg))
                tvDiya.setText("未测量");
            else
                tvDiya.setText(String.format("%.0f", Float.parseFloat(weekDiyaAvg)));
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_something_advice:
                startActivity(new Intent(this, WeeklyReportActivity.class));
                break;
            case R.id.health_knowledge:
                startActivity(new Intent(this, MonthlyReportActivity.class));
                break;
        }
    }
}
