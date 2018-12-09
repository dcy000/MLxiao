package com.example.module_blood_pressure.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.module_blood_pressure.R;
import com.example.module_blood_pressure.presenter.BloodpressurePresenter;
import com.gcml.lib_video_ksyplayer.MeasureVideoPlayActivity;
import com.gzq.lib_bluetooth.common.BaseBluetoothFragment;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;


public class BloodpressureFragment extends BaseBluetoothFragment implements IBluetoothView, View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvGaoya;
    private TextView mTvDiya;
    private TextView mTvMaibo;

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            mTvGaoya.setText(datas[0]);
            mTvDiya.setText("0");
            mTvMaibo.setText("0");
        } else if (datas.length == 3) {
            mTvGaoya.setText(datas[0]);
            mTvDiya.setText(datas[1]);
            mTvMaibo.setText(datas[2]);
            MLVoiceSynthetize.startSynthesize("主人，您本次测量高压" + datas[0] + ",低压" + datas[1] + ",脉搏" + datas[2]);
        } else {

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
            emitEvent("HealthRecord>BloodPressure");
        } else if (i == R.id.btn_video_demo) {
            emitEvent("Video>BloodPressure");
            //血压演示视频
            Uri uri = Uri.parse("android.resource://" + Box.getApp().getPackageName() + "/" + com.gcml.lib_video_ksyplayer.R.raw.tips_xueya);
            MeasureVideoPlayActivity.startActivity(mActivity, uri, null, "血压测量演示视频");
        }
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.bluetooth_fragment_bloodpressure;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = view.findViewById(R.id.tv_gaoya);
        mTvDiya = view.findViewById(R.id.tv_diya);
        mTvMaibo = view.findViewById(R.id.tv_maibo);
        mTvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvMaibo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BloodpressurePresenter(this);
    }


    public void abnormalData() {
        startActivityForResult(new Intent(getActivity(), BloodpressureAbnormalActivity.class), 1001);
    }

    public void normalData(String state, int score, int currentHigh,
                           int currentLow, String suggest) {
        ShowMeasureBloodpressureResultActivity.startActivity(getActivity(), state, score, currentHigh, currentLow, suggest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            boolean reason = data.getBooleanExtra(BloodpressureAbnormalActivity.KEY_HAS_ABNIRMAL_REASULT, false);
            if (reason) {
                ToastUtils.showShort("主人，因为你测量出现偏差，此次测量将不会作为历史数据");
                MLVoiceSynthetize.startSynthesize("主人，因为你测量出现偏差，此次测量将不会作为历史数据");
            } else {
                ((BloodpressurePresenter) mPresenter).uploadData();
            }
        }
    }
}
