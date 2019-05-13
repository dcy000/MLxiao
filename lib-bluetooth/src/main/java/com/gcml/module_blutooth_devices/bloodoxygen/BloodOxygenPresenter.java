package com.gcml.module_blutooth_devices.bloodoxygen;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.DeviceBrand;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.google.common.primitives.Bytes;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.HashMap;
import java.util.UUID;

public class BloodOxygenPresenter extends BaseBluetooth {
    /**
     * 自家血氧仪
     */
    private static final String SELF_SERVICE = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String SELF_NOTIFY = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static final String SELF_WRITE = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static final byte[] SELF_DATA_OXYGEN_TO_WRITE = {(byte) 0xAA, 0x55, 0x0F, 0x03, (byte) 0x84, 0x01, (byte) 0xE0};

    /**
     * 超思血氧仪
     */
    private String CHAOSI_SERVICE;
    private static final String CHASOSI_NOTIFY = "0000cd04-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_WRITE = "0000cd20-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY1 = "0000cd01-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY2 = "0000cd02-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY3 = "0000cd03-0000-1000-8000-00805f9b34fb";
    /**
     * 密码校验指令
     */
    private static final byte[] CHAOSI_PASSWORD = {(byte) 0xAA, 0x55, 0x04, (byte) 0xB1, 0x00, 0x00, (byte) 0xB5};
    DetectionData detectionData = new DetectionData();

    public BloodOxygenPresenter(IBluetoothView owner) {
        this(owner, true);
    }

    public BloodOxygenPresenter(IBluetoothView owner, boolean isAutoDiscovery) {
        super(owner);
        if (isAutoDiscovery) {
            startDiscovery(targetAddress);
        }
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        DetectionData detectionData = new DetectionData();
        detectionData.setInit(true);
        detectionData.setBloodOxygen(0.0f);
        detectionData.setPulse(0);
        baseView.updateData(detectionData);

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
        SPUtil.put(BluetoothConstants.SP.SP_SAVE_BLOODOXYGEN, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_BLOODOXYGEN, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.BLOODOXYGEN;
    }

    private void handleChaosi(String address) {
        CHAOSI_SERVICE = "ba11f08c-5f14-0b0d-1080-00" + address.toLowerCase().replace(":", "").substring(2);

        //第一通道监听
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY1), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
            }

            @Override
            public void onResponse(int i) {
            }
        });
        //第二通道监听
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY2), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
            }

            @Override
            public void onResponse(int i) {
            }
        });
        //第三通道
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY3), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
            }

            @Override
            public void onResponse(int i) {
            }
        });
        //写入密码校验
        BluetoothStore.getClient().write(address,
                UUID.fromString(CHAOSI_SERVICE),
                UUID.fromString(CHAOSI_WRITE),
                CHAOSI_PASSWORD, new BleWriteResponse() {
                    @Override
                    public void onResponse(int i) {
                    }
                });

        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHASOSI_NOTIFY), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes.length == 6) {
                    detectionData.setBloodOxygen((float) bytes[3]);
                    detectionData.setPulse((int) bytes[4]);
                    detectionData.setInit(false);
                    baseView.updateData(detectionData);
//                    baseView.updateData(bytes[3] + "", bytes[4] + "");
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

                            detectionData.setBloodOxygen((float) value[5]);
                            detectionData.setPulse((int) value[6]);
                            detectionData.setInit(false);
                            baseView.updateData(detectionData);
//                            baseView.updateData(value[5] + "", value[6] + "");
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
