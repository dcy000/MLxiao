package com.example.han.referralproject.measure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.intelligent_system.blood_sugar_risk_assessment.BloodsugarRiskAssessmentActivity;
import com.example.han.referralproject.intelligent_system.intelligent_diagnosis.BloodsugarMonthlyReportActivity;
import com.example.han.referralproject.intelligent_system.intelligent_diagnosis.BloodsugarWeeklyReportActivity;
import com.example.han.referralproject.view.progress.RxRoundProgressBar;
import com.littlejie.circleprogress.WaveProgress;

public class MeasureXuetangResultActivity extends BaseActivity implements View.OnClickListener {

    private Intent intent;
    private String measureSum, measureZhengchangNum, measurePiangaoNum, measurePiandiNum, weekOneAvg, weekTwoAvg, weekEmptyAvg, fenshu, suggest, result;
    private Thread progressAnim;
    private float cp_piangao, cp_zhengchang, cp_piandi, cp_one, cp_two, cp_empty;
    private TextView tvMeasureTitle;
    private TextView tvGao;
    private RxRoundProgressBar rpbGao;
    private TextView tvZhengchang;
    private RxRoundProgressBar rpbZhengchang;
    private TextView tvDi;
    private RxRoundProgressBar rpbDi;
    private TextView tvEmpty;
    private RxRoundProgressBar rpbEmpty;
    private TextView tvOne;
    private RxRoundProgressBar rpbOne;
    private TextView tvTwo;
    private RxRoundProgressBar rpbTwo;
    private TextView measureType;
    private TextView currentXuetang;
    private WaveProgress waveProgressBar;
    private TextView tvSuggest;
    private TextView tvSomethingAdvice;
    private TextView healthKnowledge;
    private TextView currentXuetangUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_xuetang_result);
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血糖结果分析");
        intent = getIntent();
        initProgressBar();
        initOther();
        tvSomethingAdvice.setOnClickListener(this);
        healthKnowledge.setOnClickListener(this);
    }

    private void initOther() {
        fenshu = intent.getStringExtra("fenshu");
        suggest = intent.getStringExtra("suggest");
        result = intent.getStringExtra("result");

        waveProgressBar.setHealthValue(fenshu + "分");
        waveProgressBar.setMaxValue(100);
        float float_fenshu = Float.parseFloat(fenshu);
        waveProgressBar.setValue(float_fenshu);
        if (float_fenshu >= 80) {
            waveProgressBar.setWaveDarkColor(Color.parseColor("#5BD78C"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#86F77D"));
            waveProgressBar.setValueColor(Color.parseColor("#ffffff"));
        } else if (float_fenshu >= 60) {
            waveProgressBar.setWaveDarkColor(Color.parseColor("#F78237"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#FBBF81"));
            waveProgressBar.setValueColor(Color.parseColor("#ffffff"));
        } else {
            waveProgressBar.setWaveDarkColor(Color.parseColor("#FE5848"));
            waveProgressBar.setWaveLightColor(Color.parseColor("#F88A78"));
            waveProgressBar.setValueColor(Color.parseColor("#FE5848"));
        }
        tvSuggest.setText(suggest);
        int measure_type = intent.getIntExtra("measure_type", 0);
        String stringMeasureType = "空腹";
        switch (measure_type) {
            case 0:
                stringMeasureType = "空腹";
                measureType.setText("空腹");
                break;
            case 1:
                stringMeasureType = "饭后1小时";
                measureType.setText("饭后1小时");
                break;
            case 2:
                stringMeasureType = "饭后2小时";
                measureType.setText("饭后2小时");
                break;
        }

        float weekEmptyAvg_f = Float.parseFloat(weekEmptyAvg);
        float weekOneAvg_f = Float.parseFloat(weekOneAvg);
        float weekTwoAvg_f = Float.parseFloat(weekTwoAvg);
        String string_result = String.format("%.1f", Float.parseFloat(result));
        currentXuetang.setText(string_result);
        speak("主人，您本次测量的" + stringMeasureType + "血糖值是" + string_result +
                "，本周空腹平均血糖值" + (weekEmptyAvg_f == -1 ? "未测量" : String.format("%.1f", weekEmptyAvg_f)) + ",饭后一小时平均血糖值"
                + (weekOneAvg_f == -1 ? "未测量" : String.format("%.1f", weekOneAvg_f)) + ",饭后两小时平均血糖值" +
                (weekTwoAvg_f == -1 ? "未测量" : String.format("%.1f", weekTwoAvg_f)) + ",健康分数" + fenshu + "分。" + suggest);
    }

    private void initProgressBar() {

        measureSum = intent.getStringExtra("measure_sum");
        measureZhengchangNum = intent.getStringExtra("measure_zhengchang_num");
        measurePiangaoNum = intent.getStringExtra("measure_piangao_num");
        measurePiandiNum = intent.getStringExtra("measure_piandi_num");
        weekOneAvg = intent.getStringExtra("week_avg_one");
        weekTwoAvg = intent.getStringExtra("week_avg_two");
        weekEmptyAvg = intent.getStringExtra("week_avg_empty");

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
        if (!TextUtils.isEmpty(weekEmptyAvg))
            if ("-1".equals(weekEmptyAvg)) {
                tvEmpty.setText("未测量");
            } else {
                tvEmpty.setText(String.format("%.1f", Float.parseFloat(weekEmptyAvg)));
            }
        if (!TextUtils.isEmpty(weekOneAvg))
            if ("-1".equals(weekOneAvg)) {
                tvOne.setText("未测量");
            } else {
                tvOne.setText(String.format("%.1f", Float.parseFloat(weekOneAvg)));
            }
        if (!TextUtils.isEmpty(weekTwoAvg))
            if ("-1".equals(weekTwoAvg)) {
                tvTwo.setText("未测量");
            } else {
                tvTwo.setText(String.format("%.1f", Float.parseFloat(weekTwoAvg)));
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
            case R.id.tv_something_advice://风险评估
                startActivity(new Intent(this, BloodsugarRiskAssessmentActivity.class));
                break;
            case R.id.health_knowledge://健康报告
                showPopwindow();
                break;
        }
    }

    private PopupWindow popupWindow;
    private View popupView;
    private TranslateAnimation animation;

    private void showPopwindow() {
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.item_choose_weekly_or_monthly, null);
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lighton();
                }
            });
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);

            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);

            popupView.findViewById(R.id.tv_monthly).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MeasureXuetangResultActivity.this, BloodsugarMonthlyReportActivity.class));
                    popupWindow.dismiss();
                    lighton();
                }
            });
            popupView.findViewById(R.id.tv_weekly).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MeasureXuetangResultActivity.this, BloodsugarWeeklyReportActivity.class));
                    popupWindow.dismiss();
                    lighton();
                }
            });
            popupView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    lighton();
                }
            });
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            lighton();
        }

        popupWindow.showAtLocation(healthKnowledge, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.startAnimation(animation);
        lightoff();
    }

    private void lightoff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    private void initView() {
        tvMeasureTitle = (TextView) findViewById(R.id.tv_measure_title);
        tvGao = (TextView) findViewById(R.id.tv_gao);
        rpbGao = (RxRoundProgressBar) findViewById(R.id.rpb_gao);
        tvZhengchang = (TextView) findViewById(R.id.tv_zhengchang);
        rpbZhengchang = (RxRoundProgressBar) findViewById(R.id.rpb_zhengchang);
        tvDi = (TextView) findViewById(R.id.tv_di);
        rpbDi = (RxRoundProgressBar) findViewById(R.id.rpb_di);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        rpbEmpty = (RxRoundProgressBar) findViewById(R.id.rpb_empty);
        tvOne = (TextView) findViewById(R.id.tv_one);
        rpbOne = (RxRoundProgressBar) findViewById(R.id.rpb_one);
        tvTwo = (TextView) findViewById(R.id.tv_two);
        rpbTwo = (RxRoundProgressBar) findViewById(R.id.rpb_two);
        measureType = (TextView) findViewById(R.id.measure_type);
        currentXuetang = (TextView) findViewById(R.id.currentXuetang);
        currentXuetang.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
        waveProgressBar = (WaveProgress) findViewById(R.id.wave_progress_bar);
        tvSuggest = (TextView) findViewById(R.id.tv_suggest);
        tvSomethingAdvice = (TextView) findViewById(R.id.tv_something_advice);
        tvSomethingAdvice.setOnClickListener(this);
        healthKnowledge = (TextView) findViewById(R.id.health_knowledge);
        healthKnowledge.setOnClickListener(this);
        currentXuetangUnit = (TextView) findViewById(R.id.currentXuetang_unit);
        currentXuetangUnit.setTypeface(Typeface.createFromAsset(getAssets(), "font/DINEngschrift-Alternate.otf"));
    }
}
