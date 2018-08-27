package com.example.han.referralproject.measure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.NormalHightActivity;
import com.example.han.referralproject.hypertensionmanagement.bean.DiagnoseInfoBean;
import com.example.han.referralproject.hypertensionmanagement.dialog.FllowUpTimesDialog;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_widget.progressbar.RoundProgressBar;
import com.google.gson.Gson;
import com.littlejie.circleprogress.WaveProgress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/30 9:19
 * created by:gzq
 * description:单次血压测量结果展示页面
 */
public class NewMeasureBloodpressureResultActivity extends BaseActivity implements View.OnClickListener {


    /**
     * 本周测量次数
     */
    private TextView mTvMeasureTitle;
    /**
     * 0次
     */
    private TextView mTvGao;
    private RoundProgressBar mRpbGao;
    /**
     * 0次
     */
    private TextView mTvZhengchang;
    private RoundProgressBar mRpbZhengchang;
    /**
     * 0次
     */
    private TextView mTvDi;
    private RoundProgressBar mRpbDi;
    /**
     * 120
     */
    private TextView mTvGaoya;
    private RoundProgressBar mRpbGaoya;
    /**
     * 80
     */
    private TextView mTvDiya;
    private RoundProgressBar mRpbDiya;
    private LinearLayout mLlLeft;
    /**
     * 本次测量结果
     */
    private TextView mTvResultTitle;
    /**
     * 120
     */
    private TextView mCurrentGaoya;
    private LinearLayout mLlGaoya;
    /**
     * 80
     */
    private TextView mCurrentDiya;
    private ConstraintLayout mLlGaodi;
    /**
     * 健康分数
     */
    private TextView mTvScoreTitle;
    /**
     * 正常
     */
    private TextView mTvState;
    private WaveProgress mWaveProgressBar;
    private ConstraintLayout mLlFenshu;
    /**
     * 健康建议
     */
    private TextView mTvSuggestTitle;
    /**
     * 主人，您血糖偏低，并有下降趋势，低血糖出现饥饿、头昏眼花、面色苍白、心慌手颤、出冷汗、虚弱无力等症状，
     * 低血糖还容易诱发心律失常，心绞痛、心肌梗死以及脑血管意外并发症，请持续测量，必要时及时联系医生。
     */
    private TextView mTvSuggest;
    /**
     * 风险评估
     */
    private TextView mTvSomethingAdvice;
    /**
     * 健康报告
     */
    private TextView mHealthKnowledge;
    private ConstraintLayout mLlRight;
    private String healthState;
    private int healthScore;
    private int currentHighBloodpressure;
    private int currentLowBloodpressure;
    private String currentSuggest;
    //非同日测量次数
    private Integer detectionDayCount;

