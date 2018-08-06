package com.gcml.module_blutooth_devices.weight_devices;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.gcml.lib_utils.data.SPUtil;
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

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/1 13:34
 * created by:gzq
 * description: 自家配备的体重秤
 * name:000FatScale01
 * mac:12:30:00:5A:FF:16
 *
 */
public class Weight_Self_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "0000fff0-0000-1000-8000-00805f9b34fb";//主服务
    private static final String targetCharacteristicUUid = "0000fff1-0000-1000-8000-00805f9b34fb";

    public Weight_Self_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails,
                                    boolean isReturnServiceAndCharacteristic) {
        super.connectSuccessed(address, serviceDetails, isReturnServiceAndCharacteristic);
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("0.00");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_WEIGHT,targetName+","+address);
        BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] bytes) {
                        if (bytes.length == 14 && (bytes[1] & 0xff) == 221) {
                            float weight = ((float) (bytes[2] << 8) + (float) (bytes[3] & 0xff)) / 10;
                            baseView.updateData(String.format("%.2f", weight));
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
    }
}
