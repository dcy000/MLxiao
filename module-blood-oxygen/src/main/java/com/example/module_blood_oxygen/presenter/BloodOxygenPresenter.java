package com.example.module_blood_oxygen.presenter;

import android.arch.lifecycle.Lifecycle;
import android.text.TextUtils;

import com.example.module_blood_oxygen.R;
import com.gzq.lib_bluetooth.BaseBluetooth;
import com.gzq.lib_bluetooth.BluetoothConstants;
import com.gzq.lib_bluetooth.BluetoothStore;
import com.gzq.lib_bluetooth.BluetoothType;
import com.gzq.lib_bluetooth.DeviceBean;
import com.gzq.lib_bluetooth.DeviceType;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_bluetooth.bean.DetectionData;
import com.gzq.lib_bluetooth.bean.DetectionResult;
import com.gzq.lib_bluetooth.service.BluetoothAPI;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_core.utils.WeakHandler;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * name:POD
 * mac:50:65:83:9A:56:DF
 */
public class BloodOxygenPresenter extends BaseBluetooth {
    private static final UUID targetServiceUUid = UUID.fromString("0000ffb0-0000-1000-8000-00805f9b34fb");
    private static final UUID targetCharacteristicUUid = UUID.fromString("0000ffb2-0000-1000-8000-00805f9b34fb");
    private static final UUID targetWriteCharacteristicUUid = UUID.fromString("0000ffb2-0000-1000-8000-00805f9b34fb");
    private static final byte[] DATA_OXYGEN_TO_WRITE = {(byte) 0xAA, 0x55, 0x0F, 0x03, (byte) 0x84, 0x01, (byte) 0xE0};
    private boolean isMeasureEnd = false;
    private String targetName;
    private static final String[] TARGET_BLUETOOTH_NAMES = {"POD"};

    public BloodOxygenPresenter(IBluetoothView owner) {
        super(owner);

        DeviceBean entity = KVUtils.getEntity(BluetoothConstants.KEY_OXIMETER, DeviceBean.class);
        if (entity == null) {
            start(BluetoothType.BLUETOOTH_TYPE_BLE, null, TARGET_BLUETOOTH_NAMES);
        } else {
            start(BluetoothType.BLUETOOTH_TYPE_BLE, entity.getAddress(), targetName = entity.getName());
        }
    }

    @Override
    protected void noneFind() {
        baseView.updateState(Box.getString(R.string.bluetooth_undiscoveried));
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        if (!TextUtils.isEmpty(name)) {
            targetName = name;
        }
        baseView.updateState(Box.getString(R.string.bluetooth_connected));
        baseView.updateData("initialization", "0", "0");
        isMeasureEnd = false;

        DeviceBean deviceBean = new DeviceBean(targetName, address, DeviceType.DEVICE_OXIMETER);
        KVUtils.putEntity(BluetoothConstants.KEY_OXIMETER, deviceBean);

        if (targetName.contains("POD")) {
            self(address);
        } else {
            connectFailed();
        }
    }

    private void self(String address) {
        BluetoothStore.getClient().notify(address, targetServiceUUid, targetCharacteristicUUid, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                if (value.length == 12) {
                    baseView.updateData(value[5] + "", value[6] + "");
                    if (!isMeasureEnd) {
                        isMeasureEnd = true;
                        if (baseView.isUploadData()){
                            uploadData(value[5], value[6]);
                        }
                    }
                }
            }

            @Override
            public void onResponse(int code) {

            }
        });

        BluetoothStore.getClient().write(address, targetServiceUUid, targetWriteCharacteristicUUid,
                DATA_OXYGEN_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
    private void uploadData(int oxygen, int pulse) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData dataPulse = new DetectionData();
        pressureData.setDetectionType("6");
        pressureData.setBloodOxygen((float) oxygen);
        dataPulse.setDetectionType("9");
        dataPulse.setPulse(pulse);
        datas.add(pressureData);
        datas.add(dataPulse);

        Box.getRetrofit(BluetoothAPI.class)
                .postMeasureData(Box.getUserId(), datas)
                .compose(RxUtils.<List<DetectionResult>>httpResponseTransformer())
                .as(RxUtils.<List<DetectionResult>>autoDisposeConverter(owner, Lifecycle.Event.ON_STOP))
                .subscribe(new CommonObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> detectionResults) {
                        ToastUtils.showShort(Box.getString(R.string.upload_data_success));
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        ToastUtils.showShort(ex.message);
                    }
                });
    }

    @Override
    protected void connectFailed() {
        baseView.updateState(Box.getString(R.string.bluetooth_failed));
    }

    @Override
    protected void disConnected(final String address) {
        baseView.updateState(Box.getString(R.string.bluetooth_disconnected));
        //2秒之后进行重连
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(address)) {
                    connect(address);
                } else {
                    start(BluetoothType.BLUETOOTH_TYPE_BLE, null, TARGET_BLUETOOTH_NAMES);
                }
            }
        }, 2000);
    }
}
