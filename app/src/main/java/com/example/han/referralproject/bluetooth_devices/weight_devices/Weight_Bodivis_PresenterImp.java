package com.example.han.referralproject.bluetooth_devices.weight_devices;

import android.util.Log;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothServiceDetail;
import com.example.han.referralproject.bluetooth_devices.base.ClientManager;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.base.Logg;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

/**
 * 同方体重秤
 * 测试体重秤：
 * name:VScale
 * mac:7C:01:0A:73:1A:2A
 */
public class Weight_Bodivis_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "f433bd80-75b8-11e2-97d9-0002a5d5c51b";//主服务
    private static final String targetCharacteristicUUid = "1a2ea400-75b9-11e2-be05-0002a5d5c51b";
    private static final String writeUserInfoCharacteristicUUid = "29f11080-75b9-11e2-8bf6-0002a5d5c51b";//写入数据通道
    private static final String readUserinfoCharacteristicUUid = "23b4fec0-75b9-11e2-972a-0002a5d5c51b";//读取更详细信息通道
    private Weight_Fragment fragment;

    public Weight_Bodivis_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Weight_Fragment) fragment;
    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        super.discoveredTargetDevice(device);
        Logg.e(Weight_Bodivis_PresenterImp.class, "discoveredTargetDevice: ");
    }

    @Override
    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
        super.connectSuccessed(serviceDetails);
        fragment.updateState(fragment.getString(R.string.bluetooth_device_connected));
        fragment.updateData("0.00");
        if (serviceDetails != null && serviceDetails.size() > 0) {
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Weight_Bodivis_PresenterImp.class, "onNotify: " + ByteUtils.byteToString(bytes));
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
                                Logg.e(Weight_Bodivis_PresenterImp.class, "onNotify: " + weight + "Kg");
                                fragment.updateData(weight + "");
                                //TODO 可以向体重秤写入个人信息然后得到体脂，BIM等一系列更详细的信息
                            }
                        } else {// 详细信息

                        }
                    }
                }

                @Override
                public void onResponse(int i) {
                    Logg.e(Weight_Bodivis_PresenterImp.class, "onResponse: " + i);
                }
            });
        }
    }


    @Override
    protected void disConnected() {
        super.disConnected();
        Logg.e(Weight_Bodivis_PresenterImp.class, "disConnected: ");
        if (fragment.isAdded()) {
            fragment.updateState(fragment.getString(R.string.bluetooth_device_disconnected));
        }
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
}
