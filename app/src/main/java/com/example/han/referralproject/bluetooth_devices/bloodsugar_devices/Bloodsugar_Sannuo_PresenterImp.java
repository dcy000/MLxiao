package com.example.han.referralproject.bluetooth_devices.bloodsugar_devices;

import android.util.Log;

import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;

public class Bloodsugar_Sannuo_PresenterImp extends BaseBluetoothPresenter{
    private Bloodsugar_Sannuo_Fragment fragment;
    private final String TAG=Bloodsugar_Sannuo_PresenterImp.this.getClass().getSimpleName();
    public Bloodsugar_Sannuo_PresenterImp(IView fragment ,DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment= (Bloodsugar_Sannuo_Fragment) fragment;
    }

    @Override
    public void searchDevices() {

    }

    @Override
    public void stateChanged(int state) {
        switch (state) {
            case DEVICE_FOUNDED://发现目标设备
                connectDevice();
                break;
            case DEVICE_UNFOUNDED://未发现目标设备
                break;
            case DEVICE_CONNECTED://设备连接成功
                break;
            case DEVICE_DISCONNECTED://设备连接断开
                break;

        }
    }

    @Override
    protected void discoverTargetDevice(BluetoothDevice device) {
        Log.d(TAG, "discoverTargetDevice: ");

    }
}
