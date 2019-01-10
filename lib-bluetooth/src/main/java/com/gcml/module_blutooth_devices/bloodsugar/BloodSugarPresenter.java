package com.gcml.module_blutooth_devices.bloodsugar;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.bluetooth.BaseBluetooth;
import com.gcml.module_blutooth_devices.bluetooth.BluetoothStore;
import com.gcml.module_blutooth_devices.bluetooth.DeviceBrand;
import com.gcml.module_blutooth_devices.bluetooth.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.HashMap;
import java.util.UUID;

public class BloodSugarPresenter extends BaseBluetooth {
    private static final String SELF_SERVICE = "00001000-0000-1000-8000-00805f9b34fb";
    private static final String SELF_NOTIFY = "00001002-0000-1000-8000-00805f9b34fb";
    private static final String SELF_WRITE = "00001001-0000-1000-8000-00805f9b34fb";
    private static final byte[] SELF_DATA_SUGAR_TO_WRITE = {0x5A, 0x0A, 0x03, 0x10, 0x05, 0x02, 0x0F, 0x21, 0x3B, (byte) 0xEB};

    public BloodSugarPresenter(IBluetoothView owner) {
        super(owner);
        startDiscovery(targetAddress);
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        baseView.updateData("initialization", "0.0");
        if (name.startsWith("BLE-Glucowell")) {
            return;
        }
        if (name.startsWith("Bioland-BGM")) {
            handleSelf(address);
            return;
        }
        baseView.updateState("未兼容该设备:" + name + ":::" + address);
    }

    @Override
    protected boolean isSelfConnect(String name, String address) {
        if (name.startsWith("BLE-Glucowell")) {
            new BloodsugarGlucWellPresenter(getActivity(), baseView, name, address);
            return true;
        }
        if (name.startsWith("Bioland-BGM")) {
            return false;
        }
        return super.isSelfConnect(name, address);
    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected(String address) {

    }

    @Override
    protected void saveSP(String sp) {
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.BLOODSUGAR;
    }

    private void handleSelf(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_NOTIFY), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] bytes) {
                        if (bytes.length >= 12) {
                            float sugar = ((float) (bytes[10] << 8) + (float) (bytes[9] & 0xff)) / 18;
                            baseView.updateData(String.format("%.1f", sugar));
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
        BluetoothStore.getClient().write(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_WRITE), SELF_DATA_SUGAR_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }
}
