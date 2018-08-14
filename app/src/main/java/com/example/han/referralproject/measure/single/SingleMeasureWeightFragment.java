package com.example.han.referralproject.measure.single;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.MeasureResult;
import com.example.han.referralproject.health.model.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
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
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            MLVoiceSynthetize.startSynthesize(getContext(), "主人，您本次测量体重" + results[0] + "公斤", false);
            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("3");
            data.setWeight(Float.parseFloat(results[0]));
            datas.add(data);

            DataInfoBean info = new DataInfoBean();
            info.weight = data.getWeight();
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
//                    ToastUtils.showLong("数据上传成功");
//                }
//
//                @Override
//                public void onError() {
//                    ToastUtils.showLong("数据上传失败");
//                }
//            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MLVoiceSynthetize.stop();
    }
}
