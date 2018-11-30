package com.example.module_blood_sugar.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.module_blood_sugar.R;
import com.example.module_blood_sugar.presenter.BloodSugarPresenter;
import com.gzq.lib_bluetooth.common.BaseBluetoothFragment;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;


public class BloodsugarFragment extends BaseBluetoothFragment implements IBluetoothView, View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;
    private int sugarTime;

    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            mTvResult.setText("0.00");
        } else if (datas.length == 1) {
            mTvResult.setText(datas[0]);
            MLVoiceSynthetize.startSynthesize("主人，您本次测量血糖" + datas[0]);
        }
    }

    @Override
    public void updateState(String state) {
        if (stateUpdate2Activity != null) {
            stateUpdate2Activity.onStateChanged(state);
        }
        ToastUtils.showShort(state);
        MLVoiceSynthetize.startSynthesize(state);
    }

    @Override
    public boolean isUploadData() {
        return true;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            emitEvent("HealthRecord>BloodSugar");
        } else if (i == R.id.btn_video_demo) {
            emitEvent("Video>BloodSugar");
        }
    }


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.bluetooth_fragment_bloodsugar;
    }

    @Override
    public void initParams(Bundle bundle) {
        if (bundle != null) {
            sugarTime = bundle.getInt("selectMeasureSugarTime", 0);
        }
    }

    @Override
    public void initView(View view) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = view.findViewById(R.id.tv_result);
        mTvResult.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BloodSugarPresenter(this, sugarTime);
    }

    public void abnormalData() {
        startActivityForResult(new Intent(getActivity(), BloodsugarAbnormalActivity.class), 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            boolean reason = data.getBooleanExtra(BloodsugarAbnormalActivity.KEY_HAS_ABNIRMAL_REASULT, false);
            if (reason) {
                ToastUtils.showShort("主人，因为你测量出现偏差，此次测量将不会作为历史数据");
                MLVoiceSynthetize.startSynthesize("主人，因为你测量出现偏差，此次测量将不会作为历史数据");
            } else {
                ((BloodSugarPresenter) mPresenter).uploadData();
            }
        }
    }
}
