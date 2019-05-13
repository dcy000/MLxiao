package com.gcml.module_blutooth_devices.bloodpressure;

import android.annotation.SuppressLint;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.DeviceBrand;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.HashMap;
import java.util.UUID;

public class BloodPressurePresenter extends BaseBluetooth {
    /**
     * 超思
     */
    private String CHAOSI_SERVICE;
    private static final String CHAOSI_NOTIFY = "0000cd04-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_WRITE = "0000cd20-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY1 = "0000cd01-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY2 = "0000cd02-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY3 = "0000cd03-0000-1000-8000-00805f9b34fb";
    private static final byte[] CHAOSI_PASSWORD = {(byte) 0xAA, 0x55, 0x04, (byte) 0xB1, 0x00, 0x00, (byte) 0xB5};//密码校验指令

    /**
     * 自家
     */
    private static final String SELF_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String SELF_NOTIFY = "0000fff4-0000-1000-8000-00805f9b34fb";
    private boolean isGetResult = false;

    /**
     * 西恩4.0
     */
    private static final String XIEN4_SERVICE = "01000000-0000-0000-0000-000000000080";
    private static final String XIEN4_WRITE = "05000000-0000-0000-0000-000000000080";
    private static final String XIEN4_NOTIFY = "02000000-0000-0000-0000-000000000080";
    private byte[] XIEN4_COMMOND = {(byte) 0xff, (byte) 0xff, 0x05, 0x01, (byte) 0xfa};
    /**
     * 鱼跃
     */
    private static final String YUYUE_SERVICE = "00001810-0000-1000-8000-00805f9b34fb";
    private static final String YUYUE_INDICATE = "00002a35-0000-1000-8000-00805f9b34fb";
    private static final String YUYUE_NOTIFY = "00002a36-0000-1000-8000-00805f9b34fb";

    private BloodpressureXien2Presenter xien2Presenter;
    DetectionData detectionData = new DetectionData();

    public BloodPressurePresenter(IBluetoothView owner) {
        this(owner, true);
    }

    public BloodPressurePresenter(IBluetoothView owner, boolean isAutoDiscovery) {
        super(owner);
        if (isAutoDiscovery) {
            startDiscovery(targetAddress);
        }
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        detectionData.setInit(true);
        detectionData.setHighPressure(0);
        detectionData.setLowPressure(0);
        detectionData.setPulse(0);
        baseView.updateData(detectionData);
        if (name.startsWith("iChoice")) {
            CHAOSI_SERVICE = "ba11f08c-5f14-0b0d-10a0-00" + address.toLowerCase().replace(":", "").substring(2);
            handleChaosi(address);
            return;
        }
        if (name.startsWith("eBlood")) {
            handleSelf(address);
            return;
        }
        if (name.startsWith("LD")) {
            handleXien4(address);
            return;
        }
        if (name.startsWith("Yuwell")) {
            handleYuyue(address);
            return;
        }
        baseView.updateState("未兼容该设备:" + name + ":::" + address);
    }


