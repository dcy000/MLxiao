package com.gcml.module_blutooth_devices.temperature_devices;


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
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

/**
 * 美的连耳温枪
 * name:MEDXING-IRT
 * mac:00:13:04:25:A7:65
 */
public class Temperature_Meidilian_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static String TAG = Temperature_Meidilian_PresenterImp.class.getSimpleName();

    public Temperature_Meidilian_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected boolean discoveredTargetDevice(SearchResult device) {
        //此处的super.必须书写
        super.discoveredTargetDevice(device);
        return false;
    }


    @Override
    protected void connectSuccessed(String address, final List<BluetoothServiceDetail> serviceDetails, boolean isReturn) {
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("0.00");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_TEMPERATURE,targetName+","+address);
        if (!isReturn) {
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Temperature_Meidilian_PresenterImp.class, "onNotify: " + ByteUtils.byteToString(bytes) + "长度：" + bytes.length);
                    if (bytes.length == 4) {
                        float result = ((float) (bytes[3] << 8) + (float) (bytes[2] & 0xff)) / 10;
                        Logg.e(Temperature_Meidilian_PresenterImp.class, "onNotify: " + result);
                        if (result < 50) {
                            baseView.updateData(result + "");
                        }
                    }
                }

                @Override
                public void onResponse(int i) {

                }
            });
        }
    }
}
