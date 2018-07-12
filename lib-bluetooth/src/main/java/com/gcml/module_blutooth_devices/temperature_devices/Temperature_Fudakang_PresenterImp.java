package com.gcml.module_blutooth_devices.temperature_devices;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * 福达康耳温枪
 * name:ClinkBlood
 * mac:C6:05:04:03:52:9B
 */
public class Temperature_Fudakang_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "0000fc00-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000fca1-0000-1000-8000-00805f9b34fb";

    public Temperature_Fudakang_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment,discoverSetting);
    }

    @Override
    protected boolean discoveredTargetDevice(SearchResult device) {
        super.discoveredTargetDevice(device);
        Logg.e(Temperature_Fudakang_PresenterImp.class, "discoveredTargetDevice: 发现目标设备");
        return false;
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturn) {
        super.connectSuccessed(address, serviceDetails, isReturn);
        Logg.e(Temperature_Fudakang_PresenterImp.class, "福达康连接成功");
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("0.00");
        SPUtil.put(baseContext,SPUtil.SP_SAVE_TEMPERATURE,targetName+","+address);
        if (!isReturn) {
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    if (bytes.length == 8 && bytes[4] != 85) {
                        byte[] bytes1 = new byte[8];
                        System.arraycopy(bytes, 0, bytes1, 0, 8);
                        float result = ((int) (((bytes1[4] << 8) + (float) (bytes1[5] & 0xff)) / 10)) / 10.0f;
                        Logg.e(Temperature_Fudakang_PresenterImp.class, "onNotify: 结果" + result);
                        baseView.updateData(result + "");
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
        super.disConnected();
        Logg.e(Temperature_Fudakang_PresenterImp.class, "福达康断开连接");
        if (baseView instanceof Activity) {
            baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
        } else if (baseView instanceof Fragment) {
            if (((Fragment) baseView).isAdded()) {
                baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
            }
        }
    }
}
