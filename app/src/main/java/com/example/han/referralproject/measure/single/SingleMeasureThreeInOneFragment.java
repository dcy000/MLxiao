package com.example.han.referralproject.measure.single;

import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 11:13
 * created by:gzq
 * description:TODO
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
            }
            if (results[0].equals("cholesterol")) {
                cholesterolData = new DetectionData();
                cholesterolData.setDetectionType("7");
                cholesterolData.setCholesterol(Float.parseFloat(results[1]));
            }

            if (results[0].equals("bua")) {
                lithicAcidData = new DetectionData();
                lithicAcidData.setDetectionType("8");
                lithicAcidData.setUricAcid(Float.parseFloat(results[1]));
            }
            if (sugarData != null && cholesterolData != null && lithicAcidData != null) {
                MLVoiceSynthetize.startSynthesize(getContext(), "主人，您本次测量血糖"
                        + sugarData.getBloodSugar() + ",尿酸" + lithicAcidData.getUricAcid() + ",胆固醇"
                        + cholesterolData.getCholesterol(), false);
                datas.add(sugarData);
                datas.add(cholesterolData);
                datas.add(lithicAcidData);

                NetworkApi.postMeasureData(datas, new NetworkCallback() {
                    @Override
                    public void onSuccess(String callbackString) {
                        ToastUtils.showLong("数据上传成功");
                    }

                    @Override
                    public void onError() {
                        ToastUtils.showLong("数据上传失败");
                    }
                });
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MLVoiceSynthetize.stop();
    }
}
