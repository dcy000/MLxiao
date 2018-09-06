package com.gcml.module_blutooth_devices.others;

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

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/1 14:03
 * created by:gzq
 * description:自带三合一
 * name:BeneCheck GL-0F8B0C
 */
public class ThreeInOne_Self_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "00001808-0000-1000-8000-00805f9b34fb";//主服务
    private static final String targetCharacteristicUUid = "00002a18-0000-1000-8000-00805f9b34fb";

    public ThreeInOne_Self_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturnServiceAndCharacteristic) {
        super.connectSuccessed(address, serviceDetails, isReturnServiceAndCharacteristic);
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("initialization");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_THREE_IN_ONE,targetName+","+address);
        BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        parseData(value);
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    protected void parseData(byte[] bytes) {
        if (bytes.length < 13) {
            return;
        }
        int temp = ((bytes[11] & 0xff) << 8) + (bytes[10] & 0xff);
        int basic = (int) Math.pow(16, 3);
        int flag = temp / basic;
        int number = temp % basic;
        float result = (float) (number / Math.pow(10, 13 - flag));
        if (bytes[1] == 65) {//血糖
            baseView.updateData("bloodsugar",String.format("%.1f",result));
        } else if (bytes[1] == 81) {//尿酸
            baseView.updateData("bua",String.format("%.2f",result));
        } else if (bytes[1] == 97) {//胆固醇
            baseView.updateData("cholesterol",String.format("%.2f",result));
        }
    }
}
