package com.example.han.referralproject.health.intelligentdetection;

import android.content.Intent;
import android.net.Uri;
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
import com.example.han.referralproject.health.model.DetailsModel;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;

public class HealthWeightDetectionUiFragment extends HealthWeightDetectionFragment
        implements HealthDiaryDetailsFragment.OnActionListener {

    private DetailsModel mUiModel;
    private static final int WHAT_WEIGHT_DETECTION = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.tips_xueya);
        MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血压测量演示视频");
        mUiModel = new DetailsModel();
        mUiModel.setWhat(WHAT_WEIGHT_DETECTION);
        mUiModel.setTitle(getResources().getString(R.string.health_detection_weight_title));
        mUiModel.setUnitPosition(0);
        mUiModel.setUnits(new String[]{"kg"});
        mUiModel.setUnitSum(new String[]{"kg"});
        mUiModel.setSelectedValues(new float[]{0f});
        mUiModel.setMinValues(new float[]{0f});
        mUiModel.setMaxValues(new float[]{200f});
        mUiModel.setPerValues(new float[]{0.2f});
        mUiModel.setAction("下一步");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MeasureVideoPlayActivity.REQUEST_PALY_VIDEO) {
            startDetection();
        }
    }

    private ImageView ivRight;

    @Override
    protected int layoutId() {
        return super.layoutId();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.test_tizhong);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        ivRight.setImageResource(R.drawable.health_ic_blutooth);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetection();
            }
        });
        showUi();
    }

    HealthDiaryDetailsFragment mUiFragment;

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

        }
    }

    @Override
    protected void onWeightResult(float weight) {
        if (mUiFragment != null) {
            mUiFragment.setValue(weight);
        }
    }
}
