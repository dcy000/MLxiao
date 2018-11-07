package com.example.han.referralproject.single_measure;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class HealthSelectSugarDetectionTimeFragment extends BluetoothBaseFragment implements View.OnClickListener {

    private ImageView mIvEmptyStomach;
    private ImageView mIvTwoHours;
    private ImageView mIvOtherTime;

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_select_sugar_time;
    }
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mIvEmptyStomach = view.findViewById(R.id.iv_empty_stomach);
        mIvTwoHours = view.findViewById(R.id.iv_two_hours);
        mIvOtherTime = view.findViewById(R.id.iv_other_time);
        mIvEmptyStomach.setOnClickListener(this);
        mIvTwoHours.setOnClickListener(this);
        mIvOtherTime.setOnClickListener(this);
    }

    public static final int ACTION_EMPTY_STOMACH = 0;
    public static final int ACTION_TWO_HOURS = 2;
    public static final int ACTION_OTHER_TIME = 3;

    @Override
    public void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getContext(),"主人，请选择您当前的状态",false);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_empty_stomach) {
            if (fragmentChanged != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("selectMeasureSugarTime", ACTION_EMPTY_STOMACH);
                fragmentChanged.onFragmentChanged(this, bundle);
            }

        } else if (i == R.id.iv_two_hours) {
            if (fragmentChanged != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("selectMeasureSugarTime", ACTION_TWO_HOURS);
                fragmentChanged.onFragmentChanged(this, bundle);
            }

        } else if (i == R.id.iv_other_time) {
            if (fragmentChanged != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("selectMeasureSugarTime", ACTION_OTHER_TIME);
                fragmentChanged.onFragmentChanged(this, bundle);
            }

        }
    }
}
