package com.gcml.health.measure.single_measure.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

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
    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        this.bundle = bundle;
        if (bundle!=null){
            if (bundle.getBoolean("isOnlyShowBtnHealthRecord")) {
                mBtnVideoDemo.setVisibility(View.GONE);
                mBtnHealthHistory.setText("下一步");
            }
        }
    }

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            String roundUp = DataUtils.getRoundUp(results[0], 1);
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + roundUp, false);

            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("1");
            if (bundle != null) {
                data.setSugarTime(bundle.getInt("selectMeasureSugarTime"));
            } else {
                data.setSugarTime(0);
            }
            data.setBloodSugar(Float.parseFloat(roundUp));
            datas.add(data);
            HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    ToastUtils.showShort("数据上传成功");
                }

                @Override
                public void onError() {
                    ToastUtils.showShort("数据上传失败");
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
