package com.gcml.module_blutooth_devices.temperature_devices;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;
import java.util.UUID;

/**
 * 爱立康耳温枪(鹿得)
 * 测试信息：
 * name:AET-WD
 * mac:04:B3:EC:50:6D:D3
 */

public class Temperature_Ailikang_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000ffb2-0000-1000-8000-00805f9b34fb";

    public Temperature_Ailikang_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected boolean discoveredTargetDevice(SearchResult device) {
        super.discoveredTargetDevice(device);
        Logg.e(Temperature_Ailikang_PresenterImp.class, "discoveredTargetDevice: 发现目标设备");
        return false;
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturn) {
        super.connectSuccessed(address, serviceDetails, isReturn);
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("initialization","0.00");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE,targetName+","+address);
        if (!isReturn) {
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    float result;
                    if (bytes.length == 13) {
                        result = bytes[9] + bytes[10] / 10 / 10.0f;
                    } else {
                        result = 0.0f;
                    }
                    Logg.e(Temperature_Ailikang_PresenterImp.class, "onNotify: 体温" + result);
                    baseView.updateData(result + "");
                }

                @Override
                public void onResponse(int i) {

                }
            });
        }
    }
}
