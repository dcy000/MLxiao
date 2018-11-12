package com.example.han.referralproject.single_measure;

import android.annotation.SuppressLint;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 10:41
 * created by:gzq
 * description:单次血氧测量
 */
public class SingleMeasureBloodoxygenFragment extends Bloodoxygen_Fragment {
    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 2) {
            MLVoiceSynthetize.startSynthesize(MyApplication.getInstance(),
                    "主人，您本次测量血氧" + results[0] + "%", false);


           /* ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData pressureData = new DetectionData();
            DetectionData dataPulse = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            pressureData.setDetectionType("6");
            pressureData.setBloodOxygen(Float.parseFloat(results[0]));
            dataPulse.setDetectionType("9");
            dataPulse.setPulse(Integer.parseInt(results[1]));
            datas.add(pressureData);
            datas.add(dataPulse);
            HealthMeasureRepository.postMeasureData(datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<Object>() {
                        @Override
                        public void onNext(Object o) {
                            ToastUtils.showShort("上传数据成功");
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showShort("上传数据失败:" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
*/
            DataInfoBean info = new DataInfoBean();
            info.blood_oxygen = results[0];
            info.pulse = Integer.parseInt(results[1]);

            NetworkApi.postData(info, response -> {
                T.show("数据上传成功");
            }, message -> {
                T.show("数据上传失败");
            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
