package com.example.han.referralproject.measure.single;


import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.MeasureResult;
import com.example.han.referralproject.health.model.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 10:41
 * created by:gzq
 * description:单次血氧测量
 */
public class SingleMeasureBloodoxygenFragment extends Bloodoxygen_Fragment {
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 2) {
            MLVoiceSynthetize.startSynthesize(getContext(), "主人，您本次测量血氧" + results[0] + "%,脉搏" + results[1], false);
            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData pressureData = new DetectionData();
            DetectionData dataPulse = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            pressureData.setDetectionType("6");
            pressureData.setBloodOxygen(Float.parseFloat(results[0]));
            dataPulse.setDetectionType("9");
            dataPulse.setPulse(Integer.parseInt(results[1]));
            datas.add(pressureData);
            datas.add(dataPulse);

            DataInfoBean info = new DataInfoBean();
            info.blood_oxygen = String.valueOf(pressureData.getBloodOxygen());
            info.pulse = dataPulse.getPulse();
            NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                @Override
                public void onSuccess(MeasureResult response) {
                    //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                }
            }, new NetworkManager.FailedCallback() {
                @Override
                public void onFailed(String message) {

                }
            });

//            NetworkApi.postMeasureData(datas, new NetworkCallback() {
//                @Override
//                public void onSuccess(String callbackString) {
//                    ToastUtils.showShort("上传数据成功");
//                }
//
//                @Override
//                public void onError() {
//                    ToastUtils.showShort("上传数据失败");
//                }
//            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }
}
