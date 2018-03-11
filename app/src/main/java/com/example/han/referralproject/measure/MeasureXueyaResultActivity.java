package com.example.han.referralproject.measure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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
import com.example.han.referralproject.activity.DiseaseDetailsActivity;
import com.example.han.referralproject.formatter.MeasureFormatter;
import com.example.han.referralproject.intelligent_diagnosis.WeeklyReportActivity;
import com.example.han.referralproject.personal.PersonActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.video.VideoListActivity;
import com.example.han.referralproject.view.progress.RxRoundProgressBar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.littlejie.circleprogress.WaveProgress;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasureXueyaResultActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_gao)
    TextView tvGao;
    @BindView(R.id.rpb_gao)
    RxRoundProgressBar rpbGao;
    @BindView(R.id.tv_zhengchang)
    TextView tvZhengchang;
    @BindView(R.id.rpb_zhengchang)
    RxRoundProgressBar rpbZhengchang;
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
    @BindView(R.id.wave_progress_bar)
    WaveProgress waveProgressBar;
    @BindView(R.id.ll_fenshu)
    ConstraintLayout llFenshu;
    @BindView(R.id.tv_suggest_title)
    TextView tvSuggestTitle;
    @BindView(R.id.tv_something_advice)
    TextView tvSomethingAdvice;
    @BindView(R.id.health_knowledge)
    TextView healthKnowledge;
    @BindView(R.id.ll_right)
    ConstraintLayout llRight;
    @BindView(R.id.tv_measure_title)
    TextView tvMeasureTitle;
    @BindView(R.id.tv_suggest)
    TextView tvSuggest;
    @BindView(R.id.tv_result_gaoya)
    TextView tvResultGaoya;
    @BindView(R.id.tv_result_diya)
    TextView tvResultDiya;

    private String measureSum, measureZhengchangNum, measurePiangaoNum, measurePiandiNum;
    private Thread progressAnim;
    private Intent intent;
    private float cp_piangao, cp_zhengchang, cp_piandi, cp_gaoya, cp_diya;
    private String weekGaoyaAvg, weekDiyaAvg, fenshu, suggest;
    private String currentGaoya,currentDiya;
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
        speak("主人，您本次测量的高压是" + (TextUtils.isEmpty(currentGaoya)?"":currentGaoya) + ",低压是" + (TextUtils.isEmpty(currentDiya)?"":currentDiya) +
                "，本周平均高压" + String.format("%.0f",Float.parseFloat(weekGaoyaAvg)) + ",低压" + String.format("%.0f",Float.parseFloat(weekDiyaAvg)) + ",健康分数" + fenshu+ "分。" + suggest);
        tvSomethingAdvice.setOnClickListener(this);
        healthKnowledge.setOnClickListener(this);
    }

    private void initOther() {
        fenshu = intent.getStringExtra("fenshu");
        suggest = intent.getStringExtra("suggest");


        float fenshuNum=Float.parseFloat(fenshu);
        if (fenshuNum>=80){
            waveProgressBar.setWaveDarkColor(Color.parseColor("#5BD78C"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#86F77D"));
            waveProgressBar.setValueColor(Color.parseColor("#ffffff"));
        }else if(fenshuNum>=60){
            waveProgressBar.setWaveDarkColor(Color.parseColor("#F78237"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#FBBF81"));
            waveProgressBar.setValueColor(Color.parseColor("#ffffff"));
        }else{
            waveProgressBar.setWaveDarkColor(Color.parseColor("#FE5848"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#F88A78"));
            waveProgressBar.setValueColor(Color.parseColor("#FE5848"));
        }
        waveProgressBar.setMaxValue(100.0f);
        waveProgressBar.setValue(Float.parseFloat(fenshu));
        waveProgressBar.setHealthValue(fenshu+"分");
        tvSuggest.setText(suggest);

        currentGaoya=intent.getStringExtra("current_gaoya");
        currentDiya=intent.getStringExtra("current_diya");

        tvResultGaoya.setText(currentGaoya);
        tvResultDiya.setText(currentDiya);
    }

    private void initProgressBar() {

        measureSum = intent.getStringExtra("measure_sum");
        measureZhengchangNum = intent.getStringExtra("measure_zhengchang_num");
        measurePiangaoNum = intent.getStringExtra("measure_piangao_num");
        measurePiandiNum = intent.getStringExtra("measure_piandi_num");
        weekGaoyaAvg = intent.getStringExtra("week_avg_gaoya");
        weekDiyaAvg = intent.getStringExtra("week_avg_diya");


        rpbGao.setMax(Float.parseFloat(measureSum));
        rpbZhengchang.setMax(Float.parseFloat(measureSum));
        rpbDi.setMax(Float.parseFloat(measureSum));
        rpbGaoya.setMax(180);
        rpbDiya.setMax(100);

        tvMeasureTitle.setText("周测量：" + measureSum + "次");
        tvGao.setText(measurePiangaoNum + "次");
        tvZhengchang.setText(measureZhengchangNum + "次");
        tvDi.setText(measurePiandiNum + "次");
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

        progressAnim = new Thread() {
            @Override
            public void run() {
                while (!progressAnim.isInterrupted()) {
                    if (cp_piangao < Float.parseFloat(measurePiangaoNum)) {
                        cp_piangao += rpbGao.getMax() * 0.01;
                        handler.sendEmptyMessage(0);
                    }
                    if (cp_zhengchang < Float.parseFloat(measureZhengchangNum)) {
                        cp_zhengchang += rpbZhengchang.getMax() * 0.01;
                        handler.sendEmptyMessage(1);
                    }
                    if (cp_piandi < Float.parseFloat(measurePiandiNum)) {
                        cp_piandi += rpbDi.getMax() * 0.01;
                        handler.sendEmptyMessage(2);
                    }
                    if (cp_gaoya < Float.parseFloat(weekGaoyaAvg)) {
                        cp_gaoya += rpbGaoya.getMax() * 0.01;
                        handler.sendEmptyMessage(3);
                    }
                    if (cp_diya < Float.parseFloat(weekDiyaAvg)) {
                        cp_diya += rpbDiya.getMax() * 0.01;
                        handler.sendEmptyMessage(4);
                    }
                    if (cp_piangao >= Float.parseFloat(measurePiangaoNum) &&
                            cp_zhengchang >= Float.parseFloat(measureZhengchangNum) &&
                            cp_piandi >= Float.parseFloat(measurePiandiNum) &&
                            cp_gaoya >= Float.parseFloat(weekGaoyaAvg) &&
                            cp_diya >= Float.parseFloat(weekDiyaAvg)) {
                        progressAnim.interrupt();
                    }
                    try {
                        Thread.sleep(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        progressAnim.start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://偏高
                    rpbGao.setProgress(cp_piangao);
                    break;
                case 1://正常
                    rpbZhengchang.setProgress(cp_zhengchang);
                    break;
                case 2://偏低
                    rpbDi.setProgress(cp_piandi);
                    break;
                case 3://高压
                    rpbGaoya.setProgress(cp_gaoya);
                    break;
                case 4://低压
                    rpbDiya.setProgress(cp_diya);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressAnim.isInterrupted();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_something_advice:
//                startActivity(new Intent(this, DiseaseDetailsActivity.class)
//                        .putExtra("type", "高血压"));
                startActivity(new Intent(this, WeeklyReportActivity.class));
                break;
            case R.id.health_knowledge:
                startActivity(new Intent(this, VideoListActivity.class));
                break;
        }
    }
}
