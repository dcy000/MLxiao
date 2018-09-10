package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.hypertensionmanagement.bean.DiagnoseInfoBean;
import com.example.han.referralproject.hypertensionmanagement.dialog.FllowUpTimesDialog;
import com.example.han.referralproject.hypertensionmanagement.dialog.TwoChoiceDialog;
import com.gcml.common.data.AppManager;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.dialog.DialogSure;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlowDiseaseManagementActivity extends BaseActivity implements TwoChoiceDialog.OnDialogClickListener, FllowUpTimesDialog.OnDialogClickListener {

    @BindView(R.id.iv_Hypertension_manage)
    ImageView ivHypertensionManage;
    @BindView(R.id.iv_blood_sugar_manage)
    ImageView ivBloodSugarManage;

    public static final String CONTENT = "您当前测量次数未满足非同日3次测量,高血压诊断条件不足,再测2日即可为您开启方案";
    private DiagnoseInfoBean.DataBean diagnoseInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slow_disease_management);
        ButterKnife.bind(this);
        initTitle();
        getDiagnoseInfo();
//        OriginHypertensionTipActivity
        AppManager.getAppManager().addActivity(this);
    }

    /**
     * 获取诊断的相关信息
     */
    private void getDiagnoseInfo() {
        showLoadingDialog("");
        NetworkApi.getDiagnoseInfo(LocalShared.getInstance(this).getUserId(), new StringCallback() {
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

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(SlowDiseaseManagementActivity.this, WifiConnectActivity.class)));
        mlSpeak("主人，欢迎来到健康管理。");
    }

    @OnClick({R.id.iv_Hypertension_manage, R.id.iv_blood_sugar_manage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_Hypertension_manage:
                onclickHypertensionManage();
                break;
            case R.id.iv_blood_sugar_manage:
                ToastUtils.showShort("敬请期待");
                break;
        }
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
                    startActivity(new Intent(SlowDiseaseManagementActivity.this, TreatmentPlanActivity.class));
                });
            }

        } else {
            ToastUtils.showShort("网络繁忙");
        }


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
        startActivity(new Intent(SlowDiseaseManagementActivity.this, BasicInformationActivity.class)
                .putExtra("fromWhere", "pressureFlat"));
    }

    private void onNormal() {
        startActivity(new Intent(SlowDiseaseManagementActivity.this, BasicInformationActivity.class)
                .putExtra("fromWhere", "pressureNormal"));

    }

    private void onNormalHigh() {
        if (diagnoseInfo.risk == null) {
            startActivity(new Intent(SlowDiseaseManagementActivity.this, BasicInformationActivity.class)
                    .putExtra("fromWhere", "pressureNormalHigh"));
        } else {
//            toDetete();
//            startActivity(new Intent(this, WeightMeasureActivity.class));

            CC.obtainBuilder("health_measure")
                    .setActionName("To_WeightManagerActivity")
                    .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                @Override
                public void onResult(CC cc, CCResult result) {
                    startActivity(new Intent(SlowDiseaseManagementActivity.this, TreatmentPlanActivity.class));
                }
            });
        }

    }

    private void onHigh() {
        if (diagnoseInfo.hypertensionTarget == null) {
            startActivity(new Intent(SlowDiseaseManagementActivity.this, BasicInformationActivity.class)
                    .putExtra("fromWhere", "pressureHigh"));
        } else if ("1".equals(diagnoseInfo.hypertensionTarget)) {
            startActivity(new Intent(this, TreatmentPlanActivity.class));
        } else if ("0".equals(diagnoseInfo.hypertensionTarget)) {
            if (diagnoseInfo.heart == null) {
                startActivity(new Intent(this, HypertensionTipActivity.class));
            } else {
                startActivity(new Intent(this, IsEmptyStomachOrNotActivity.class));
            }

        }
    }

    private void end() {
        // TODO: 2018/7/28
    }

    /**
     * 原发弹框点击是
     */
    private void onOriginClickYes() {
        if (diagnoseInfo.primary == null) {
            startActivity(new Intent(SlowDiseaseManagementActivity.this, SlowDiseaseManagementTipActivity.class));
        } else {
            if (diagnoseInfo.lowPressure == null) {
//                startActivity(new Intent(this, BloodPressureMeasureActivity.class));
                CC.obtainBuilder("health_measure")
                        .setActionName("To_BloodpressureManagerActivity")
                        .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        startActivity(new Intent(SlowDiseaseManagementActivity.this, TreatmentPlanActivity.class));
                    }
                });
            } else {
                startActivity(new Intent(this, TreatmentPlanActivity.class));
            }
        }
    }

    /**
     * -->解决方案页面
     */

    private void toSulotion() {
        startActivity(new Intent(this, TreatmentPlanActivity.class));
    }

    private void getDatimeInfo() {
        showLoadingDialog("");
        NetworkApi.getDiagnoseInfo(LocalShared.getInstance(this).getUserId(), new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        DiagnoseInfoBean bean = new Gson().fromJson(response.body(), DiagnoseInfoBean.class);
                        if (bean != null && bean.tag && bean.data != null) {

                            if (bean.data.detectionDayCount >= 3) {
                                String hypertensionLevel = bean.data.hypertensionLevel;
                                jumpPages(hypertensionLevel);
                            } else {
                                showLessThan3Dialog("0");
                            }
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

    private void showLessThan3Dialog(String notice) {
        FllowUpTimesDialog dialog = new FllowUpTimesDialog(notice);
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "less3");
        mlSpeak("主人，您尚未满足3天测量标准，请在健康监测中测量三日");
    }

    private void showOriginHypertensionDialog() {
        TwoChoiceDialog dialog = new TwoChoiceDialog("您是否诊断过原发性高血压且正在进行高血压规范治疗？(您的选择将影响您的健康方案，且一旦选择不可更改，请谨慎回答)", "是", "否");
        dialog.setListener(this);
        dialog.show(getFragmentManager(), "yuanfa");
        mlSpeak("主人，您是否已确诊高血压且在治疗？");
    }

    @Override
    public void onClickConfirm(String content) {
        stopSpeaking();
        postOriginPertensionState("1");
        startActivity(new Intent(this, SlowDiseaseManagementTipActivity.class));
    }

    @Override
    public void onClickCancel() {
        postOriginPertensionState("0");
    }

    private void postOriginPertensionState(String state) {
        NetworkApi.postOriginHypertension(state, LocalShared.getInstance(this).getUserId(), new StringCallback() {
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

    private void jumpPages(String hypertensionLevel) {
        switch (hypertensionLevel) {

            case "00"://正常
//                startActivityForResult(new Intent(SlowDiseaseManagementActivity.this, PressureNornalTipActivity.class));
                startActivity(new Intent(SlowDiseaseManagementActivity.this, BasicInformationActivity.class)
                        .putExtra("fromWhere", "pressureNormal"));
                break;
            case "01"://偏低
//                startActivityForResult(new Intent(SlowDiseaseManagementActivity.this, PressureTipActivity.class));
                startActivity(new Intent(SlowDiseaseManagementActivity.this, BasicInformationActivity.class)
                        .putExtra("fromWhere", "pressureFlat"));
                break;
            case "02"://正常高值
//                startActivityForResult(new Intent(SlowDiseaseManagementActivity.this, NormalHighTipActivity.class));
                startActivity(new Intent(SlowDiseaseManagementActivity.this, BasicInformationActivity.class)
                        .putExtra("fromWhere", "pressureNormalHigh"));
                break;
            case "11:1"://高血压
            case "12:1":
            case "13:1":
            case "22:2":
            case "23:2":
            case "33:3":
            case "34:3":
//                startActivityForResult(new Intent(SlowDiseaseManagementActivity.this, HypertensionTipActivity.class));
                startActivity(new Intent(SlowDiseaseManagementActivity.this, BasicInformationActivity.class)
                        .putExtra("fromWhere", "pressureHigh"));
                break;
        }
    }


    @Override
    public void onClickConfirm() {

    }
}
