package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;
import java.util.UUID;

/**
 * 小机器人自带的血压计
 * name:eBlood-Pressure
 * mac:F4:5E:AB:0D:81:26
 */
public class Bloodpressure_Self_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000fff4-0000-1000-8000-00805f9b34fb";
    private boolean isGetResult = false;

    public Bloodpressure_Self_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected boolean discoveredTargetDevice(SearchResult device) {
        super.discoveredTargetDevice(device);
        Logg.e(Bloodpressure_Self_PresenterImp.class, "discoveredTargetDevice: ");
        return false;
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturn) {
        super.connectSuccessed(address, serviceDetails, isReturn);
        baseView.updateState(baseView.getThisContext().getString(R.string.bluetooth_device_connected));
        baseView.updateData("0", "0", "0");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, targetName + "," + address);
        if (!isReturn) {
            BluetoothClientManager.getClient().notify(address,
                    UUID.fromString(targetServiceUUid),
                    UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                            int length = bytes.length;
                            switch (length) {
                                case 2:
                                    isGetResult=false;
                                    baseView.updateData((bytes[1] & 0xff) + "");
                                    break;
                                case 12:
                                    if (!isGetResult) {
                                        isGetResult = true;
                                        baseView.updateData((bytes[2] & 0xff) + "", (bytes[4] & 0xff) + "", (bytes[8] & 0xff) + "");
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onResponse(int i) {

                        }
                    });
        }
    }
}
