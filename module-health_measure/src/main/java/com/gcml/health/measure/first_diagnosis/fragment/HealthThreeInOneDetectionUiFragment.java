package com.gcml.health.measure.first_diagnosis.fragment;

import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthThreeInOneDetectionUiFragment extends ThreeInOne_Fragment {
    private ArrayList<DetectionData> datas = new ArrayList<>();
    private boolean isJump2Next = false;
    private DetectionData sugarData;
    private DetectionData cholesterolData;
    private DetectionData lithicAcidData;

    @Override
    public void onStart() {
        super.onStart();
        isJump2Next = false;
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        setBtnClickableState(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，请将试纸插入仪器，开始测量", false);
    }


    @Override
    protected void onMeasureFinished(String... results) {

        if (results.length == 2) {
            if (results[0].equals("bloodsugar")) {
                sugarData = new DetectionData();
                sugarData.setDetectionType("1");
                sugarData.setSugarTime(0);
                sugarData.setBloodSugar(Float.parseFloat(results[1]));
                datas.add(sugarData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),"主人，您本次测量血糖"+sugarData.getBloodSugar());
                uploadData(datas);
            }
            if (results[0].equals("cholesterol")) {
                cholesterolData = new DetectionData();
                cholesterolData.setDetectionType("7");
                cholesterolData.setCholesterol(Float.parseFloat(results[1]));
                datas.add(cholesterolData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),"主人，您本次测量胆固醇"+cholesterolData.getCholesterol());
                uploadData(datas);
            }

            if (results[0].equals("bua")) {
                lithicAcidData = new DetectionData();
                lithicAcidData.setDetectionType("8");
                lithicAcidData.setUricAcid(Float.parseFloat(results[1]));

                datas.add(lithicAcidData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),"主人，您本次测量尿酸"+lithicAcidData.getUricAcid());
                uploadData(datas);
            }
//            if (sugarData != null && cholesterolData != null && lithicAcidData != null) {
//                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖"
//                        + sugarData.getBloodSugar() + ",尿酸" + lithicAcidData.getUricAcid() + ",胆固醇"
//                        + cholesterolData.getCholesterol(), false);
//                datas.add(sugarData);
//                datas.add(cholesterolData);
//                datas.add(lithicAcidData);
//
//            }
        }
    }

    private void uploadData(ArrayList<DetectionData> datas) {

        HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
            @Override
            public void onSuccess(String callbackString) {
                ToastUtils.showLong("数据上传成功");
                ((FirstDiagnosisActivity) mActivity).putCacheData(sugarData);
                ((FirstDiagnosisActivity) mActivity).putCacheData(cholesterolData);
                ((FirstDiagnosisActivity) mActivity).putCacheData(lithicAcidData);

//                        if (fragmentChanged != null && !isJump2Next) {
//                            isJump2Next = true;
//                            fragmentChanged.onFragmentChanged(
//                                    HealthThreeInOneDetectionUiFragment.this, null);
//                        }
                setBtnClickableState(true);
                datas.clear();
            }

            @Override
            public void onError() {
                ToastUtils.showLong("数据上传失败");
                datas.clear();
            }
        });
    }

    @Override
    protected void clickHealthHistory(View view) {
        if (fragmentChanged != null && !isJump2Next) {
            isJump2Next = true;
            fragmentChanged.onFragmentChanged(this, null);
        }
    }

    private void setBtnClickableState(boolean enableClick) {
        if (enableClick) {
            mBtnHealthHistory.setClickable(true);
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_health_history_set);
        } else {
            mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
            mBtnHealthHistory.setClickable(false);
        }
    }
}
