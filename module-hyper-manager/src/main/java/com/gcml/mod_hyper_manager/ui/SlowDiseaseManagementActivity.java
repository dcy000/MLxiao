package com.gcml.mod_hyper_manager.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.mod_hyper_manager.R;
import com.gcml.mod_hyper_manager.bean.DiagnoseInfoBean;
import com.gcml.mod_hyper_manager.dialog.FllowUpTimesDialog;
import com.gcml.mod_hyper_manager.dialog.TwoChoiceDialog;
import com.gcml.mod_hyper_manager.net.HyperRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/hypertension/slow/disease/management")
public class SlowDiseaseManagementActivity extends ToolbarBaseActivity implements TwoChoiceDialog.OnDialogClickListener, FllowUpTimesDialog.OnDialogClickListener {

    ImageView ivHypertensionManage;
    ImageView ivBloodSugarManage;

    private DiagnoseInfoBean.DataBean diagnoseInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slow_disease_management);
        ivHypertensionManage = findViewById(R.id.iv_Hypertension_manage);
        ivHypertensionManage.setOnClickListener(this);
        ivBloodSugarManage = findViewById(R.id.iv_blood_sugar_manage);
        ivBloodSugarManage.setOnClickListener(this);
        initTitle();
        getDiagnoseInfo();
