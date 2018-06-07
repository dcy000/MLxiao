package com.example.han.referralproject.bluetooth_devices.bloodpressure_devices;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothServiceDetail;
import com.example.han.referralproject.bluetooth_devices.base.ClientManager;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.base.Logg;
import com.example.han.referralproject.util.LocalShared;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.List;
import java.util.UUID;

/**
 * 小机器人自带的血压计
 * name:eBlood-Pressure
 * mac:F4:5E:AB:0D:81:26
 */
public class Bloodpressure_Self_PresenterImp extends BaseBluetoothPresenter {
    private Bloodpressure_Fragment fragment;
    private static final String targetServiceUUid = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000fff4-0000-1000-8000-00805f9b34fb";

    public Bloodpressure_Self_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Bloodpressure_Fragment) fragment;

    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        super.discoveredTargetDevice(device);
        Logg.e(Bloodpressure_Self_PresenterImp.class, "discoveredTargetDevice: ");
    }

    @Override
    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
        super.connectSuccessed(serviceDetails);
        fragment.updateState(fragment.getString(R.string.bluetooth_device_connected));
        fragment.updateData("0","0","0");
        LocalShared.getInstance(fragment.getContext()).setXueyaMac(lockedDevice.getSearchResult().getAddress());
        if (serviceDetails != null && serviceDetails.size() > 0) {
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(),
                    UUID.fromString(targetServiceUUid),
                    UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                            int length = bytes.length;
                            switch (length) {
                                case 2:
                                    Logg.e(Bloodpressure_Self_PresenterImp.class, "onNotify: " + (bytes[1] & 0xff));
                                    fragment.updateData((bytes[1] & 0xff) + "");
                                    break;
                                case 12:
                                    Logg.e(Bloodpressure_Self_PresenterImp.class, "onNotify: " + "高压：" + (bytes[2] & 0xff) + "低压：" + (bytes[4] & 0xff) + "脉搏：" + (bytes[8] & 0xff));
                                    fragment.updateData((bytes[2] & 0xff) + "", (bytes[4] & 0xff) + "", (bytes[8] & 0xff) + "");
                                    break;
                            }
                        }

                        @Override
                        public void onResponse(int i) {

                        }
                    });
        }
    }

    @Override
    protected void disConnected() {
        if (fragment.isAdded()) {
            fragment.updateState(fragment.getString(R.string.bluetooth_device_disconnected));
        }
        super.disConnected();
    }
}
