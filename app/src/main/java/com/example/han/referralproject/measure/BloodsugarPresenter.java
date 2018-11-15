package com.example.han.referralproject.measure;

import android.arch.lifecycle.LifecycleOwner;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.example.han.referralproject.util.WeakHandler;
import com.gzq.lib_bluetooth.BaseBluetooth;
import com.gzq.lib_bluetooth.BluetoothStore;
import com.gzq.lib_bluetooth.BluetoothType;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.vivachek.ble.sdk.outer.BleManager;
import com.vivachek.ble.sdk.outer.constant.BleActionType;
import com.vivachek.ble.sdk.outer.constant.BleConnectState;
import com.vivachek.ble.sdk.outer.constant.BleMeasureToastType;
import com.vivachek.ble.sdk.outer.entity.BaseGlucoseEntity;
import com.vivachek.ble.sdk.outer.interfaces.OnBleListener;

import java.util.List;
import java.util.UUID;

public class BloodsugarPresenter extends BaseBluetooth implements OnBleListener {
    private final BloodsugarMeasureActivity context;
    private String targetName;
    private String targetAddress;
    private static final String targetServiceUUid = "00001000-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "00001002-0000-1000-8000-00805f9b34fb";
    private static final String targetWriteCharacteristicUUid = "00001001-0000-1000-8000-00805f9b34fb";
    private static final byte[] DATA_SUGAR_TO_WRITE = {0x5A, 0x0A, 0x03, 0x10, 0x05, 0x02, 0x0F, 0x21, 0x3B, (byte) 0xEB};

    public BloodsugarPresenter(LifecycleOwner owner) {
        super(owner);
        context = ((BloodsugarMeasureActivity) owner);

        startDiscover();
    }

    public void startDiscover() {
        String xuetangMacAndName = LocalShared.getInstance(context).getXuetangMac();
        if (!TextUtils.isEmpty(xuetangMacAndName)) {
            String[] split = xuetangMacAndName.split(",");
            if (split.length == 2) {
                targetName = split[0];
                targetAddress = split[1];
                if (!TextUtils.isEmpty(targetAddress)) {
                    connect(targetAddress);
                } else {
                    start(BluetoothType.BLUETOOTH_TYPE_BLE, targetAddress, "Bioland-BGM", "BLE-Glucowell");
                    start(BluetoothType.BLUETOOTH_TYPE_CLASSIC, targetAddress, "Bioland-BGM", "BLE-Glucowell");
                }
            }
        } else {
            start(BluetoothType.BLUETOOTH_TYPE_BLE, targetAddress, "Bioland-BGM", "BLE-Glucowell");
            start(BluetoothType.BLUETOOTH_TYPE_CLASSIC, targetAddress, "Bioland-BGM", "BLE-Glucowell");
        }
    }

    @Override
    protected void noneFind() {

    }

    @Override
    protected void connectSuccessed(String address) {
        ToastTool.showShort("设备已连接");
        MLVoiceSynthetize.startSynthesize(context, "设备已连接", false);
        LocalShared.getInstance(context).setXuetangMac(targetName + "," + address);
        context.updateData("initialization", "0.0");
        BluetoothStore.getInstance().getClient().notify(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] bytes) {
                        if (bytes.length >= 12) {
                            float sugar = ((float) (bytes[10] << 8) + (float) (bytes[9] & 0xff)) / 18;
                            context.updateData(String.format("%.1f", sugar));
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
        BluetoothStore.getInstance().getClient().write(address, UUID.fromString(targetServiceUUid),
                UUID.fromString(targetWriteCharacteristicUUid), DATA_SUGAR_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected() {

    }

    @Override
    protected boolean isSelfConnect() {
        return true;
    }

    private static final String TAG = "BloodsugarPresenter";

    @Override
    protected void targetDevice(BluetoothDevice device) {
        targetName = device.getName();
        targetAddress = device.getAddress();
        Log.e(TAG, "targetDevice: 》》》》》》》》》" + targetName + "<<<<<<<<<<<>>>>>>>>>>" + targetAddress);

        if (!TextUtils.isEmpty(targetName)) {
            if (targetName.contains("Glucowell")) {
                //好糖的
                BleManager.getInstance().connect(context, targetAddress);
                BleManager.getInstance().setOnBleListener(this);
            }

            if (targetName.contains("Bioland")) {
                connect(targetAddress);
            }
        }
    }

    @Override
    public void onCheckBleCallback(int i) {

    }

    @Override
    public void onStartScan() {

    }

    @Override
    public void onBleScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {

    }

    @Override
    public void onBleScanFailure(int i) {

    }

    @Override
    public void onStoppedScan() {

    }

    @Override
    public void onBleConnectCallback(String s, int connectState) {
        if (TextUtils.isEmpty(s))
            return;
        switch (connectState) {
            case BleConnectState.CONNECT_SUCCESS:
                // 蓝牙连接设备成功
                ToastTool.showShort("设备已连接");
                MLVoiceSynthetize.startSynthesize(context, "设备已连接", false);
                context.updateData("initialization", "0.00");
                LocalShared.getInstance(context).setXuetangMac(targetName + "," + targetAddress);
                BleManager.getInstance().sendGetSnCommond();
                break;

            case BleConnectState.DISCONNECTED:
                // 已断开设备的蓝牙连接
                ToastTool.showShort("设备已断开");
                MLVoiceSynthetize.startSynthesize(context, "设备已断开", false);
                new WeakHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BleManager.getInstance().connect(context, targetAddress);
                    }
                }, 2000);

                break;

            case BleConnectState.CONNECT_FAILURE:
                // 蓝牙连接设备失败
                break;
            default:
                break;
        }
    }


    @Override
    public void onBleSnCallback(String s, String s1) {
        BleManager.getInstance().sendGetUnitCommond();
    }

    @Override
    public void onBleUnitCallback(String s, String s1) {
        BleManager.getInstance().sendSetTimeCommond();
    }

    @Override
    public void onBleTimeCallback(String s, int i) {
        BleManager.getInstance().sendRealtimeMeasureCommond();
    }

    @Override
    public void onBleRealTimeMeasureToastCallback(String s, int measureToastType) {
        switch (measureToastType) {
            case BleMeasureToastType.LAST_TIME_MEASURE:
                break;

            case BleMeasureToastType.INSERTED_TEST_STRIP:
                break;

            case BleMeasureToastType.WAITING_COLLECTION_BLOOD:
                break;

            case BleMeasureToastType.FINISH_COLLECTION_BLOOD:
                break;

            case BleMeasureToastType.MEASURE_ERROR:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBleGlucoseDataCallback(String s, List<BaseGlucoseEntity> list, int actionType) {
        if (list == null || list.isEmpty()) {
            return;
        }
        switch (actionType) {
            case BleActionType.ACTION_REALTIME:
                //实时数据
                break;

            case BleActionType.ACTION_HISTORY:
                //历史数据
                break;
            default:
                break;
        }

        BaseGlucoseEntity baseGlucoseEntity = list.get(0);
        float result = baseGlucoseEntity.getValue();//+ baseGlucoseEntity.getMeasureUnit();
        context.updateData(String.format("%.2f", result));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().destroy(context);
    }
}
