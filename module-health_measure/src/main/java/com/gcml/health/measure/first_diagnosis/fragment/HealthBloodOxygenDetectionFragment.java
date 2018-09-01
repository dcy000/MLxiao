package com.gcml.health.measure.first_diagnosis.fragment;

import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 14:05
 * created by:gzq
 * description:TODO
 */
public class HealthBloodOxygenDetectionFragment extends Bloodoxygen_Fragment{
    private boolean isJump2Next=false;
    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
    }

    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getContext(),"主人，请打开设备开关,开始测量",false);
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
        if (results.length==2){
            ArrayList<DetectionData> datas = new ArrayList<>();
            final DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("6");
            data.setBloodOxygen(Float.parseFloat(results[0]));
            data.setPulse(Integer.parseInt(results[1]));
            datas.add(data);
            HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    if (fragmentChanged != null && !isJump2Next) {
                        isJump2Next = true;
                        fragmentChanged.onFragmentChanged(HealthBloodOxygenDetectionFragment.this, null);
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
