package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.R;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.bloodoxygen.BloodOxygenFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 10:41
 * created by:gzq
 * description:单次血氧测量
 */
public class SingleMeasureBloodoxygenFragment extends BloodOxygenFragment {
    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(DetectionData detectionData) {
        MLVoiceSynthetize.startSynthesize(UM.getApp(),
                "您本次测量血氧" + detectionData.getBloodOxygen() + "%", false);


        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData dataPulse = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        pressureData.setDetectionType("6");
        pressureData.setBloodOxygen(detectionData.getBloodOxygen());
        dataPulse.setDetectionType("9");
        dataPulse.setPulse(detectionData.getPulse());
        datas.add(pressureData);
        datas.add(dataPulse);

        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("上传数据成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showUploadDataFailedDialog(detectionData, R.string.xml_dialog_upload_failed_single);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
