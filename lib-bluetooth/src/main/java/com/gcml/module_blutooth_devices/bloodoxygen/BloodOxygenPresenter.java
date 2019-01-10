package com.gcml.module_blutooth_devices.bloodoxygen;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.bluetooth.BaseBluetooth;
import com.gcml.module_blutooth_devices.bluetooth.BluetoothStore;
import com.gcml.module_blutooth_devices.bluetooth.DeviceBrand;
import com.gcml.module_blutooth_devices.bluetooth.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.HashMap;
import java.util.UUID;

public class BloodOxygenPresenter extends BaseBluetooth {
    /**自家血氧仪*/
    private static final String SELF_SERVICE = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String SELF_NOTIFY = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static final String SELF_WRITE = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static final byte[] SELF_DATA_OXYGEN_TO_WRITE = {(byte) 0xAA, 0x55, 0x0F, 0x03, (byte) 0x84, 0x01, (byte) 0xE0};

    /**超思血氧仪*/
    private String CHAOSI_SERVICE;
    private static final String CHASOSI_NOTIFY = "0000cd04-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_WRITE = "0000cd20-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY1 = "0000cd01-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY2 = "0000cd02-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY3 = "0000cd03-0000-1000-8000-00805f9b34fb";
    /**密码校验指令*/
    private static final byte[] CHAOSI_PASSWORD = {(byte) 0xAA, 0x55, 0x04, (byte) 0xB1, 0x00, 0x00, (byte) 0xB5};

    public BloodOxygenPresenter(IBluetoothView owner) {
        super(owner);
        startDiscovery(targetAddress);
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        baseView.updateData("initialization", "0", "0");
        if (name.startsWith("POD")) {
            //自家血氧仪
            handleSelf(address);
            return;
        }
        if (name.startsWith("iChoice")) {
            //超思血氧仪
            handleChaosi(address);
            return;
        }
        if (name.startsWith("SpO2080971")) {
            //康泰血氧仪
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
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.BLOODOXYGEN;
    }

    private void handleChaosi(String address) {
        CHAOSI_SERVICE = "ba11f08c-5f14-0b0d-1080-00" + address.toLowerCase().replace(":", "").substring(2);

        //第一通道监听
        BluetoothClientManager.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY1), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify1: pass" + bytes);
            }

            @Override
            public void onResponse(int i) {
                Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onResponse: cd01" + (i == 0 ? "成功" : "失败"));
            }
        });
        //第二通道监听
        BluetoothClientManager.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY2), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify2: pass" + bytes);
            }

            @Override
            public void onResponse(int i) {
                Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onResponse: cd02" + (i == 0 ? "成功" : "失败"));
            }
        });
        //第三通道
        BluetoothClientManager.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY3), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify3: pass" + bytes);
            }

            @Override
            public void onResponse(int i) {
                Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onResponse: cd03" + (i == 0 ? "成功" : "失败"));
            }
        });
        //写入密码校验
        BluetoothClientManager.getClient().write(address,
                UUID.fromString(CHAOSI_SERVICE),
                UUID.fromString(CHAOSI_WRITE),
                CHAOSI_PASSWORD, new BleWriteResponse() {
                    @Override
                    public void onResponse(int i) {
                        Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onResponseWrite: " + i);
                    }
                });

        BluetoothClientManager.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHASOSI_NOTIFY), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes.length == 6) {
                    baseView.updateData(bytes[3] + "", bytes[4] + "");
                    Logg.e(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify: 血氧：" + bytes[3]);
                    Logg.e(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify: 脉搏" + bytes[4]);
                }
            }

            @Override
            public void onResponse(int i) {
            }
        });
    }

    private void handleSelf(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_NOTIFY), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        if (value.length == 12) {
                            baseView.updateData(value[5] + "", value[6] + "");
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });

        BluetoothStore.getClient().write(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_WRITE), SELF_DATA_OXYGEN_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                    }
                });
    }
}
