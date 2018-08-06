package com.gcml.module_blutooth_devices.bloodoxygen_devices;

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
 * created on 2018/8/1 16:50
 * created by:gzq
 * description:自己血氧仪
 * name:POD
 * mac:50:65:83:9A:56:DF
 */
public class Bloodoxygen_Self_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static final String targetWriteCharacteristicUUid = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static final byte[] DATA_OXYGEN_TO_WRITE = {(byte) 0xAA, 0x55, 0x0F, 0x03, (byte) 0x84, 0x01, (byte) 0xE0};

    public Bloodoxygen_Self_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails,
                                    boolean isReturnServiceAndCharacteristic) {
        super.connectSuccessed(address, serviceDetails, isReturnServiceAndCharacteristic);
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("0", "0");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN, targetName + "," + address);

        BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        Timber.e("采集到的数据"+value.length);
                        if (value.length==12){
                            Timber.e("血氧%s,脉搏%s",value[5],value[6]);
                            baseView.updateData(value[5]+"",value[6]+"");
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });

        BluetoothClientManager.getClient().write(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetWriteCharacteristicUUid), DATA_OXYGEN_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        Timber.e(code == 0 ? "写入成功" : "写入失败");
                    }
                });

    }
}
