package com.gcml.health.measure.first_diagnosis.fragment;

import android.os.Bundle;
import android.view.View;

import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

public class HealthSugarDetectionUiFragment extends Bloodsugar_Fragment {


    private int selectMeasureSugarTime;
    private boolean isJump2Next = false;

    @Override
    public void onStart() {
        super.onStart();
        isJump2Next=false;
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        Bundle arguments = getArguments();
        if (arguments != null) {
            selectMeasureSugarTime = arguments.getInt("selectMeasureSugarTime", 0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getContext(),"主人，请将试纸插入仪器，开始测量",false);
    }

    @Override
    protected void clickHealthHistory(View view) {
        if (fragmentChanged != null && !isJump2Next) {
            isJump2Next = true;
            fragmentChanged.onFragmentChanged(this, null);
        }
    }

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            ArrayList<DetectionData> datas = new ArrayList<>();
            final DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("1");
            data.setSugarTime(selectMeasureSugarTime);
            data.setBloodSugar(Float.parseFloat(results[0]));
            datas.add(data);
            HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    if (fragmentChanged != null && !isJump2Next) {
                        isJump2Next = true;
                        fragmentChanged.onFragmentChanged(HealthSugarDetectionUiFragment.this, null);
                    }
                    ((FirstDiagnosisActivity) mActivity).putCacheData(data);
                }

                @Override
                public void onError() {
                    ToastUtils.showShort("上传数据失败");
                }
            });
        }
    }
}
