package com.gcml.health.measure.first_diagnosis.fragment;

import android.view.View;

import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthThreeInOneDetectionUiFragment extends ThreeInOne_Fragment {
    private boolean isJump2Next = false;

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
    }

    private HashMap<String, Float> results = new HashMap<>();

    @Override
    protected void onMeasureFinished(String... result) {
        if (result.length == 2) {
            results.put(result[0], Float.parseFloat(result[1]));
        }
    }

    @Override
    protected void clickHealthHistory(View view) {
        uploadData();
    }

    private void uploadData() {
        if (results.size() == 0) {
            if (fragmentChanged != null && !isJump2Next) {
                isJump2Next = true;
                fragmentChanged.onFragmentChanged(this, null);
            }
            return;
        }
        ArrayList<DetectionData> datas = new ArrayList<>();
        final DetectionData sugarData = new DetectionData();
        final DetectionData cholesterolData = new DetectionData();
        final DetectionData lithicAcidData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        sugarData.setDetectionType("1");
        sugarData.setSugarTime(0);
        sugarData.setBloodSugar(results.get("bloodsugar"));
        cholesterolData.setDetectionType("7");
        cholesterolData.setCholesterol(results.get("cholesterol"));
        lithicAcidData.setDetectionType("8");
        lithicAcidData.setUricAcid(results.get("bua"));
        datas.add(sugarData);
        datas.add(cholesterolData);
        datas.add(lithicAcidData);
        HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
            @Override
            public void onSuccess(String callbackString) {
                if (fragmentChanged != null && !isJump2Next) {
                    isJump2Next = true;
                    fragmentChanged.onFragmentChanged(
                            HealthThreeInOneDetectionUiFragment.this, null);
                }
                ((FirstDiagnosisActivity) getActivity()).putCacheData(sugarData);
                ((FirstDiagnosisActivity) getActivity()).putCacheData(cholesterolData);
                ((FirstDiagnosisActivity) getActivity()).putCacheData(lithicAcidData);
            }

            @Override
            public void onError() {
                ToastUtils.showLong("数据上传失败");
            }
        });
    }
}
