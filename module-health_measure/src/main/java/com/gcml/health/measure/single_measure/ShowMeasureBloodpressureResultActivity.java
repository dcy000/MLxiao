package com.gcml.health.measure.single_measure;

import android.app.ProgressDialog;
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

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.data.UserSpHelper;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.single_measure.bean.DiagnoseInfoBean;
import com.gcml.health.measure.single_measure.bean.NewWeeklyOrMonthlyBean;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.DialogSure;
import com.gcml.lib_utils.ui.dialog.DialogSureCancel;
import com.gcml.lib_widget.dialog.FllowUpTimesDialog;
import com.gcml.lib_widget.progressbar.RoundProgressBar;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
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
public class ShowMeasureBloodpressureResultActivity extends ToolbarBaseActivity implements View.OnClickListener {

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
    private ProgressDialog mDialog;
    private DiagnoseInfoBean.DataBean diagnoseInfo;

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
        context.startActivity(new Intent(context, ShowMeasureBloodpressureResultActivity.class)
                .putExtra("health_state", state)
                .putExtra("health_score", score)
                .putExtra("high_bloodpressure", currentHigh)
                .putExtra("low_bloodpressure", currentLow)
                .putExtra("suggest", suggest));
    }

    @Override
    protected void backMainActivity() {
        CCAppActions.jump2MainActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_show_measure_bloodpressure_result);
        initView();
        initViewColor();
        getData();
        getDiagnoseInfo();
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
        OkGo.<String>get(HealthMeasureApi.WeeklyOrMonthlyReport)
                .params("userId", UserSpHelper.getUserId())
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

        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                "主人，您本次测量高压" + currentHighBloodpressure + ",低压"
                        + currentLowBloodpressure + ",健康分数" + healthScore + "分。" + currentSuggest);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_something_advice) {
            CCAppActions.jump2NormalHightActivity("NewMeasureBloodpressureResultActivity");

        } else if (i == R.id.health_knowledge) {

            onclickHypertensionManage();
        } else {
        }
    }

    /**
     * 获取诊断的相关信息
     */
    private void getDiagnoseInfo() {
        showLoadingDialog("");
        HealthMeasureApi.getDiagnoseInfo(UserSpHelper.getUserId(), new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        DiagnoseInfoBean bean = new Gson().fromJson(response.body(), DiagnoseInfoBean.class);
                        if (bean != null && bean.tag && bean.data != null) {
                            diagnoseInfo = bean.data;
                        }
                    }

                    @Override
                    public void onFinish() {
                        hideLoadingDialog();
                        super.onFinish();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        hideLoadingDialog();
                    }
                }
        );

    }


    /**
     * 点击高血压管理 按钮
     */
    private void onclickHypertensionManage() {

        if (diagnoseInfo != null) {
            if (diagnoseInfo.result == null) {
                if (diagnoseInfo.hypertensionPrimaryState == null) {
                    //用户更新原发性信息
                    showOriginHypertensionDialog();
                } else if ("1".equals(diagnoseInfo.hypertensionPrimaryState)) {
                    onOriginClickYes();
                } else if ("0".equals(diagnoseInfo.hypertensionPrimaryState)) {
//                onOriginClickNo();
                    showOriginHypertensionDialog();
                }
            } else {
                DialogSure sure = new DialogSure(this);
                sure.setContent("您在7天内已生成过健康方案，点击健康方案可直接查看。");
                sure.setSure("健康方案");
                sure.show();
                sure.setOnClickSureListener(dialog1 -> {
                    dialog1.dismiss();
                    toSulotion();
                });
            }

        } else {
            ToastUtils.showShort("网络繁忙");
        }


    }

    private void showOriginHypertensionDialog() {
//        TwoChoiceDialog dialog = new TwoChoiceDialog("您是否诊断过原发性高血压且正在进行高血压规范治疗？(您的选择将影响您的健康方案，且一旦选择不可更改，请谨慎回答)", "是", "否");
//        dialog.setListener(this);
//        dialog.show(getFragmentManager(), "yuanfa");
//        mlSpeak("主人，您是否已确诊高血压且在治疗？");
        DialogSureCancel dialogSureCancel = new DialogSureCancel(this);
        dialogSureCancel.setContent("您是否诊断过原发性高血压且正在进行高血压规范治疗？(您的选择将影响您的健康方案，且一旦选择不可更改，请谨慎回答)");
        dialogSureCancel.getCancelView().setText("否");
        dialogSureCancel.getSureView().setText("是");
        dialogSureCancel.setOnClickCancelListener(null);
        dialogSureCancel.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {
                postOriginPertensionState("1");
                CC.obtainBuilder("app")
                        .setActionName("To_SlowDiseaseManagementTipActivity")
                        .build()
                        .call();
            }
        });
    }

    private void postOriginPertensionState(String state) {
        HealthMeasureApi.postOriginHypertension(state, UserSpHelper.getUserId(), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject object = new JSONObject(body);
                    if (object.getBoolean("tag")) {
                        if ("0".equals(state)) {
                            onOriginClickNo();
                        } else if ("1".equals(state)) {
                            onOriginClickNo();
                        }
                    } else {
                        ToastUtils.showShort(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 原发弹框点击否
     */
    private void onOriginClickNo() {

        if (diagnoseInfo != null && diagnoseInfo.hypertensionLevel == null) {
            if (diagnoseInfo != null) {
                if (diagnoseInfo.detectionDayCount != null) {
                    if (diagnoseInfo.detectionDayCount >= 3) {
                        judgeClass();
                    } else {
                        showLessThan3Dialog((3 - diagnoseInfo.detectionDayCount) + "");
                    }

                }
            } else {
                showLessThan3Dialog("0");
            }

        } else {
            toSulotion();
        }


    }

    /**
     * 判定等级
     */
    private void judgeClass() {
        Integer high = diagnoseInfo.highPressure;
        Integer low = diagnoseInfo.lowPressure;
        if (high == null || low == null) {
            return;
        }
//        高血压 high>=140 或 low>=90
//        正常高值 140>high>=120 或 90>low>=80
//        正常 90<=高压<120且60<=低压<80
//        偏低 高压<90 或 低压<60
        if (high >= 140 || low >= 90) {
            onHigh();
        } else if ((high < 140 && high >= 120) || (low < 90 && low >= 80)) {
            onNormalHigh();
        } else if ((high < 120 && high >= 90) && (low < 80 && low >= 60)) {
            onNormal();
        } else if (high < 90 || low < 60) {
            onLow();
        }

    }

    private void onLow() {
        CC.obtainBuilder("app")
                .setActionName("To_BasicInformationActivity")
                .addParam("fromWhere", "pressureFlat")
                .build()
                .call();
    }

    private void onNormal() {
        CC.obtainBuilder("app")
                .setActionName("To_BasicInformationActivity")
                .addParam("fromWhere", "pressureNormal")
                .build()
                .call();

    }

    private void onNormalHigh() {
        if (diagnoseInfo.risk == null) {
            CC.obtainBuilder("app")
                    .setActionName("To_BasicInformationActivity")
                    .addParam("fromWhere", "pressureNormalHigh")
                    .build()
                    .call();
        } else {
//            toDetete();
//            startActivity(new Intent(this, WeightMeasureActivity.class));

            CC.obtainBuilder("health_measure")
                    .setActionName("To_WeightManagerActivity")
                    .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                @Override
                public void onResult(CC cc, CCResult result) {
                    toSulotion();
                }
            });
        }

    }

    private void onHigh() {
        if (diagnoseInfo.hypertensionTarget == null) {
            CC.obtainBuilder("app")
                    .setActionName("To_BasicInformationActivity")
                    .addParam("fromWhere", "pressureHigh")
                    .build()
                    .call();
        } else if ("1".equals(diagnoseInfo.hypertensionTarget)) {
            toSulotion();
        } else if ("0".equals(diagnoseInfo.hypertensionTarget)) {
            if (diagnoseInfo.heart == null) {
                CC.obtainBuilder("app")
                        .setActionName("To_HypertensionTipActivity")
                        .build()
                        .call();
            } else {
                CC.obtainBuilder("app")
                        .setActionName("To_IsEmptyStomachOrNotActivity")
                        .build()
                        .call();
            }

        }
    }

    /**
     * -->解决方案页面
     */

    private void toSulotion() {
        CC.obtainBuilder("app")
                .setActionName("ToTreatmentPlanActivity")
                .build()
                .call();
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
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您尚未满足3天测量标准，请在健康监测中测量三日", false);
    }
    /**
     * 原发弹框点击是
     */
    private void onOriginClickYes() {
        if (diagnoseInfo.primary == null) {
            CC.obtainBuilder("app")
                    .setActionName("To_SlowDiseaseManagementTipActivity")
                    .build()
                    .call();
        } else {
            if (diagnoseInfo.lowPressure == null) {
//                startActivity(new Intent(this, BloodPressureMeasureActivity.class));
                CC.obtainBuilder("health_measure")
                        .setActionName("To_BloodpressureManagerActivity")
                        .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        toSulotion();
                    }
                });
            } else {
                toSulotion();
            }
        }
    }
    public void showLoadingDialog(String message) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new ProgressDialog(this);
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
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
