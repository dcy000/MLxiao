package com.gcml.health.measure.first_diagnosis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.health.measure.first_diagnosis.bean.DetailsModel;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;

import java.util.ArrayList;

public class HealthWeightDetectionUiFragment extends Weight_Fragment
        implements HealthDiaryDetailsFragment.OnActionListener {

    private DetailsModel mUiModel;
    private static final int WHAT_WEIGHT_DETECTION = 0;
    private boolean isJump2Next = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiModel = new DetailsModel();
        mUiModel.setWhat(WHAT_WEIGHT_DETECTION);
        mUiModel.setTitle(getResources().getString(R.string.health_measure_detection_weight_title));
        mUiModel.setUnitPosition(0);
        mUiModel.setUnits(new String[]{"kg"});
        mUiModel.setUnitSum(new String[]{"kg"});
        mUiModel.setSelectedValues(new float[]{60f});
        mUiModel.setMinValues(new float[]{0f});
        mUiModel.setMaxValues(new float[]{200f});
        mUiModel.setPerValues(new float[]{0.2f});
        mUiModel.setAction("下一步");
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        showUi();
        dealLogic();
    }

    private HealthDiaryDetailsFragment mUiFragment;

    private void showUi() {
        String tag = HealthDiaryDetailsFragment.class.getName();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null && fragment.isHidden()) {
            transaction.show(fragment);
        } else {
            fragment = HealthDiaryDetailsFragment.newInstance(mUiModel);
            transaction.add(R.id.fl_container, fragment, tag);
        }
        mUiFragment = (HealthDiaryDetailsFragment) fragment;
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onAction(int what, float selectedValue, int unitPosition, String item) {
        if (WHAT_WEIGHT_DETECTION == what) {
            uploadData(selectedValue);
        }
    }

    private void uploadData(float weight) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        final DetectionData weightData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        weightData.setDetectionType("3");
        weightData.setWeight(weight);
        datas.add(weightData);
        HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
            @Override
            public void onSuccess(String callbackString) {
                ((FirstDiagnosisActivity) mActivity).putCacheData(weightData);
                if (fragmentChanged != null && !isJump2Next) {
                    isJump2Next = true;
                    fragmentChanged.onFragmentChanged(
                            HealthWeightDetectionUiFragment.this, null);
                }
            }

            @Override
            public void onError() {
                ToastUtils.showLong("数据上传失败");
            }
        });
    }

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            mUiFragment.setValue(Float.parseFloat(results[0]));
        }
    }

}
