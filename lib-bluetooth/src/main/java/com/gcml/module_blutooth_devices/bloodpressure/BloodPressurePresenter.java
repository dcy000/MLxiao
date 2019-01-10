package com.gcml.module_blutooth_devices.bloodpressure;

import android.annotation.SuppressLint;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Chaosi_PresenterImp;
import com.gcml.module_blutooth_devices.bluetooth.BaseBluetooth;
import com.gcml.module_blutooth_devices.bluetooth.BluetoothStore;
import com.gcml.module_blutooth_devices.bluetooth.DeviceBrand;
import com.gcml.module_blutooth_devices.bluetooth.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

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

    public BloodPressurePresenter(IBluetoothView owner) {
        super(owner);
        startDiscovery(targetAddress);
    }

    @Override
    protected void connectSuccessed(String name, String address) {

        baseView.updateData("0", "0", "0");
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
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
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
                            baseView.updateData((value[1] & 0xff) + "");
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
                            baseView.updateData((value[1] & 0xff) + "", (value[3] & 0xff) + "", (value[14] & 0xff) + "");
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
                            baseView.updateData(String.valueOf(hight));
                        } else if (bytes.length == 9 && (bytes[3] & 0xff) == 73 && (bytes[4] & 0xff) == 3) {
                            int highPress = (bytes[6] & 0xff) + 30;
                            int lowPress = (bytes[7] & 0xff) + 30;
                            int pulse = bytes[5] & 0xff;

                            baseView.updateData(highPress + "", lowPress + "", pulse + "");
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
                                baseView.updateData((bytes[1] & 0xff) + "");
                                break;
                            case 12:
                                if (!isGetResult) {
                                    isGetResult = true;
                                    baseView.updateData((bytes[2] & 0xff) + "", (bytes[4] & 0xff) + "", (bytes[8] & 0xff) + "");
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
                Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: " + ByteUtils.byteToString(bytes));
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(int i) {
                Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onResponse: cd01" + (i == 0 ? "成功" : "失败"));
            }
        });

        //第二通道监听
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY2), new BleNotifyResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify2: pass" + ByteUtils.byteToString(bytes));
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(int i) {
                Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onResponse: cd02" + (i == 0 ? "成功" : "失败"));
            }
        });
        //第三通道
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY3), new BleNotifyResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify3: pass" + ByteUtils.byteToString(bytes));
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(int i) {
                Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onResponse: cd03" + (i == 0 ? "成功" : "失败"));
            }
        });
        //第四通道
        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY), new BleNotifyResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: " + bytes.length);
                if (bytes.length == 12) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: 高压：" + (bytes[4] + bytes[5]));
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: 低压：" + (bytes[6] + bytes[7]));
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: 脉搏" + (bytes[8] + bytes[9]));
                    baseView.updateData((bytes[4] + bytes[5]) + "", (bytes[6] + bytes[7]) + "", (bytes[8] + bytes[9]) + "");
                }
            }

            @Override
            public void onResponse(int i) {
                Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onResponse: cd04" + (i == 0 ? "成功" : "失败"));
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
                        Logg.d(Bloodpressure_Chaosi_PresenterImp.class, "onResponseWrite: " + i);
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
