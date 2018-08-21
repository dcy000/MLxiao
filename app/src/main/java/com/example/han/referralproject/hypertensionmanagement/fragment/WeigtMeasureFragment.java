package com.example.han.referralproject.hypertensionmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.HealthDiaryDetailsFragment;
import com.example.han.referralproject.health.intelligentdetection.DataCacheFragment;
import com.example.han.referralproject.health.intelligentdetection.HealthWeightDetectionFragment;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.health.model.DetailsModel;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.hypertensionmanagement.util.AppManager;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkCallback;
import com.gcml.lib_utils.display.ToastUtils;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/7/30.
 */

public class WeigtMeasureFragment extends HealthWeightDetectionFragment implements HealthDiaryDetailsFragment.OnActionListener {
    private ImageView ivRight;
    private DetailsModel mUiModel;
    private HealthDiaryDetailsFragment mUiFragment;
    private static final int WHAT_WEIGHT_DETECTION = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiModel = new DetailsModel();
        mUiModel.setWhat(WHAT_WEIGHT_DETECTION);
        mUiModel.setTitle(getResources().getString(R.string.health_detection_weight_title));
        mUiModel.setUnitPosition(0);
        mUiModel.setUnits(new String[]{"kg"});
        mUiModel.setUnitSum(new String[]{"kg"});
        mUiModel.setSelectedValues(new float[]{50f});
        mUiModel.setMinValues(new float[]{0f});
        mUiModel.setMaxValues(new float[]{200f});
        mUiModel.setPerValues(new float[]{0.2f});
        mUiModel.setAction("下一步");
    }

    protected void initView(View view, Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.test_tizhong);
        view.findViewById(R.id.ll_back).setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
            getActivity().finish();
        });
        ivRight = view.findViewById(R.id.iv_top_right);
        ivRight.setImageResource(R.drawable.health_ic_blutooth);
        ivRight.setOnClickListener(v -> startDetection());
        showUi();
    }

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
    public void onResume() {
        super.onResume();
        startDetection();
    }

    @Override
    protected void onWeightResult(float weight) {
        if (mUiFragment != null) {
            mUiFragment.setValue(weight);
        }
    }

    @Override
    public void onAction(int what, float selectedValue, int unitPosition, String item) {
        if (WHAT_WEIGHT_DETECTION == what) {
            uploadData(selectedValue);
        }
    }

    private void uploadData(float weight) {
        if (getFragmentManager() != null) {
            DataCacheFragment.get(getFragmentManager()).getDataCache().put("weight", weight);
        }
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData data = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        data.setDetectionType("3");
        data.setWeight(weight);
        datas.add(data);
        NetworkApi.postMeasureData(datas, new NetworkCallback() {
            @Override
            public void onSuccess(String callbackString) {
                navToNext();
            }

            @Override
            public void onError() {
                ToastUtils.showLong("数据上传失败");
            }
        });
    }

    private void navToNext() {
        AppManager.getAppManager().finishAllActivity();
        startActivity(new Intent(getActivity(), TreatmentPlanActivity.class));
    }

}
