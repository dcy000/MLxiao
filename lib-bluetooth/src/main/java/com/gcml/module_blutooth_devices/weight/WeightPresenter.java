package com.gcml.module_blutooth_devices.weight;

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

public class WeightPresenter extends BaseBluetooth {
    private static final String TONGFANG_SERVICE = "f433bd80-75b8-11e2-97d9-0002a5d5c51b";//主服务
    private static final String TONGFANG_NOTIFY = "1a2ea400-75b9-11e2-be05-0002a5d5c51b";

    private String CHAOSI_SERVICE;
    private static final String CHAOSI_NOTIFY = "0000cd04-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_WRITE = "0000cd20-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY1 = "0000cd01-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY2 = "0000cd02-0000-1000-8000-00805f9b34fb";
    private static final String CHAOSI_NOTIFY3 = "0000cd03-0000-1000-8000-00805f9b34fb";
    private static final byte[] CHAOSI_PASSWORD = {(byte) 0xAA, 0x55, 0x04, (byte) 0xB1, 0x00, 0x00, (byte) 0xB5};//密码校验指令

    private static final String SELF_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";//主服务
    private static final String SELF_NOTIFY = "0000fff1-0000-1000-8000-00805f9b34fb";

    public WeightPresenter(IBluetoothView owner) {
        super(owner);
        startDiscovery(targetAddress);
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        baseView.updateData("initialization", "0.00");
        if (name.startsWith("VScale")) {
            handleTongfang(address);
            return;
        }
        if (name.startsWith("iChoice")) {
            handleChaosi(address);
            return;
        }
        if (name.startsWith("000FatScale01")) {
            handleSelf(address);
            return;
        }
        baseView.updateState("未兼容该设备:" + name + ":::" + address);
    }

    @Override
    protected boolean isSelfConnect(String name, String address) {
        if (name.startsWith("VScale")) {
            return false;
        }
        if (name.startsWith("iChoice")) {
            return false;
        }
        if (name.startsWith("000FatScale01")) {
            return false;
        }
        if (name.startsWith("dr01")) {
            new WeightSimaidePresenter(getActivity(), baseView, name, address);
            return true;
        }
        if (name.startsWith("SHHC-60F1")) {
            new WeightYikePresenter(getActivity(), baseView, name, address);
            return true;
        }
        if (name.startsWith("SENSSUN")) {
            new WeightXiangshanPresenter(getActivity(), baseView, name, address);
            return true;
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
        SPUtil.put(BluetoothConstants.SP.SP_SAVE_WEIGHT, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_WEIGHT, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.WEIGHT;
    }

    private void handleSelf(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_NOTIFY), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] bytes) {
                        if (bytes.length == 14 && (bytes[1] & 0xff) == 221) {
                            float weight = ((float) (bytes[2] << 8) + (float) (bytes[3] & 0xff)) / 10;
                            baseView.updateData("result", "result", String.format("%.2f", weight));

                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    private void handleTongfang(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(TONGFANG_SERVICE), UUID.fromString(TONGFANG_NOTIFY), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                float weight;
                if (bytes.length == 0)
                    return;
                int type = bytes[0] & 0xf0;//数据类型：0x10测体重
                if (type == 0x10) {// 测体重
                    weight = parseWeight(bytes);
                } else {
                    if (bytes.length > 4 && bytes[2] == 0 && bytes[3] == 0) {// 测脂肪
                        if (bytes.length > 5) {
                            int h = bytes[4] & 0xff;
                            int l = bytes[5] & 0xff;
                            weight = (float) ((h * 256 + l) / 10.0);
                            if (weight == 0)
                                return;
                            baseView.updateData(weight + "");
                            //TODO 可以向体重秤写入个人信息然后得到体脂，BIM等一系列更详细的信息
                        }
                    } else {// 详细信息

                    }
                }
            }

            @Override
            public void onResponse(int i) {
            }
        });
    }

    private float parseWeight(byte[] srcData) {
        if (srcData == null || srcData.length < 5) {
            return 0;
        }
        int[] srcIntData = byteArrayToIntArray(srcData);
        int acc = srcIntData[0] & 0xf;
        double weightAccuracy = Math.pow(10, acc);
        float testWeight = srcIntData[1] * 0x1000000 + srcIntData[2] * 0x10000 + srcIntData[3]
                * 0x100 + srcIntData[4];

        float weight = (float) (testWeight / weightAccuracy);

        return weight;

    }

    private int[] byteArrayToIntArray(byte[] srcData) {
        if (srcData == null) {
            return null;
        }

        int[] data = new int[srcData.length];

        for (int i = 0; i < srcData.length; i++) {
            data[i] = srcData[i] & 0xff;
        }
        return data;
    }


    private void handleChaosi(String address) {
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

        BluetoothStore.getClient().notify(address, UUID.fromString(CHAOSI_SERVICE), UUID.fromString(CHAOSI_NOTIFY), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes.length == 8) {
                    int h = bytes[6] & 0xff;
                    int l = bytes[5] & 0xff;
                    float weight = ((h << 8) + l) * 0.1f;
                    baseView.updateData(String.format("%.2f", weight));
                }
            }

            @Override
            public void onResponse(int i) {

            }
        });
    }
}
