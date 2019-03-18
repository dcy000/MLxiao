package com.gcml.module_blutooth_devices.three;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.DeviceBrand;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.HashMap;
import java.util.UUID;

public class ThreeInOnePresenter extends BaseBluetooth {
    private static final String SELF_SERVICE = "00001808-0000-1000-8000-00805f9b34fb";//主服务
    private static final String SELF_NOTIFY = "00002a18-0000-1000-8000-00805f9b34fb";

    public ThreeInOnePresenter(IBluetoothView owner) {
        super(owner);
        startDiscovery(targetAddress);
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        baseView.updateData("initialization");
        if (name.startsWith("BeneCheck")) {
            handleSelf(address);
            return;
        }
        baseView.updateState("未兼容该设备:" + name + ":::" + address);
    }


    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected(String address) {

    }

    @Override
    protected void saveSP(String sp) {
        SPUtil.put(BluetoothConstants.SP.SP_SAVE_THREE_IN_ONE, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_THREE_IN_ONE, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.THREE_IN_ONE;
    }

    private void handleSelf(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_NOTIFY), new BleNotifyResponse() {
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
            baseView.updateData("bloodsugar", String.format("%.1f", result));
        } else if (bytes[1] == 81) {//尿酸
            baseView.updateData("bua", String.format("%.2f", result));
        } else if (bytes[1] == 97) {//胆固醇
            baseView.updateData("cholesterol", String.format("%.2f", result));
        }
    }
}
