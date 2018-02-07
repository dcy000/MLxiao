package com.example.han.referralproject.measure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DiseaseDetailsActivity;
import com.example.han.referralproject.video.VideoListActivity;
import com.example.han.referralproject.view.progress.RxRoundProgressBar;
import com.littlejie.circleprogress.CircleProgress;
import com.littlejie.circleprogress.WaveProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasureXuetangResultActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_measure_title)
    TextView tvMeasureTitle;
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
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.rpb_empty)
    RxRoundProgressBar rpbEmpty;
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.rpb_one)
    RxRoundProgressBar rpbOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.rpb_two)
    RxRoundProgressBar rpbTwo;
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.circleprogress)
    CircleProgress circleprogress;
    @BindView(R.id.ll_gaodi)
    LinearLayout llGaodi;
    @BindView(R.id.wave_progress_bar)
    WaveProgress waveProgressBar;
    @BindView(R.id.ll_fenshu)
    LinearLayout llFenshu;
    @BindView(R.id.line)
    View line;
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
    private String measureSum, measureZhengchangNum, measurePiangaoNum, measurePiandiNum, weekOneAvg, weekTwoAvg, weekEmptyAvg, fenshu, suggest, result;
    private Thread progressAnim;
    private float cp_piangao, cp_zhengchang, cp_piandi, cp_one, cp_two, cp_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_xuetang_result);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血糖结果分析");
        intent = getIntent();
        initProgressBar();
        initOther();

        speak("主人，您本次测量的血糖值是" + intent.getStringExtra("result") +
                "，本周空腹平均血糖值" + String.format("%.2f", Float.parseFloat(weekEmptyAvg)) + ",饭后一小时平均血糖值"
                + String.format("%.2f", Float.parseFloat(weekOneAvg)) + ",饭后两小时平均血糖值" +
                String.format("%.2f", Float.parseFloat(weekTwoAvg)) + ",健康分数" + fenshu + "分。" + suggest);

        tvSomethingAdvice.setOnClickListener(this);
        healthKnowledge.setOnClickListener(this);
    }

    private void initOther() {
        fenshu = intent.getStringExtra("fenshu");
        suggest = intent.getStringExtra("suggest");
        result = intent.getStringExtra("result");

//        result="8.5";
//        fenshu="85";
//        suggest="测试测试";

        waveProgressBar.setMaxValue(100);
        waveProgressBar.setValue(Float.parseFloat(fenshu));
        tvSuggest.setText(suggest);

        circleprogress.setValue(Float.parseFloat(result));
    }

    private void initProgressBar() {

        measureSum = intent.getStringExtra("measure_sum");
        measureZhengchangNum = intent.getStringExtra("measure_zhengchang_num");
        measurePiangaoNum = intent.getStringExtra("measure_piangao_num");
        measurePiandiNum = intent.getStringExtra("measure_piandi_num");
        weekOneAvg = intent.getStringExtra("week_avg_one");
        weekTwoAvg = intent.getStringExtra("week_avg_two");
        weekEmptyAvg = intent.getStringExtra("week_avg_empty");

//        measureSum="20";
//        measureZhengchangNum="14";
//        measurePiangaoNum="5";
//        measurePiandiNum="10";
//        weekOneAvg="5.3";
//        weekTwoAvg="5.1";
//        weekEmptyAvg="5.0";


        rpbGao.setMax(Float.parseFloat(measureSum));
        rpbZhengchang.setMax(Float.parseFloat(measureSum));
        rpbDi.setMax(Float.parseFloat(measureSum));
        rpbEmpty.setMax(10);
        rpbOne.setMax(10);
        rpbTwo.setMax(10);

        tvMeasureTitle.setText("周测量：" + measureSum + "次");
        tvGao.setText(measurePiangaoNum + "次");
        tvZhengchang.setText(measureZhengchangNum + "次");
        tvDi.setText(measurePiandiNum + "次");
        tvEmpty.setText(String.format("%.2f", Float.parseFloat(weekEmptyAvg)));
        tvOne.setText(String.format("%.2f", Float.parseFloat(weekOneAvg)));
        tvTwo.setText(String.format("%.2f", Float.parseFloat(weekTwoAvg)));

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
                    if (cp_empty < Float.parseFloat(weekEmptyAvg)) {
                        cp_empty += rpbEmpty.getMax() * 0.01;
                        handler.sendEmptyMessage(3);
                    }
                    if (cp_one < Float.parseFloat(weekOneAvg)) {
                        cp_one += rpbOne.getMax() * 0.01;
                        handler.sendEmptyMessage(4);
                    }
                    if (cp_two < Float.parseFloat(weekTwoAvg)) {
                        cp_two += rpbTwo.getMax() * 0.01;
                        handler.sendEmptyMessage(5);
                    }
                    if (cp_piangao >= Float.parseFloat(measurePiangaoNum) &&
                            cp_zhengchang >= Float.parseFloat(measureZhengchangNum) &&
                            cp_piandi >= Float.parseFloat(measurePiandiNum) &&
                            cp_empty >= Float.parseFloat(weekEmptyAvg) &&
                            cp_one >= Float.parseFloat(weekOneAvg) &&
                            cp_two >= Float.parseFloat(weekTwoAvg)
                            ) {
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
                case 3://空腹
                    rpbEmpty.setProgress(cp_empty);
                    break;
                case 4://饭后一小时
                    rpbOne.setProgress(cp_one);
                    break;
                case 5://饭后两小时
                    rpbTwo.setProgress(cp_two);
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
                startActivity(new Intent(this, DiseaseDetailsActivity.class)
                        .putExtra("type", "糖尿病"));
                break;
            case R.id.health_knowledge:
                startActivity(new Intent(this, VideoListActivity.class));
                break;
        }
    }
}
