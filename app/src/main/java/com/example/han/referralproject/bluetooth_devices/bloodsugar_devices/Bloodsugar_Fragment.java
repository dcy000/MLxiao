package com.example.han.referralproject.bluetooth_devices.bloodsugar_devices;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Bloodsugar_Fragment extends BaseFragment implements IView, View.OnClickListener {
    private TextView mBtnHealthHistory;
    private TextView mBtnVideoDemo;
    private TextView mTvResult;
    private IPresenter bluetoothPresenter;

    @Override
    protected int initLayout() {
        return R.layout.fragment_bloodsugar;
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
        String xuetangMac = LocalShared.getInstance(getContext()).getXuetangMac();
        if (bundle != null) {
            String brand = bundle.getString(IPresenter.BRAND);
            if (TextUtils.isEmpty(xuetangMac)) {
                switch (brand) {
                    case "WEI_YI":
                        bluetoothPresenter = new Bloodsugar_GlucWell_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "BLE-Glucowell"));
                        break;
                    case "SAN_NUO":
                        bluetoothPresenter = new Bloodsugar_Sannuo_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "BDE_WEIXIN_TTM"));
                        break;
                }
            } else {
                switch (brand) {
                    case "WEI_YI":
                        bluetoothPresenter = new Bloodsugar_GlucWell_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, xuetangMac, "BLE-Glucowell"));
                        break;
                    case "SAN_NUO":
                        bluetoothPresenter = new Bloodsugar_Sannuo_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, xuetangMac, "BDE_WEIXIN_TTM"));
                        break;
                }
            }

        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            mTvResult.setText(datas[0]);
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
        bluetoothPresenter.onDestroy();
    }
}
