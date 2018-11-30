package com.example.module_blood_pressure.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.module_blood_pressure.R;
import com.gcml.lib_widget.progressbar.RoundProgressBar;
import com.gzq.lib_bluetooth.bean.NewWeeklyOrMonthlyBean;
import com.gzq.lib_bluetooth.common.BaseBluetoothFragment;
import com.gzq.lib_bluetooth.service.BluetoothAPI;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.littlejie.circleprogress.WaveProgress;

import java.util.Calendar;
import java.util.List;

/**
 * Created by lenovo on 2018/9/20.
 */

public class ShowMeasureBloodpressureResultFragment extends BaseBluetoothFragment implements View.OnClickListener {
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
    private ProgressDialog mDialog;

    private String healthState;
    private int healthScore;
    private int currentHighBloodpressure;
    private int currentLowBloodpressure;
    private String currentSuggest;
    private boolean isTask;



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_something_advice) {
            emitEvent("To_WeeklyReportActivity");
        } else if (i == R.id.health_knowledge) {
            emitEvent("To_MonthlyReportActivity");
        } else {

        }
    }








    public void showLoadingDialog(String message) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new ProgressDialog(mContext);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setIndeterminate(true);
        mDialog.setMessage(message);
        mDialog.show();
    }

    public void hideLoadingDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_measure_activity_show_measure_bloodpressure_result;
    }

    @Override
    public void initParams(Bundle bundle) {
        if (bundle != null) {
            healthState = bundle.getString("health_state");
            healthScore = bundle.getInt("health_score", 0);
            currentHighBloodpressure = bundle.getInt("high_bloodpressure", 120);
            currentLowBloodpressure = bundle.getInt("low_bloodpressure", 80);
            currentSuggest = bundle.getString("suggest");
            isTask = bundle.getBoolean("isTask", false);

        }
        getData();
    }

    @Override
    public void initView(View view) {
        mTvMeasureTitle = view.findViewById(R.id.tv_measure_title);
        mTvGao = view.findViewById(R.id.tv_gao);
        mRpbGao = view.findViewById(R.id.rpb_gao);
        mTvZhengchang = view.findViewById(R.id.tv_zhengchang);
        mRpbZhengchang = view.findViewById(R.id.rpb_zhengchang);
        mTvDi = view.findViewById(R.id.tv_di);
        mRpbDi = view.findViewById(R.id.rpb_di);
        mTvGaoya = view.findViewById(R.id.tv_gaoya);
        mRpbGaoya = view.findViewById(R.id.rpb_gaoya);
        mTvDiya = view.findViewById(R.id.tv_diya);
        mRpbDiya = view.findViewById(R.id.rpb_diya);
        mLlLeft = view.findViewById(R.id.ll_left);
        mTvResultTitle = view.findViewById(R.id.tv_result_title);
        mCurrentGaoya = view.findViewById(R.id.current_gaoya);
        mLlGaoya = view.findViewById(R.id.ll_gaoya);
        mCurrentDiya = view.findViewById(R.id.current_diya);
        mLlGaodi = view.findViewById(R.id.ll_gaodi);
        mTvScoreTitle = view.findViewById(R.id.tv_score_title);
        mTvState = view.findViewById(R.id.tv_state);
        mWaveProgressBar = view.findViewById(R.id.wave_progress_bar);
        mWaveProgressBar.setMaxValue(100.0f);
        mLlFenshu = view.findViewById(R.id.ll_fenshu);
        mTvSuggestTitle = view.findViewById(R.id.tv_suggest_title);
        mTvSuggest = view.findViewById(R.id.tv_suggest);
        mTvSomethingAdvice = view.findViewById(R.id.tv_something_advice);
        mTvSomethingAdvice.setOnClickListener(this);
        mHealthKnowledge = view.findViewById(R.id.health_knowledge);
        mHealthKnowledge.setOnClickListener(this);
        mLlRight = view.findViewById(R.id.ll_right);

        if (isTask) {
            mHealthKnowledge.setText("返回任务");
        } else {
            mHealthKnowledge.setText("健康方案");
        }

        mTvState.setText(healthState);
        mWaveProgressBar.setValue(healthScore);
        mWaveProgressBar.setHealthValue(healthScore + "分");
        mCurrentGaoya.setText(String.valueOf(currentHighBloodpressure));
        mCurrentDiya.setText(String.valueOf(currentLowBloodpressure));
        mTvSuggest.setText(currentSuggest);

        MLVoiceSynthetize.startSynthesize("主人，您本次测量高压" + currentHighBloodpressure + ",低压"
                + currentLowBloodpressure + ",健康分数" + healthScore + "分。" + currentSuggest);
        initViewColor();

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
            default:
                break;
        }
    }

    private void getData() {

        Calendar curr = Calendar.getInstance();
        long weekAgoTime = curr.getTimeInMillis();
        Box.getRetrofit(BluetoothAPI.class)
                .getWeeklyOrMonthlyReport(Box.getUserId(), weekAgoTime, "1")
                .compose(RxUtils.<NewWeeklyOrMonthlyBean>httpResponseTransformer())
                .as(RxUtils.<NewWeeklyOrMonthlyBean>autoDisposeConverter(this))
                .subscribe(new CommonObserver<NewWeeklyOrMonthlyBean>() {
                    @Override
                    public void onNext(NewWeeklyOrMonthlyBean newWeeklyOrMonthlyBean) {
                        dealData(newWeeklyOrMonthlyBean);
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
                    default:
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
    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }
}
