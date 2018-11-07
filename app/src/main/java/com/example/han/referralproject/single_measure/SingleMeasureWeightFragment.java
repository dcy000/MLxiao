package com.example.han.referralproject.single_measure;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.single_measure.bean.DetectionData;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 11:09
 * created by:gzq
 * description:单次体重测量
 */
public class SingleMeasureWeightFragment extends Weight_Fragment {

    private boolean isMeasureTask = false;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        if (bundle != null) {
            isMeasureTask = bundle.getBoolean(IPresenter.IS_MEASURE_TASK);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            //得到身高和体重，再计算一下体质
            if (mTvTizhi != null) {
                String height = LocalShared.getInstance(mContext).getUserHeight();
                if (!TextUtils.isEmpty(height)) {
                    float parseFloat = Float.parseFloat(height);
                    float weight = Float.parseFloat(results[0]);
                    if (mTvTizhi != null) {
                        mTvTizhi.setText(String.format("%.2f", weight / (parseFloat * parseFloat / 10000)));
                    }
                }
            }
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量体重" + results[0] + "公斤", false);
            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("3");
            data.setWeight(Float.parseFloat(results[0]));
            datas.add(data);
            //TODO======================
//            HealthMeasureRepository.postMeasureData(datas)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
//                    .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
//                        @Override
//                        public void onNext(List<DetectionResult> o) {
//                            ToastUtils.showLong("数据上传成功");
//                            if (isMeasureTask && !mActivity.isFinishing()) {
//                                mActivity.finish();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            ToastUtils.showLong("数据上传失败:" + e.getMessage());
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
