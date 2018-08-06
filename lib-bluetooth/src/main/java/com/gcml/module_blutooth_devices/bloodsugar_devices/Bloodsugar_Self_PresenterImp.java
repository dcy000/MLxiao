package com.gcml.module_blutooth_devices.bloodsugar_devices;

import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.List;
import java.util.UUID;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/1 15:47
 * created by:gzq
 * description:自家血糖仪
 * name:Bioland-BGM
 * mac:
 */
public class Bloodsugar_Self_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "00001000-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "00001002-0000-1000-8000-00805f9b34fb";
    private static final String targetWriteCharacteristicUUid = "00001001-0000-1000-8000-00805f9b34fb";
    private static final byte[] DATA_SUGAR_TO_WRITE = {0x5A, 0x0A, 0x03, 0x10, 0x05, 0x02, 0x0F, 0x21, 0x3B, (byte) 0xEB};

    public Bloodsugar_Self_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturnServiceAndCharacteristic) {
        super.connectSuccessed(address, serviceDetails, isReturnServiceAndCharacteristic);
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("0.00");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, targetName + "," + address);
        BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] bytes) {
                        if (bytes.length >= 12) {
                            float sugar = ((float) (bytes[10] << 8) + (float) (bytes[9] & 0xff)) / 18;
                            Timber.i("<- sugar = %s", sugar);
                            baseView.updateData(String.format("%.2f", sugar));
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
        BluetoothClientManager.getClient().write(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetWriteCharacteristicUUid), DATA_SUGAR_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }
}
