package com.example.han.referralproject.bluetooth_devices.bloodoxygen_devices;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.AllMeasureActivity;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BaseFragment;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IPresenter;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;

public class Bloodoxygen_Fragment extends BaseFragment implements IView, View.OnClickListener {
    private TextView mBtnHealthHistory;
    private TextView mBtnVideoDemo;
    private TextView mTvResult;
    private BaseBluetoothPresenter bluetoothPresenter;

    @Override
    protected int initLayout() {
        return R.layout.fragment_bloodoxygen;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        dealLogic(bundle);
    }

    private void dealLogic(Bundle bundle) {
        String xueyangMac = LocalShared.getInstance(getContext()).getXueyangMac();
        if (bundle != null) {
            String brand = bundle.getString(IPresenter.BRAND);
            if (TextUtils.isEmpty(xueyangMac)) {
                switch (brand) {
                    case "HUA_DAI_FU":
                        bluetoothPresenter = new Bloodoxygen_Chaosi_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "iChoice"));
                        break;
                    case "KANG_TAI":
                        bluetoothPresenter = new Bloodoxygen_Kangtai_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "SpO2080971"));
                        break;
                }
            } else {
                switch (brand) {
                    case "HUA_DAI_FU":
                        bluetoothPresenter = new Bloodoxygen_Chaosi_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, xueyangMac, "iChoice"));
                        break;
                    case "KANG_TAI":
                        bluetoothPresenter = new Bloodoxygen_Chaosi_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, xueyangMac, "SpO2080971"));
                        break;
                }
            }
        }

    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            mTvResult.setText(datas[0]);
        }
    }

    @Override
    public void updateState(String state) {
        ToastTool.showShort(state);
        ((AllMeasureActivity) getActivity()).speak(state);
    }

    @Override
    public void onStop() {
        super.onStop();
        bluetoothPresenter.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_health_history:
                break;
            case R.id.btn_video_demo:
                break;
        }
    }
}
