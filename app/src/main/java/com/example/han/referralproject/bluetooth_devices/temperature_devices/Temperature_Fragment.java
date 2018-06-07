package com.example.han.referralproject.bluetooth_devices.temperature_devices;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.han.referralproject.bluetooth_devices.bloodsugar_devices.Bloodsugar_GlucWell_PresenterImp;
import com.example.han.referralproject.bluetooth_devices.bloodsugar_devices.Bloodsugar_Sannuo_PresenterImp;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;

public class Temperature_Fragment extends BaseFragment implements IView, View.OnClickListener {
    private TextView mBtnHealthHistory;
    private TextView mBtnVideoDemo;
    private TextView mTvResult;
    private BaseBluetoothPresenter bluetoothPresenter;

    @Override
    protected int initLayout() {
        return R.layout.fragment_temperature;
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
        String wenduMac = LocalShared.getInstance(getContext()).getWenduMac();
        if (bundle != null) {
            String brand = bundle.getString(IPresenter.BRAND);
            if (TextUtils.isEmpty(wenduMac)) {
                switch (brand) {
                    case "AI_LI_KANG":
                        bluetoothPresenter = new Temperature_Ailikang_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "AET-WD"));
                        break;
                    case "FU_DA_KANG":
                        bluetoothPresenter = new Temperature_Fudakang_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "ClinkBlood"));
                        break;
                    case "MEI_DI_LIAN":
                        bluetoothPresenter = new Temperature_Meidilian_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "MEDXING-IRT"));
                        break;
                    case "MAI_LIAN":
                        bluetoothPresenter = new Temperature_Zhiziyun_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME, null, "FSRKB-EWQ01"));
                        break;
                }
            } else {
                switch (brand) {
                    case "AI_LI_KANG":
                        bluetoothPresenter = new Temperature_Ailikang_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, wenduMac, "AET-WD"));
                        break;
                    case "FU_DA_KANG":
                        bluetoothPresenter = new Temperature_Fudakang_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, wenduMac, "ClinkBlood"));
                        break;
                    case "MEI_DI_LIAN":
                        bluetoothPresenter = new Temperature_Meidilian_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, wenduMac, "MEDXING-IRT"));
                        break;
                    case "MAI_LIAN":
                        bluetoothPresenter = new Temperature_Zhiziyun_PresenterImp(this,
                                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, wenduMac, "FSRKB-EWQ01"));
                        break;
                }
            }

        }
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
    public void onStop() {
        super.onStop();
        bluetoothPresenter.onDestroy();
    }
}
