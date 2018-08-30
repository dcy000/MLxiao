package com.gcml.health.measure.first_diagnosis.fragment;

import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 14:08
 * created by:gzq
 * description:TODO
 */
public class HealthTemperatureDetectionFragment extends Temperature_Fragment {
    private boolean isJump2Next = false;
    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
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
        if (results.length==1){
            ArrayList<DetectionData> datas = new ArrayList<>();
            final DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("4");
            data.setTemperAture(Float.parseFloat(results[0]));
            datas.add(data);
            HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    if (fragmentChanged != null && !isJump2Next) {
                        isJump2Next = true;
                        fragmentChanged.onFragmentChanged(HealthTemperatureDetectionFragment.this, null);
                    }
                    ((FirstDiagnosisActivity) getActivity()).putCacheData(data);
                }

                @Override
                public void onError() {
                    ToastUtils.showShort("上传数据失败");
                }
            });
        }
    }
}
