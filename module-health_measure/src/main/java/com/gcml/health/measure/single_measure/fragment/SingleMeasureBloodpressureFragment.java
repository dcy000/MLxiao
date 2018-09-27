package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.dialog.SingleDialog;
import com.gcml.health.measure.bloodpressure_habit.GetHypertensionHandActivity;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.measure_abnormal.HealthMeasureAbnormalActivity;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.single_measure.ShowMeasureBloodpressureResultActivity;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 9:54
 * created by:gzq
 * description:单次血压测量
 */
public class SingleMeasureBloodpressureFragment extends Bloodpressure_Fragment {
    private static final int CODE_REQUEST_ABNORMAL = 10001;
    private static final int CODE_REQUEST_GETHYPERTENSIONHAND = 10002;
    private ArrayList<DetectionData> datas;
    private int highPressure;
    private int lowPressure;
    private boolean isMeasureTask = false;
    private boolean hasHypertensionHand=false;
    private boolean isOnPause=false;
    public SingleMeasureBloodpressureFragment() {
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        isMeasureTask = bundle.getBoolean(IPresenter.IS_MEASURE_TASK);
        getHypertensionHand();
    }

    /**
     * 获取惯用手
     */
    private void getHypertensionHand() {
        String userHypertensionHand = UserSpHelper.getUserHypertensionHand();
        Timber.i("SingleMeasureBloodpressureFragment惯用手："+userHypertensionHand);
        if (TextUtils.isEmpty(userHypertensionHand)) {
            //还没有录入惯用手，则跳转到惯用手录入activity
            GetHypertensionHandActivity.startActivityForResult(this, CODE_REQUEST_GETHYPERTENSIONHAND);
        } else {
            hasHypertensionHand=true;
            if ("0".equals(userHypertensionHand)) {
                showHypertensionHandDialog("左手");
            } else if ("1".equals(userHypertensionHand)) {
                showHypertensionHandDialog("右手");
            }
        }
    }

    private void showHypertensionHandDialog(String hand) {
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，请使用" + hand + "测量");
        new SingleDialog(mContext)
                .builder()
                .setMsg("请使用" + hand + "测量")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3&&!isOnPause) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量高压" + results[0] + ",低压" + results[1] + ",脉搏" + results[2], false);

            datas = new ArrayList<>();
            DetectionData pressureData = new DetectionData();
            DetectionData dataPulse = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            pressureData.setDetectionType("0");
            highPressure = Integer.parseInt(results[0]);
            pressureData.setHighPressure(highPressure);
            lowPressure = Integer.parseInt(results[1]);
            pressureData.setLowPressure(lowPressure);
            dataPulse.setDetectionType("9");
            dataPulse.setPulse(Integer.parseInt(results[2]));
            datas.add(pressureData);
            datas.add(dataPulse);

            HealthMeasureRepository.checkIsNormalData(UserSpHelper.getUserId(), datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<Object>() {
                        @Override
                        public void onNext(Object o) {
                            uploadData();
                        }

                        @Override
                        public void onError(Throwable e) {
                            HealthMeasureAbnormalActivity.startActivity(
                                    SingleMeasureBloodpressureFragment.this,
                                    IPresenter.MEASURE_BLOOD_PRESSURE, CODE_REQUEST_ABNORMAL);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }

    @SuppressLint("CheckResult")
    private void uploadData() {
        if (datas == null) {
            Timber.e("SingleMeasureBloodpressureFragment：数据被回收，程序异常");
            return;
        }
        LoadingDialog dialog = new LoadingDialog.Builder(mContext)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> o) {

                        Timber.e("单测返回来的数据：" + o);
                        ToastUtils.showLong("上传数据成功");
                        DetectionResult result = o.get(0);
                        if (isMeasureTask) {
                            ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result.getDiagnose(),
                                    result.getScore(), highPressure, lowPressure, result.getResult(), true, datas);
                            mActivity.finish();
                        } else {
                            ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result.getDiagnose(),
                                    result.getScore(), highPressure, lowPressure, result.getResult(), datas);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("上传数据失败:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        isOnPause=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause=true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.e("SingleMeasureBloodpressureFragment:" + requestCode + "++" + resultCode);
        if (requestCode == CODE_REQUEST_ABNORMAL) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    boolean booleanExtra = data.getBooleanExtra(HealthMeasureAbnormalActivity.KEY_HAS_ABNIRMAL_REASULT, false);
                    if (booleanExtra) {
                        //数据异常
                        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，因为你测量出现偏差，此次测量将不会作为历史数据");
                    } else {
                        uploadData();
                    }

                }
            }
        } else if (requestCode == CODE_REQUEST_GETHYPERTENSIONHAND) {
            if (resultCode == RESULT_OK) {
                mActivity.finish();
            } else {
                getHypertensionHand();
                dealLogic();
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
