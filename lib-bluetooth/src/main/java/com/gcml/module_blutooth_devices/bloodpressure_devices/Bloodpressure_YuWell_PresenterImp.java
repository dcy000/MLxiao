package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.util.Log;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.List;
import java.util.UUID;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/4 18:49
 * created by:gzq
 * description:鱼跃血压计
 */
public class Bloodpressure_YuWell_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "00001810-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "00002a35-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUidForNotify = "00002a36-0000-1000-8000-00805f9b34fb";
    private static final String TAG = "Bloodpressure_YuWell_Pr";

    public Bloodpressure_YuWell_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected void connectSuccessed(final String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturnServiceAndCharacteristic) {
        super.connectSuccessed(address, serviceDetails, isReturnServiceAndCharacteristic);
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("0", "0", "0");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, targetName + "," + address);
        if (!isReturnServiceAndCharacteristic) {
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid),
                    UUID.fromString(targetCharacteristicUUidForNotify), new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID service, UUID character, byte[] value) {
                            if (value.length == 19) {
                                baseView.updateData((value[1] & 0xff) + "");
                            }
                        }

                        @Override
                        public void onResponse(int code) {

                        }
                    });
            BluetoothClientManager.getClient().indicate(address, UUID.fromString(targetServiceUUid),
                    UUID.fromString(targetCharacteristicUUid),
                    new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID service, UUID character, byte[] value) {
                            Log.i(TAG, "indicate: " + value.length);
                            if (value.length == 19) {
                                baseView.updateData((value[1] & 0xff) + "", (value[3] & 0xff) + "", (value[14] & 0xff) + "");
                            }
                        }

                        @Override
                        public void onResponse(int code) {

                        }
                    });
        }
    }
}