    @Override
    protected boolean isSelfConnect(String name, String address) {
        if (name.startsWith("iChoice")) {
            return false;
        }
        if (name.startsWith("eBlood")) {
            return false;
        }
        if (name.startsWith("LD")) {
            return false;
        }
        if (name.startsWith("Dual")) {
            xien2Presenter = new BloodpressureXien2Presenter(getActivity(), baseView, name, address);
            return true;
        }
        if (name.startsWith("Yuwell")) {
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
        SPUtil.put(BluetoothConstants.SP.SP_SAVE_BLOODPRESSURE, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_BLOODPRESSURE, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.BLOODPRESSURE;
    }

    private void handleYuyue(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(YUYUE_SERVICE),
                UUID.fromString(YUYUE_NOTIFY), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        if (value.length == 19) {
                            detectionData.setInit(true);
                            detectionData.setHighPressure((value[1] & 0xff));
                            detectionData.setLowPressure(0);
                            detectionData.setPulse(0);
                            baseView.updateData(detectionData);
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
        BluetoothStore.getClient().indicate(address, UUID.fromString(YUYUE_SERVICE),
                UUID.fromString(YUYUE_INDICATE),
                new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        if (value.length == 19) {
                            detectionData.setInit(false);
                            detectionData.setHighPressure((value[1] & 0xff));
                            detectionData.setLowPressure((value[3] & 0xff));
                            detectionData.setPulse((value[14] & 0xff));
                            baseView.updateData(detectionData);
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    private void handleXien4(String address) {
        BluetoothStore.getClient().notify(address,
                UUID.fromString(XIEN4_SERVICE),
                UUID.fromString(XIEN4_NOTIFY), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                        if (bytes.length == 11 && (bytes[3] & 0xff) == 10 && (bytes[4] & 0xff) == 2) {
                            int hight = (bytes[6] & 0xff) | (bytes[7] << 8 & 0xff00);
                            detectionData.setInit(true);
                            detectionData.setHighPressure(hight);
                            detectionData.setLowPressure(0);
                            detectionData.setPulse(0);
                            baseView.updateData(detectionData);
                        } else if (bytes.length == 9 && (bytes[3] & 0xff) == 73 && (bytes[4] & 0xff) == 3) {
                            int highPress = (bytes[6] & 0xff) + 30;
                            int lowPress = (bytes[7] & 0xff) + 30;
                            int pulse = bytes[5] & 0xff;

                            detectionData.setInit(false);
                            detectionData.setHighPressure(highPress);
                            detectionData.setLowPressure(lowPress);
                            detectionData.setPulse(pulse);
                            baseView.updateData(detectionData);
                        }
                    }

                    @Override
                    public void onResponse(int i) {
                    }
                });
    }

    private void handleSelf(String address) {
        BluetoothStore.getClient().notify(address,
                UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_NOTIFY), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                        int length = bytes.length;
                        switch (length) {
                            case 2:
                                isGetResult = false;
                                detectionData.setInit(true);
                                detectionData.setHighPressure((bytes[1] & 0xff));
                                detectionData.setLowPressure(0);
                                detectionData.setPulse(0);
                                baseView.updateData(detectionData);
                                break;
                            case 12:
                                if (!isGetResult) {
                                    isGetResult = true;
                                    detectionData.setInit(false);
                                    detectionData.setHighPressure((bytes[2] & 0xff));
                                    detectionData.setLowPressure((bytes[4] & 0xff));
                                    detectionData.setPulse((bytes[8] & 0xff));
                                    baseView.updateData(detectionData);
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onResponse(int i) {

                    }
                });
    }

    private void handleChaosi(String address) {
        //第一通道
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY1), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(int i) {
            }
        });

        //第二通道监听
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY2), new BleNotifyResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(int i) {
            }
        });
        //第三通道
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY3), new BleNotifyResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(int i) {
            }
        });
        //第四通道
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY), new BleNotifyResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes.length == 12) {
                    detectionData.setInit(false);
                    detectionData.setHighPressure((bytes[4] + bytes[5]));
                    detectionData.setLowPressure((bytes[6] + bytes[7]));
                    detectionData.setPulse((bytes[8] + bytes[9]));
                    baseView.updateData(detectionData);
                }
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
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(int i) {
                    }
                });
    }

    /**
     * 西恩手动开始测量
     */
    public void startXienMeasure() {
        if (xien2Presenter != null) {
            xien2Presenter.start();
            return;
        }
        if (!isConnected()) {
            baseView.updateState("请先连接设备");
            return;
        }
        BluetoothStore.getClient().write(targetAddress,
                UUID.fromString(XIEN4_SERVICE),
                UUID.fromString(XIEN4_WRITE), XIEN4_COMMOND, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                    }
                });
    }
}
