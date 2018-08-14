package com.example.han.referralproject.measure.single;

import android.text.TextUtils;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.MeasureResult;
import com.example.han.referralproject.health.model.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 11:13
 * created by:gzq
 * description:单次单合一测量
 */
public class SingleMeasureThreeInOneFragment extends ThreeInOne_Fragment {
    ArrayList<DetectionData> datas = new ArrayList<>();
    DetectionData sugarData;
    DetectionData cholesterolData;
    DetectionData lithicAcidData;

    @Override
    protected void onMeasureFinished(String... results) {
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        if (results.length == 2) {
            if (results[0].equals("bloodsugar")) {
                sugarData = new DetectionData();
                sugarData.setDetectionType("1");
                sugarData.setSugarTime(0);
                sugarData.setBloodSugar(Float.parseFloat(results[1]));
                DataInfoBean info = new DataInfoBean();
                info.sugar_time = String.valueOf(sugarData.getSugarTime());
                info.blood_sugar = String.valueOf(sugarData.getBloodSugar());
                MLVoiceSynthetize.startSynthesize(getContext(), "主人，您本次测量血糖"
                        + sugarData.getBloodSugar(), false);
                NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                    @Override
                    public void onSuccess(MeasureResult response) {
                        //Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        if (!TextUtils.isEmpty(message) && message.startsWith("血糖超标")) {
                            MLVoiceSynthetize.startSynthesize(getContext(), "主人，您本次测量血糖超标", false);
                            T.show("血糖超标");
                        }
                    }
                });
            }
            if (results[0].equals("cholesterol")) {
                cholesterolData = new DetectionData();
                cholesterolData.setDetectionType("7");
                cholesterolData.setCholesterol(Float.parseFloat(results[1]));
                DataInfoBean info = new DataInfoBean();
                info.cholesterol = String.valueOf(cholesterolData.getCholesterol());
                MLVoiceSynthetize.startSynthesize(getContext(), "主人，您本次测量胆固醇"
                        + cholesterolData.getCholesterol(), false);
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
            }

            if (results[0].equals("bua")) {
                lithicAcidData = new DetectionData();
                lithicAcidData.setDetectionType("8");
                lithicAcidData.setUricAcid(Float.parseFloat(results[1]));
                DataInfoBean info = new DataInfoBean();
                info.uric_acid = String.valueOf(lithicAcidData.getUricAcid() * 1000);
                MLVoiceSynthetize.startSynthesize(getContext(), "主人，您本次测量尿酸" + lithicAcidData.getUricAcid(), false);
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
            }
//            if (sugarData != null && cholesterolData != null && lithicAcidData != null) {
//                MLVoiceSynthetize.startSynthesize(getContext(), "主人，您本次测量血糖"
//                        + sugarData.getBloodSugar() + ",尿酸" + lithicAcidData.getUricAcid() + ",胆固醇"
//                        + cholesterolData.getCholesterol(), false);
//                datas.add(sugarData);
//                datas.add(cholesterolData);
//                datas.add(lithicAcidData);
//            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MLVoiceSynthetize.stop();
    }
}