    /**
     * @param context
     * @param state       血压状态：五种的一种
     * @param score       健康分数
     * @param currentHigh 当前高压
     * @param currentLow  当前低压
     * @param suggest     健康建议
     */
    public static void startActivity(Context context, String state, int score, int currentHigh,
                                     int currentLow, String suggest) {
        context.startActivity(new Intent(context, NewMeasureBloodpressureResultActivity.class)
                .putExtra("health_state", state)
                .putExtra("health_score", score)
                .putExtra("high_bloodpressure", currentHigh)
                .putExtra("low_bloodpressure", currentLow)
                .putExtra("suggest", suggest));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measure_result);
        initView();
        initViewColor();
        getData();
    }

    private void initViewColor() {
        switch (healthState) {
            case "异常增高":
            case "增高":
                mCurrentGaoya.setTextColor(Color.parseColor("#F56C6C"));
                mCurrentDiya.setTextColor(Color.parseColor("#F56C6C"));
                mTvState.setTextColor(Color.parseColor("#F56C6C"));
                mWaveProgressBar.setWaveDarkColor(Color.parseColor("#F96859"));
                mWaveProgressBar.setWaveLightColor(Color.parseColor("#EF9F95"));
                mWaveProgressBar.setValueColor(Color.parseColor("#ffffff"));
                break;
            case "正常高值":
            case "正常":
                mCurrentGaoya.setTextColor(Color.parseColor("#3ACC61"));
                mCurrentDiya.setTextColor(Color.parseColor("#3ACC61"));
                mTvState.setTextColor(Color.parseColor("#3ACC61"));
                mWaveProgressBar.setWaveDarkColor(Color.parseColor("#4FE08F"));
                mWaveProgressBar.setWaveLightColor(Color.parseColor("#9CF397"));
                mWaveProgressBar.setValueColor(Color.parseColor("#ffffff"));
                break;
            case "偏低":
                mCurrentGaoya.setTextColor(Color.parseColor("#F56C6C"));
                mCurrentDiya.setTextColor(Color.parseColor("#F56C6C"));
                mTvState.setTextColor(Color.parseColor("#F56C6C"));
                mWaveProgressBar.setWaveDarkColor(Color.parseColor("#F96859"));
                mWaveProgressBar.setWaveLightColor(Color.parseColor("#EF9F95"));
                mWaveProgressBar.setValueColor(Color.parseColor("#ffffff"));
                break;
        }
    }

    private void getData() {
        NetworkApi.getDiagnoseInfo(MyApplication.getInstance().userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                DiagnoseInfoBean bean = new Gson().fromJson(response.body(), DiagnoseInfoBean.class);
                if (bean != null && bean.tag && bean.data != null) {
                    detectionDayCount = bean.data.detectionDayCount;
                    if (detectionDayCount < 3) {
                        mHealthKnowledge.setBackgroundColor(Color.parseColor("#BBBBBB"));
                    }
                }
            }
        });

        Calendar curr = Calendar.getInstance();
        long weekAgoTime = curr.getTimeInMillis();
        OkGo.<String>get(NetworkApi.WeeklyOrMonthlyReport)
                .params("userId", MyApplication.getInstance().userId)
                .params("endTimeStamp", weekAgoTime)
                .params("num", "1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("请求成功", "onSuccess: " + response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                NewWeeklyOrMonthlyBean weeklyOrMonthlyReport = new Gson().
                                        fromJson(data.toString(), NewWeeklyOrMonthlyBean.class);
                                dealData(weeklyOrMonthlyReport);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShort("暂无周报告");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e("请求失败", "onError: " + response.message());
                        ToastUtils.showShort("暂无周报告");
                    }
                });

    }

    private void dealData(NewWeeklyOrMonthlyBean weeklyOrMonthlyReport) {
        if (weeklyOrMonthlyReport == null) {
            return;
        }
        List<NewWeeklyOrMonthlyBean.TargetListBean> targetList = weeklyOrMonthlyReport.getTargetList();
        if (targetList != null && targetList.size() > 0) {
            for (NewWeeklyOrMonthlyBean.TargetListBean targetListBean : targetList) {
                int target = targetListBean.getTarget();
                int num = targetListBean.getNum();
                switch (target) {
                    case -1:
                        mTvDi.setText(num + "次");
                        mRpbDi.setProgress(num);
                        break;
                    case 0:
                        mTvZhengchang.setText(num + "次");
                        mRpbZhengchang.setProgress(num);
                        break;
                    case 1:
                        mTvGao.setText(num + "次");
                        mRpbGao.setProgress(num);
                        break;
                }
            }
        }
        List<NewWeeklyOrMonthlyBean.WeekDateListBean> weekDateList = weeklyOrMonthlyReport.getWeekDateList();
        if (weekDateList != null && weekDateList.size() > 0) {
            NewWeeklyOrMonthlyBean.WeekDateListBean weekDateListBean = weekDateList.get(0);
            if (weekDateListBean != null) {
                int highPressureAvg = weekDateListBean.getHighPressureAvg();
                int lowPressureAvg = weekDateListBean.getLowPressureAvg();
                mRpbGaoya.setMax(180);
                mRpbGaoya.setProgress(highPressureAvg);
                mRpbDiya.setMax(100);
                mRpbDiya.setProgress(lowPressureAvg);
                mTvGaoya.setText(highPressureAvg + "");
                mTvDiya.setText(lowPressureAvg + "");
            }
        }

    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血压结果分析");
        mTvMeasureTitle = findViewById(R.id.tv_measure_title);
        mTvGao = findViewById(R.id.tv_gao);
        mRpbGao = findViewById(R.id.rpb_gao);
        mTvZhengchang = findViewById(R.id.tv_zhengchang);
        mRpbZhengchang = findViewById(R.id.rpb_zhengchang);
        mTvDi = findViewById(R.id.tv_di);
        mRpbDi = findViewById(R.id.rpb_di);
        mTvGaoya = findViewById(R.id.tv_gaoya);
        mRpbGaoya = findViewById(R.id.rpb_gaoya);
        mTvDiya = findViewById(R.id.tv_diya);
        mRpbDiya = findViewById(R.id.rpb_diya);
        mLlLeft = findViewById(R.id.ll_left);
        mTvResultTitle = findViewById(R.id.tv_result_title);
        mCurrentGaoya = findViewById(R.id.current_gaoya);
        mLlGaoya = findViewById(R.id.ll_gaoya);
        mCurrentDiya = findViewById(R.id.current_diya);
        mLlGaodi = findViewById(R.id.ll_gaodi);
        mTvScoreTitle = findViewById(R.id.tv_score_title);
        mTvState = findViewById(R.id.tv_state);
        mWaveProgressBar = findViewById(R.id.wave_progress_bar);
        mWaveProgressBar.setMaxValue(100.0f);
        mLlFenshu = findViewById(R.id.ll_fenshu);
        mTvSuggestTitle = findViewById(R.id.tv_suggest_title);
        mTvSuggest = findViewById(R.id.tv_suggest);
        mTvSomethingAdvice = findViewById(R.id.tv_something_advice);
        mTvSomethingAdvice.setOnClickListener(this);
        mHealthKnowledge = findViewById(R.id.health_knowledge);
        mHealthKnowledge.setOnClickListener(this);
        mLlRight = findViewById(R.id.ll_right);
        healthState = getIntent().getStringExtra("health_state");
        mTvState.setText(healthState);
        healthScore = getIntent().getIntExtra("health_score", 0);
        Timber.e("健康分数：" + healthScore);
        mWaveProgressBar.setValue(healthScore);
        mWaveProgressBar.setHealthValue(healthScore + "分");
        currentHighBloodpressure = getIntent().getIntExtra("high_bloodpressure", 120);
        currentLowBloodpressure = getIntent().getIntExtra("low_bloodpressure", 80);
        mCurrentGaoya.setText(String.valueOf(currentHighBloodpressure));
        mCurrentDiya.setText(String.valueOf(currentLowBloodpressure));
        currentSuggest = getIntent().getStringExtra("suggest");
        mTvSuggest.setText(currentSuggest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_something_advice:
                startActivity(new Intent(this, NormalHightActivity.class)
                        .putExtra("fromWhere", NewMeasureBloodpressureResultActivity
                                .class.getSimpleName()));
                break;
            case R.id.health_knowledge:
                if (detectionDayCount<3){
                    showLessThan3Dialog((3 - detectionDayCount) + "");
                    return;
                }
                startActivity(new Intent(this, TreatmentPlanActivity.class));
                break;
        }
    }

    private void showLessThan3Dialog(String notice) {
        FllowUpTimesDialog dialog = new FllowUpTimesDialog(notice);
        dialog.setListener(new FllowUpTimesDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm() {
                dialog.dismiss();
            }
        });
        dialog.show(getSupportFragmentManager(), "less3");
        mlSpeak("主人，您尚未满足3天测量标准，请在健康监测中测量三日");
    }
}
