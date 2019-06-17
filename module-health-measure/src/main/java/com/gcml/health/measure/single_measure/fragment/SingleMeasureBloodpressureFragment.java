package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gcml.common.data.PostDataCallBackBean;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.ChannelUtils;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.measure_abnormal.HealthMeasureAbnormalActivity;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.single_measure.ShowMeasureBloodpressureResultActivity;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.bloodpressure.BloodpressureFragment;
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
public class SingleMeasureBloodpressureFragment extends BloodpressureFragment {
    private static final int CODE_REQUEST_ABNORMAL = 10001;
    private static final int CODE_REQUEST_GETHYPERTENSIONHAND = 10002;
    private ArrayList<DetectionData> datas;
    private int highPressure;
    private int lowPressure;
    private boolean isMeasureTask = false;
    private boolean hasHypertensionHand = false;
    private boolean isOnPause = false;
    private DetectionData results;

    public SingleMeasureBloodpressureFragment() {
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        isMeasureTask = bundle.getBoolean(IBleConstants.IS_MEASURE_TASK);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(DetectionData detectionData) {
        this.results = detectionData;
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量高压" + detectionData.getHighPressure() + ",低压" + detectionData.getLowPressure() + ",脉搏" + detectionData.getPulse(), false);
        datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData dataPulse = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        pressureData.setDetectionType("0");
        highPressure = detectionData.getHighPressure();
        pressureData.setHighPressure(highPressure);
        lowPressure = detectionData.getLowPressure();
        pressureData.setLowPressure(lowPressure);
        dataPulse.setDetectionType("9");
        dataPulse.setPulse(detectionData.getPulse());
        datas.add(pressureData);
        datas.add(dataPulse);

        if (UserSpHelper.isNoNetwork()) {
            uploadData();
            return;
        }

        HealthMeasureRepository.checkIsNormalData(UserSpHelper.getUserId(), datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        uploadData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        HealthMeasureAbnormalActivity.startActivity(
                                SingleMeasureBloodpressureFragment.this,
                                IBleConstants.MEASURE_BLOOD_PRESSURE, CODE_REQUEST_ABNORMAL);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
        if (ChannelUtils.isXiongAn()) {
            HealthMeasureRepository.postMeasureDataWithXiongan(datas)
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
                    .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                    .subscribeWith(new DefaultObserver<List<PostDataCallBackBean>>() {
                        @Override
                        public void onNext(List<PostDataCallBackBean> o) {
                            if (UserSpHelper.isNoNetwork()) {
                                return;
                            }
                            ToastUtils.showLong("上传数据成功");
                            PostDataCallBackBean postDataCallBackBean = o.get(0);
                            if (postDataCallBackBean == null) return;
                            PostDataCallBackBean.Result1Bean result1 = postDataCallBackBean.getResult1();
                            if (result1 == null) return;
//                            if (isMeasureTask) {
//                                ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result1.getDiagnose(),
//                                        result1.getScore(), highPressure, lowPressure, result1.getResult(), true, datas);
//                                mActivity.finish();
//                            } else {
//                                ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result1.getDiagnose(),
//                                        result1.getScore(), highPressure, lowPressure, result1.getResult(), datas);
//                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            showUploadDataFailedDialog(results, R.string.xml_dialog_upload_failed_single);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
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
                    .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                    .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                        @Override
                        public void onNext(List<DetectionResult> o) {
                            if (UserSpHelper.isNoNetwork()) {
                                return;
                            }
                            Timber.e("单测返回来的数据：" + o);
                            ToastUtils.showLong("上传数据成功");
                            DetectionResult result = o.get(0);
//                            if (isMeasureTask) {
//                                ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result.getDiagnose(),
//                                        result.getScore(), highPressure, lowPressure, result.getResult(), true, datas);
//                                mActivity.finish();
//                            } else {
//                                ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result.getDiagnose(),
//                                        result.getScore(), highPressure, lowPressure, result.getResult(), datas);
//                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            showUploadDataFailedDialog(results, R.string.xml_dialog_upload_failed_single);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    private static final String TAG = "SingleMeasureBloodpress";

    @Override
    public void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause = true;
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
                        MLVoiceSynthetize.startSynthesize(UM.getApp(), "因为你测量出现偏差，此次测量将不会作为历史数据");
                    } else {
                        uploadData();
                    }

                }
            }
        } else if (requestCode == CODE_REQUEST_GETHYPERTENSIONHAND) {
            if (resultCode == RESULT_OK) {
                mActivity.finish();
            } else {
//                getHypertensionHand();
//                autoConnect();
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
