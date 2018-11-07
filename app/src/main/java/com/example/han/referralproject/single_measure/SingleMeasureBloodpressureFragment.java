package com.example.han.referralproject.single_measure;

import android.annotation.SuppressLint;

import com.example.han.referralproject.single_measure.bean.DetectionData;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

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
    private boolean isOnPause = false;

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3 && !isOnPause) {
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

//            HealthMeasureRepository.checkIsNormalData(UserSpHelper.getUserId(), datas)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
//                    .subscribeWith(new DefaultObserver<Object>() {
//                        @Override
//                        public void onNext(Object o) {
//                            uploadData();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            HealthMeasureAbnormalActivity.startActivity(
//                                    SingleMeasureBloodpressureFragment.this,
//                                    IPresenter.MEASURE_BLOOD_PRESSURE, CODE_REQUEST_ABNORMAL);
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
            uploadData();

        }
    }

    @SuppressLint("CheckResult")
    private void uploadData() {
        //TODO:=================
//        if (datas == null) {
//            Timber.e("SingleMeasureBloodpressureFragment：数据被回收，程序异常");
//            return;
//        }
//        LoadingDialog dialog = new LoadingDialog.Builder(mContext)
//                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
//                .setTipWord("正在加载")
//                .create();
//        HealthMeasureRepository.postMeasureData(datas)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        dialog.show();
//                    }
//                })
//                .doOnTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        dialog.dismiss();
//                    }
//                })
//                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
//                .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
//                    @Override
//                    public void onNext(List<DetectionResult> o) {
//
//                        Timber.e("单测返回来的数据：" + o);
//                        ToastUtils.showLong("上传数据成功");
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "onError: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

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
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
