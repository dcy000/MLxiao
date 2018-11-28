package com.example.module_blood_sugar.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.module_blood_sugar.R;
import com.gzq.lib_bluetooth.common.BaseBluetoothFragment;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class SelectSugarDetectionTimeFragment extends BaseBluetoothFragment implements View.OnClickListener {

    private ImageView mIvEmptyStomach;
    private ImageView mIvTwoHours;
    private ImageView mIvOtherTime;


    public static final int ACTION_EMPTY_STOMACH = 0;
    public static final int ACTION_TWO_HOURS = 2;
    public static final int ACTION_OTHER_TIME = 3;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_empty_stomach) {
            if (fragmentReplaced != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("selectMeasureSugarTime", ACTION_EMPTY_STOMACH);
                fragmentReplaced.onFragmentChanged(this, bundle);
            }

        } else if (i == R.id.iv_two_hours) {
            if (fragmentReplaced != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("selectMeasureSugarTime", ACTION_TWO_HOURS);
                fragmentReplaced.onFragmentChanged(this, bundle);
            }

        } else if (i == R.id.iv_other_time) {
            if (fragmentReplaced != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("selectMeasureSugarTime", ACTION_OTHER_TIME);
                fragmentReplaced.onFragmentChanged(this, bundle);
            }

        }
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_measure_fragment_select_sugar_time;
    }

    @Override
    public void initParams(Bundle bundle) {
        MLVoiceSynthetize.startSynthesize("主人，请选择您当前的状态");
    }

    @Override
    public void initView(View view) {
        mIvEmptyStomach = view.findViewById(R.id.iv_empty_stomach);
        mIvTwoHours = view.findViewById(R.id.iv_two_hours);
        mIvOtherTime = view.findViewById(R.id.iv_other_time);
        mIvEmptyStomach.setOnClickListener(this);
        mIvTwoHours.setOnClickListener(this);
        mIvOtherTime.setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }
}
