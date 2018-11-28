package com.example.module_blood_sugar.presenter;

import android.arch.lifecycle.Lifecycle;
import android.text.TextUtils;

import com.example.module_blood_sugar.R;
import com.example.module_blood_sugar.ui.BloodsugarAbnormalActivity;
import com.example.module_blood_sugar.ui.BloodsugarFragment;
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
 * name:Bioland-BGM
 * mac:
 */
public class BloodSugarPresenter extends BaseBluetooth {
    private static final UUID targetServiceUUid = UUID.fromString("00001000-0000-1000-8000-00805f9b34fb");
    private static final UUID targetCharacteristicUUid = UUID.fromString("00001002-0000-1000-8000-00805f9b34fb");
    private static final UUID targetWriteCharacteristicUUid = UUID.fromString("00001001-0000-1000-8000-00805f9b34fb");
    private static final byte[] DATA_SUGAR_TO_WRITE = {0x5A, 0x0A, 0x03, 0x10, 0x05, 0x02, 0x0F, 0x21, 0x3B, (byte) 0xEB};
    private boolean isMeasureEnd = false;
    private String targetName;
    private static final String[] TARGET_BLUETOOTH_NAMES = {"Bioland-BGM"};
    private int sugarTime;
    private String sugar;
    private ArrayList<DetectionData> datas;

    public BloodSugarPresenter(IBluetoothView owner, int sugarTime) {
        super(owner);
        this.sugarTime = sugarTime;
        DeviceBean entity = KVUtils.getEntity(BluetoothConstants.KEY_BLOOD_GLUCOSE_METER, DeviceBean.class);
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
        baseView.updateData("initialization", "0.0");
        isMeasureEnd = false;

        DeviceBean deviceBean = new DeviceBean(targetName, address, DeviceType.DEVICE_BLOOD_GLUCOSE_METER);
        KVUtils.putEntity(BluetoothConstants.KEY_BLOOD_GLUCOSE_METER, deviceBean);

        if (targetName.contains("Bioland")) {
            self(address);
        } else {
            connectFailed();
        }
    }

    private void self(String address) {
        BluetoothStore.getClient().notify(address, targetServiceUUid, targetCharacteristicUUid, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] bytes) {
                if (bytes.length >= 12) {
                    float result = ((float) (bytes[10] << 8) + (float) (bytes[9] & 0xff)) / 18;
                    sugar = String.format("%.1f", result);
                    baseView.updateData(sugar);
                    if (!isMeasureEnd) {
                        isMeasureEnd = true;
                        if (baseView.isUploadData()) {
                            checkData();
                        }
                    }
                }
            }

            @Override
            public void onResponse(int code) {

            }
        });
        BluetoothStore.getClient().write(address, targetServiceUUid, targetWriteCharacteristicUUid,
                DATA_SUGAR_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    private void checkData() {
        datas = new ArrayList<>();
        DetectionData data = new DetectionData();
        data.setDetectionType("1");
        data.setSugarTime(sugarTime);
        data.setBloodSugar(Float.parseFloat(sugar));
        datas.add(data);

        Box.getRetrofit(BluetoothAPI.class)
                .checkIsNormalData(Box.getUserId(), datas)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(owner))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        uploadData();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ((BloodsugarFragment) baseView).abnormalData();
                    }
                });
    }

    //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
    public void uploadData() {
        if (datas == null)
            return;
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
