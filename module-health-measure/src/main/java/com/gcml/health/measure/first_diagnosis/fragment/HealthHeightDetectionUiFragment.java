package com.gcml.health.measure.first_diagnosis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.bean.DetailsModel;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;

public class HealthHeightDetectionUiFragment extends BluetoothBaseFragment implements HealthDiaryDetailsFragment.OnActionListener {
    private DetailsModel mUiModel;
    private static final int WHAT_HEIGHT_DETECTION = 1;
    private boolean isJump2Next = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiModel = new DetailsModel();
        mUiModel.setWhat(WHAT_HEIGHT_DETECTION);
        mUiModel.setTitle("请选择您的身高：");
        mUiModel.setUnitPosition(0);
        mUiModel.setUnits(new String[]{"cm"});
        mUiModel.setUnitSum(new String[]{"cm"});
        mUiModel.setSelectedValues(new float[]{160f});
        mUiModel.setMinValues(new float[]{50f});
        mUiModel.setMaxValues(new float[]{240f});
        mUiModel.setPerValues(new float[]{1f});
        mUiModel.setAction("下一步");
    }

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_weight;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        showUi();
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
        if (what == WHAT_HEIGHT_DETECTION) {
            //todo:上传身高数据
            if (fragmentChanged != null && !isJump2Next) {
                isJump2Next = true;
                fragmentChanged.onFragmentChanged(
                        HealthHeightDetectionUiFragment.this, null);
            }
        }
    }
}
