package com.example.han.referralproject.measure;

import android.content.Intent;
import android.graphics.DashPathEffect;
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
import com.example.han.referralproject.formatter.MeasureFormatter;
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
    @BindView(R.id.tv_something_advice)
    TextView tvSomethingAdvice;
    @BindView(R.id.health_knowledge)
    TextView healthKnowledge;
    @BindView(R.id.ll_right)
    ConstraintLayout llRight;
    @BindView(R.id.tv_measure_title)
    TextView tvMeasureTitle;
    @BindView(R.id.lc_xueya_result)
    BarChart bcXueyaResult;
    @BindView(R.id.tv_suggest)
    TextView tvSuggest;
    private String measureSum, measureZhengchangNum, measurePiangaoNum, measurePiandiNum;
    private Thread progressAnim;
    private Intent intent;
    private float cp_piangao, cp_zhengchang, cp_piandi, cp_gaoya, cp_diya;
    private String weekGaoyaAvg, weekDiyaAvg, fenshu, suggest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_result);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血压结果分析");
        intent = getIntent();
        initProgressBar();
        initLineChart();
        initOther();
        speak("主人，您本次测量的高压是" + intent.getStringExtra("current_gaoya") + ",低压是" + intent.getStringExtra("current_diya") +
                "，本周平均高压" + String.format("%.0f",Float.parseFloat(weekGaoyaAvg)) + ",低压" + String.format("%.0f",Float.parseFloat(weekDiyaAvg)) + ",健康分数" + String.format("%1$.0f",Float.parseFloat(fenshu)) + "分。" + suggest);
        tvSomethingAdvice.setOnClickListener(this);
        healthKnowledge.setOnClickListener(this);
    }

    private void initOther() {
        fenshu = intent.getStringExtra("fenshu");
        suggest = intent.getStringExtra("suggest");
        waveProgressBar.setMaxValue(100);
        waveProgressBar.setValue(Float.parseFloat(fenshu));
        tvSuggest.setText(suggest);
    }

    private void initLineChart() {
        bcXueyaResult.getDescription().setEnabled(false);
        bcXueyaResult.setNoDataText("");
        bcXueyaResult.setTouchEnabled(false);
        XAxis xAxis = bcXueyaResult.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new MeasureFormatter());
        //绘制底部的X轴
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(2);
        xAxis.setTextSize(20);
        YAxis yLeftAxis = bcXueyaResult.getAxisLeft();
        yLeftAxis.setEnabled(false);
        yLeftAxis.setDrawGridLines(false);
        YAxis yRightAxis = bcXueyaResult.getAxisRight();
        yRightAxis.setEnabled(false);
        yRightAxis.setDrawGridLines(false);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0, Float.parseFloat(intent.getStringExtra("current_gaoya"))));
        yVals1.add(new BarEntry(1, Float.parseFloat(intent.getStringExtra("current_diya"))));
//        yVals1.add(new BarEntry(0, 115));
//        yVals1.add(new BarEntry(1, 78));
        BarDataSet set1;
        if (bcXueyaResult.getData() != null &&
                bcXueyaResult.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) bcXueyaResult.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            bcXueyaResult.getData().notifyDataChanged();
            bcXueyaResult.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setDrawIcons(false);
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            //去处左下角的指示器
            set1.setFormLineWidth(0f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{0f, 0f}, 0f));
            set1.setFormSize(0f);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(20f);
            data.setBarWidth(0.5f);
            bcXueyaResult.setData(data);
        }
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
        tvGaoya.setText(String.format("%.0f",Float.parseFloat(weekGaoyaAvg)));
        tvDiya.setText(String.format("%.0f",Float.parseFloat(weekDiyaAvg)));

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
        switch (v.getId()){
            case R.id.tv_something_advice:
                startActivity(new Intent(this, DiseaseDetailsActivity.class)
                        .putExtra("type", "高血压"));
                break;
            case R.id.health_knowledge:
                startActivity(new Intent(this, VideoListActivity.class));
                break;
        }
    }
}
