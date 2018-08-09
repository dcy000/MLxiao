package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;

import java.util.ArrayList;

public class HealthSugarDetectionUiFragment extends Bloodsugar_Fragment {


    private int selectMeasureSugarTime;
    private boolean isJump2Next = false;

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        Bundle arguments = getArguments();
        if (arguments != null) {
            selectMeasureSugarTime = arguments.getInt("selectMeasureSugarTime", 0);
        }

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
            DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("1");
            data.setSugarTime(selectMeasureSugarTime);
            data.setBloodSugar(Float.parseFloat(results[0]));
            datas.add(data);
            NetworkApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    if (fragmentChanged != null && !isJump2Next) {
                        isJump2Next = true;
                        fragmentChanged.onFragmentChanged(HealthSugarDetectionUiFragment.this, null);
                    }
                    ((HealthIntelligentDetectionActivity) getActivity()).putCacheData(data);
                }

                @Override
                public void onError() {
                    ToastUtils.showShort("上传数据失败");
                }
            });
        }
    }
}
