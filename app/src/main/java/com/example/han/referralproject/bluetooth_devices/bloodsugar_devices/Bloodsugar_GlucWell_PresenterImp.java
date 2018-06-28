package com.example.han.referralproject.bluetooth_devices.bloodsugar_devices;

import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothServiceDetail;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import java.util.List;

public class Bloodsugar_GlucWell_PresenterImp extends BaseBluetoothPresenter {
    private MeasureBloodsugarActivity mainActivity;

    public Bloodsugar_GlucWell_PresenterImp(IView activity, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        mainActivity = (MeasureBloodsugarActivity) activity;
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
        mainActivity.updateData("发现设备成功");
    }

    @Override
    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
        mainActivity.updateData("连接设备成功");
//        List<BluetoothServiceDetail> cache = new ArrayList<>();
//        for (BluetoothServiceDetail detail : serviceDetails) {
//            List<BluetoothServiceDetail.CharacteristicBean> characteristics = detail.getCharacteristics();
//            if (characteristics != null && characteristics.size() > 0) {
//                for (BluetoothServiceDetail.CharacteristicBean characteristicBean : characteristics) {
//                    if (characteristicBean.getType().contains(BluetoothServiceDetail.NOTIFY)) {
//                        cache.add(detail);
//                        break;
//                    }
//                }
//            }
//        }
//        mainActivity.updateData(cache);
    }
}