//        OriginHypertensionTipActivity
        AppManager.getAppManager().addActivity(this);
    }

    /**
     * 获取诊断的相关信息
     */
    private void getDiagnoseInfo() {
        showLoading("");
        HyperRepository repository = new HyperRepository();
        repository.getDiagnoseInfo(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<DiagnoseInfoBean.DataBean>() {
                    @Override
                    public void onNext(DiagnoseInfoBean.DataBean dataBean) {
                        diagnoseInfo = dataBean;
                        onclickHypertensionManage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }


    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 方 案");
        mRightText.setVisibility(View.GONE);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "欢迎来到健康方案");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_Hypertension_manage) {
            onclickHypertensionManage();

        } else if (i == R.id.iv_blood_sugar_manage) {
            ToastUtils.showShort("敬请期待");

        }
    }


    /**
     * 点击高血压管理 按钮
     */
    private void onclickHypertensionManage() {
        if (diagnoseInfo != null) {
            if (diagnoseInfo.result != null) {
                clickWithoutContinueJudge();
                return;
            }

            if (diagnoseInfo.primary != null
                    && !(diagnoseInfo.risk == null
                    && diagnoseInfo.lowPressure == null
                    && diagnoseInfo.hypertensionLevel == null
                    && (diagnoseInfo.hypertensionPrimaryState == null || diagnoseInfo.hypertensionPrimaryState.equals("0"))
                    && diagnoseInfo.heart == null
                    && diagnoseInfo.hypertensionTarget == null
            )) {
                getDiagnoseInfoNew();
//                ContinueOrNotDialog();
            } else {
                clickWithoutContinueJudge();
            }
        }

//        clickWithoutJudge();
    }

    /**
     * 不加是否继续重做的逻辑
     */
    private void clickWithoutContinueJudge() {
        if (diagnoseInfo != null) {
            if (diagnoseInfo.result == null) {
                if (diagnoseInfo.hypertensionPrimaryState == null) {
                    //用户更新原发性信息
                    showOriginHypertensionDialog();
                } else if ("1".equals(diagnoseInfo.hypertensionPrimaryState)) {
                    onOriginClickYes();
                } else if ("0".equals(diagnoseInfo.hypertensionPrimaryState)) {
//                    onOriginClickNo();
                    showOriginHypertensionDialog();
                }
            } else {

                new AlertDialog(this)
                        .builder()
                        .setCancelable(false)
                        .setMsg("根据之前评估结果，您的方案已生成。")
                        .setNegativeButton("重新测量", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Routerfit.register(AppRouter.class).skipAllMeasureActivity(22);
                            }
                        })
                        .setPositiveButton("健康方案", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Routerfit.register(AppRouter.class).skipTreatmentPlanActivity();
                            }
                        }).show();
            }

        } else {
            ToastUtils.showShort("暂无推荐");
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
        Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureFlat");
    }

    private void onNormal() {
        Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureNormal");
    }

    private void onNormalHigh() {
        if (diagnoseInfo.risk == null) {
            Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureNormalHigh");
        } else {
//            toDetete();
//            startActivity(new Intent(this, WeightMeasureActivity.class));

//            CC.obtainBuilder("health_measure")
//                    .setActionName("To_WeightManagerActivity")
//                    .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//                @Override
//                public void onResult(CC cc, CCResult result) {
//                    startActivity(new Intent(SlowDiseaseManagementActivity.this, TreatmentPlanActivity.class));
//                }
//            });
            Routerfit.register(AppRouter.class).skipDetecteTipActivity("3");
        }

    }

    private void onHigh() {
        if (diagnoseInfo.hypertensionTarget == null) {
            Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureHigh");
        } else if ("1".equals(diagnoseInfo.hypertensionTarget)) {
            Routerfit.register(AppRouter.class).skipTreatmentPlanActivity();
        } else if ("0".equals(diagnoseInfo.hypertensionTarget)) {
            if (diagnoseInfo.heart == null) {
                Routerfit.register(AppRouter.class).skipHypertensionTipActivity();
            } else {
                Routerfit.register(AppRouter.class).skipIsEmptyStomachOrNotActivity();
            }

        }
    }

    /**
     * 原发弹框点击是
     */
    private void onOriginClickYes() {
        if (diagnoseInfo.primary == null) {
            Routerfit.register(AppRouter.class).skipSlowDiseaseManagementTipActivity();
        } else {
            if (diagnoseInfo.lowPressure == null) {
//                startActivity(new Intent(this, BloodPressureMeasureActivity.class));
//                CC.obtainBuilder("health_measure")
//                        .setActionName("To_BloodpressureManagerActivity")
//                        .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//                    @Override
//                    public void onResult(CC cc, CCResult result) {
//                        startActivity(new Intent(SlowDiseaseManagementActivity.this, TreatmentPlanActivity.class));
//                    }
//                });
                Routerfit.register(AppRouter.class).skipDetecteTipActivity("0");

            } else {
                toSulotion();
            }
        }
    }

    /**
     * -->解决方案页面
     */

    private void toSulotion() {
        Routerfit.register(AppRouter.class).skipTreatmentPlanActivity();
    }

    private void getDiagnoseInfoNew() {
        showLoading("");
        new HyperRepository()
                .getDiagnoseInfoNew(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<DiagnoseInfoBean.DataBean>() {
                    @Override
                    public void onNext(DiagnoseInfoBean.DataBean dataBean) {
                        diagnoseInfo = dataBean;
                        clickWithoutContinueJudge();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    private void showLessThan3Dialog(String notice) {
        FllowUpTimesDialog dialog = new FllowUpTimesDialog(notice);
        dialog.setCancelable(false);
        dialog.setListener(this);

        //此处会因为在页面不可见后出现 （此时大多数时候是内存泄漏了）
        // java.lang.IllegalStateException
        // Can not perform this action after onSaveInstanceState
//        dialog.show(getSupportFragmentManager(), "less3");
        getSupportFragmentManager().beginTransaction().add(dialog, "less3").commitAllowingStateLoss();
    }

    private void showOriginHypertensionDialog() {
        TwoChoiceDialog dialog = new TwoChoiceDialog("您是否诊断过原发性高血压，且正在进行高血压规范治疗？", "是", "否");
        dialog.setListener(this);
//        dialog.show(getFragmentManager(), "yuanfa");
        getFragmentManager().beginTransaction().add(dialog, "yuanfa").commitAllowingStateLoss();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您是否已确诊高血压且在治疗？");
    }

    // TODO: 2018/9/19
    private void ContinueOrNotDialog() {
        TwoChoiceDialog dialog = new TwoChoiceDialog("您之前的流程还未完成，是否要继续？", "是", "否");
        dialog.setListener(new TwoChoiceDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm(String content) {
                clickWithoutContinueJudge();
            }

            @Override
            public void onClickCancel() {
                getDiagnoseInfoNew();
            }
        });
//        dialog.show(getFragmentManager(), "yuanfa");
        getFragmentManager().beginTransaction().add(dialog, "yuanfa").commitAllowingStateLoss();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您之前的流程还未完成，是否要继续？");
    }

    @Override
    public void onClickConfirm(String content) {
        MLVoiceSynthetize.stop();
        postOriginPertensionState("1");
//        startActivity(new Intent(this, SlowDiseaseManagementTipActivity.class));
    }

    @Override
    public void onClickCancel() {
        postOriginPertensionState("0");
    }

    private void postOriginPertensionState(String state) {
        new HyperRepository()
                .postOriginHypertension(UserSpHelper.getUserId(), state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        if ("0".equals(state)) {
                            onOriginClickNo();
                        } else if ("1".equals(state)) {
                            onOriginClickYes();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void jumpPages(String hypertensionLevel) {
        switch (hypertensionLevel) {

            case "00"://正常
//                startActivityForResult(new Intent(SlowDiseaseManagementActivity.this, PressureNornalTipActivity.class));
                Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureNormal");
                break;
            case "01"://偏低
//                startActivityForResult(new Intent(SlowDiseaseManagementActivity.this, PressureTipActivity.class));
                Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureFlat");
                break;
            case "02"://正常高值
//                startActivityForResult(new Intent(SlowDiseaseManagementActivity.this, NormalHighTipActivity.class));
                Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureNormalHigh");
                break;
            case "11:1"://高血压
            case "12:1":
            case "13:1":
            case "22:2":
            case "23:2":
            case "33:3":
            case "34:3":
//                startActivityForResult(new Intent(SlowDiseaseManagementActivity.this, HypertensionTipActivity.class));
                Routerfit.register(AppRouter.class).skipBasicInformationActivity("pressureHigh");
                break;
        }
    }


    @Override
    public void onClickConfirm() {
        //-->人脸-->测量血压
//        Routerfit.register(AppRouter.class)
//                .getFaceProvider()
//                .getFaceId(UserSpHelper.getUserId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new io.reactivex.observers.DefaultObserver<String>() {
//                    @Override
//                    public void onNext(String faceId) {
//                        Routerfit.register(AppRouter.class).skipFaceBdSignInActivity(true, true, faceId, true, new ActivityCallback() {
//                            @Override
//                            public void onActivityResult(int result, Object data) {
//                                if (result == Activity.RESULT_OK) {
//                                    String sResult = data.toString();
//                                    if (TextUtils.isEmpty(sResult))
//                                        return;
//                                    if (sResult.equals("success") || sResult.equals("skip")) {
//
//                                    } else if (sResult.equals("failed")) {
//                                        ToastUtils.showShort("人脸验证失败");
//                                    }
//
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.showShort("请先注册人脸！");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
        toBloodPressure();
    }

    private void toBloodPressure() {
        Routerfit.register(AppRouter.class).skipBloodpressureManagerActivity("SlowDiseaseManagementActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
