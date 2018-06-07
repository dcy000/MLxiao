package com.example.han.referralproject.bluetooth_devices.bloodpressure_devices;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.AllMeasureActivity;
import com.example.han.referralproject.bluetooth_devices.base.BaseFragment;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IPresenter;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.base.Logg;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;

public class Bloodpressure_Fragment extends BaseFragment implements IView, View.OnClickListener {
    private TextView mTitle3;
    private TextView mBtnHealthHistory;
    private TextView mBtnVideoDemo;
    private TextView mTvGaoya;
    private TextView mTvDiya;
    private TextView mTvMaibo;
    private Bloodpressure_Self_PresenterImp selfPresenterImp;

    @Override
    protected int initLayout() {
        return R.layout.fragment_bloodpressure;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mTitle3 = (TextView) view.findViewById(R.id.title3);
        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = (TextView) view.findViewById(R.id.tv_gaoya);
        mTvDiya = (TextView) view.findViewById(R.id.tv_diya);
        mTvMaibo = (TextView) view.findViewById(R.id.tv_maibo);
        dealLogic(bundle);
    }

    private void dealLogic(Bundle bundle) {
        String xueyaMac = LocalShared.getInstance(getContext()).getXueyaMac();
        if (bundle != null) {
            String brand = bundle.getString(IPresenter.BRAND);
            if (TextUtils.isEmpty(xueyaMac)) {
                switch (brand) {
                    case "MAI_LIAN":
                        selfPresenterImp = new Bloodpressure_Self_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "eBlood-Pressure"));
                        break;
                }
            } else {
                switch (brand) {
                    case "MAI_LIAN":
                        selfPresenterImp = new Bloodpressure_Self_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, xueyaMac, "eBlood-Pressure"));
                        break;
                }
            }
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            mTvGaoya.setText(datas[0]);
        } else if (datas.length == 3) {
            mTvGaoya.setText(datas[0]);
            mTvDiya.setText(datas[1]);
            mTvMaibo.setText(datas[2]);
        } else {
            Logg.e(Bloodpressure_Self_PresenterImp.class, "updateData: ");
        }
    }

    @Override
    public void updateState(String state) {
        ToastTool.showShort(state);
        ((AllMeasureActivity) getActivity()).speak(state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_health_history:
                break;
            case R.id.btn_video_demo:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        selfPresenterImp.onDestroy();
    }
}
