package com.example.han.referralproject.single_measure;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.single_measure.bean.DetectionData;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.utils.DataUtils;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 10:54
 * created by:gzq
 * description:单次血糖测量
 */
public class SingleMeasureBloodsugarFragment extends Bloodsugar_Fragment {
    private Bundle bundle;
    private ArrayList<DetectionData> datas;
    private static final int CODE_REQUEST_ABNORMAL = 10002;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        this.bundle = bundle;
        if (bundle != null) {
            if (bundle.getBoolean("isOnlyShowBtnHealthRecord")) {
                mBtnVideoDemo.setVisibility(View.GONE);
                mBtnHealthHistory.setText("下一步");
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            String roundUp = DataUtils.getRoundUp(results[0], 1);
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + roundUp, false);
//
//            datas = new ArrayList<>();
//            DetectionData data = new DetectionData();
//            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
//            data.setDetectionType("1");
//            if (bundle != null) {
//                data.setSugarTime(bundle.getInt("selectMeasureSugarTime"));
//            } else {
//                data.setSugarTime(0);
//            }
//            data.setBloodSugar(Float.parseFloat(roundUp));
//            datas.add(data);
//            uploadData();
            DataInfoBean info = new DataInfoBean();
            info.blood_sugar = roundUp;
            info.upload_state = true;
            if (bundle != null) {
                info.sugar_time = bundle.getInt("selectMeasureSugarTime") + "";
            } else {
                info.sugar_time = 0 + "";
            }
            NetworkApi.postData(info,
                    response -> T.show("数据上传成功"),
                    message -> T.show("数据上传失败"));


        }
    }

    @SuppressLint("CheckResult")
    private void uploadData() {
//        if (datas == null) {
//            Timber.e("SingleMeasureBloodpressureFragment：数据被回收，程序异常");
//            return;
//        }
//
//        HealthMeasureRepository.postMeasureData(datas)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
//                .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
//                    @Override
//                    public void onNext(List<DetectionResult> o) {
//                        ToastUtils.showShort("数据上传成功");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.showShort("数据上传失败:" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
