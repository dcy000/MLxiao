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
import com.gcml.module_blutooth_devices.utils.ToastUtils;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

/**
 * 小机器人配置的耳温枪
 * 测试机器如下
 * name:FSRKB-EWQ01
 * mac:94:E3:6D:54:1D:2D
 */
public class Temperature_Zhiziyun_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "00001910-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000fff2-0000-1000-8000-00805f9b34fb";

    public Temperature_Zhiziyun_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected boolean discoveredTargetDevice(SearchResult device) {
        super.discoveredTargetDevice(device);
        return false;
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturn) {
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("initialization","0.00");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE,targetName+","+address);
        if (!isReturn) {
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Temperature_Zhiziyun_PresenterImp.class, "onNotify: " + ByteUtils.byteToString(bytes));
                    if (bytes.length > 6) {
                        int data = bytes[6] & 0xff;
                        if (data < 44) {
                            ToastUtils.showShort("测量温度不正常");
                            return;
                        }
                        double v = (data - 44.0) % 10;
                        double result = (30.0 + (data - 44) / 10 + v / 10);
                        baseView.updateData(result + "");
                    }
                }

                @Override
                public void onResponse(int i) {

                }
            });
        }
    }

}
