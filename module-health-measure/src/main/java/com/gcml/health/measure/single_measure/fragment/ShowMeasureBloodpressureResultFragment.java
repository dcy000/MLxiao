package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.single_measure.bean.DiagnoseInfoBean;
import com.gcml.health.measure.single_measure.bean.NewWeeklyOrMonthlyBean;
import com.gcml.lib_widget.dialog.FllowUpTimesDialog;
import com.gcml.lib_widget.progressbar.RoundProgressBar;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.littlejie.circleprogress.WaveProgress;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2018/9/20.
 */

public class ShowMeasureBloodpressureResultFragment extends BluetoothBaseFragment implements View.OnClickListener {
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
    private DiagnoseInfoBean.DataBean diagnoseInfo;

    private String healthState;
    private int healthScore;
    private int currentHighBloodpressure;
    private int currentLowBloodpressure;
    private String currentSuggest;
    private boolean isTask;

    @Override
    protected int initLayout() {
        return R.layout.health_measure_activity_show_measure_bloodpressure_result;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
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


        if (bundle != null) {
            healthState = bundle.getString("health_state");
            healthScore = bundle.getInt("health_score", 0);
            currentHighBloodpressure = bundle.getInt("high_bloodpressure", 120);
            currentLowBloodpressure = bundle.getInt("low_bloodpressure", 80);
            currentSuggest = bundle.getString("suggest");
            isTask = bundle.getBoolean("isTask");
        }
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

        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),
                "主人，您本次测量高压" + currentHighBloodpressure + ",低压"
                        + currentLowBloodpressure + ",健康分数" + healthScore + "分。" + currentSuggest);
        initViewColor();
        getData();
        getDiagnoseInfo();
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

    @SuppressLint("CheckResult")
    private void getData() {

        Calendar curr = Calendar.getInstance();
        long weekAgoTime = curr.getTimeInMillis();
        HealthMeasureRepository.getWeeklyOrMonthlyReport(weekAgoTime, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<NewWeeklyOrMonthlyBean>() {
                    @Override
                    public void onNext(NewWeeklyOrMonthlyBean newWeeklyOrMonthlyBean) {
                        dealData(newWeeklyOrMonthlyBean);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastUtils.showShort("暂无周报告:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_something_advice) {
            CCAppActions.jump2NormalHightActivity("NewMeasureBloodpressureResultActivity");

        } else if (i == R.id.health_knowledge) {
            if (isTask) {
                mActivity.finish();
            } else {
                onclickHypertensionManage();
            }

        } else {
        }
    }


    /**
     * 点击高血压管理 按钮
     */
    private void onclickHypertensionManage() {
//        if (diagnoseInfo != null) {
//            if (diagnoseInfo.result != null) {
//                clickWithoutContinueJudge();
//                return;
//            }
//
//            if (!(diagnoseInfo.risk == null
//                    && diagnoseInfo.primary == null
//                    && diagnoseInfo.lowPressure == null
//                    && diagnoseInfo.hypertensionLevel == null
//                    && diagnoseInfo.hypertensionPrimaryState == null
//                    && diagnoseInfo.heart == null
//                    && diagnoseInfo.hypertensionTarget == null
//            )) {
//
//                ContinueOrNotDialog();
//            }
//        }


        clickWithoutContinueJudge();
    }

    private void ContinueOrNotDialog() {

        new AlertDialog(getContext())
                .builder()
                .setMsg("您之前的流程还未完成，是否要继续？")
                .setNegativeButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickWithoutContinueJudge();
                    }
                })
                .setPositiveButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDiagnoseInfoNew();
                    }
                }).show();

        mlSpeak("您之前的流程还未完成，是否要继续？");
    }

    private void getDiagnoseInfoNew() {
        showLoadingDialog("");
        HealthMeasureApi.getDiagnoseInfoNew(UserSpHelper.getUserId(), new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        DiagnoseInfoBean bean = new Gson().fromJson(response.body(), DiagnoseInfoBean.class);
                        if (bean != null && bean.tag && bean.data != null) {
                            diagnoseInfo = bean.data;
                            clickWithoutContinueJudge();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideLoadingDialog();
                    }
                }
        );
    }

    public void mlSpeak(String text) {
        MLVoiceSynthetize.startSynthesize(getContext().getApplicationContext(), text, false);
    }

    private void clickWithoutContinueJudge() {
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
                new AlertDialog(mContext)
                        .builder()
                        .setMsg("您在7天内已生成过健康方案，点击健康方案可直接查看。")
                        .setPositiveButton("健康方案", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSulotion();
                            }
                        }).show();
            }

        } else {
            ToastUtils.showShort("暂无推荐");
        }
    }

    private void showOriginHypertensionDialog() {
        new AlertDialog(mContext)
                .builder()
                .setMsg("您是否诊断过原发性高血压且正在进行高血压规范治疗？(您的选择将影响您的健康方案，且一旦选择不可更改，请谨慎回答)")
                .setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postOriginPertensionState("1");
                        CC.obtainBuilder("app")
                                .setActionName("To_SlowDiseaseManagementTipActivity")
                                .build()
                                .call();
                    }
                })
                .setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
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
        if (!isAdded()){
            return;
        }
        FllowUpTimesDialog dialog = new FllowUpTimesDialog(notice);
        dialog.setListener(new FllowUpTimesDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm() {
                dialog.dismiss();
            }
        });

        dialog.show(getFragmentManager(), "less3");
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
}
