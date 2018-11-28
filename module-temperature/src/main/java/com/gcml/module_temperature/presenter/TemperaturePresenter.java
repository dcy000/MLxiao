package com.gcml.module_temperature.presenter;

import android.arch.lifecycle.Lifecycle;
import android.text.TextUtils;

import com.gcml.module_temperature.R;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * name:FSRKB-EWQ01
 * mac:94:E3:6D:54:1D:2D
 */
public class TemperaturePresenter extends BaseBluetooth {

    private static final UUID targetServiceUUid = UUID.fromString("00001910-0000-1000-8000-00805f9b34fb");
    private static final UUID targetCharacteristicUUid = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    private static final String[] TARGET_BLUETOOTH_NAMES = {"FSRKB-EWQ01"};
    private boolean isMeasureEnd = false;
    private String targetName;

    public TemperaturePresenter(IBluetoothView owner) {
        super(owner);

        DeviceBean entity = KVUtils.getEntity(BluetoothConstants.KEY_TEMPERATURE, DeviceBean.class);
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
        baseView.updateData("initialization", "0.00");
        isMeasureEnd = false;
        DeviceBean deviceBean = new DeviceBean(targetName, address, DeviceType.DEVICE_TEMPERATURE);
        KVUtils.putEntity(BluetoothConstants.KEY_TEMPERATURE, deviceBean);


        //根据不同的蓝牙名称处理不同的数据
        if (targetName.contains("FSRKB")) {
            zhiziyun(address);
        } else {
            connectFailed();
        }
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

    private void zhiziyun(String address) {
        BluetoothStore.getClient().notify(address, targetServiceUUid, targetCharacteristicUUid, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes.length > 6) {
                    int data = bytes[6] & 0xff;
                    if (data < 44) {
                        ToastUtils.showShort("测量温度不正常");
                        return;
                    }
                    double v = (data - 44.0) % 10;
                    double result = (30.0 + (data - 44) / 10 + v / 10);

                    if (!isMeasureEnd && result > 30) {
                        isMeasureEnd = true;
                        baseView.updateData(result + "");
                        if (baseView.isUploadData()) {
                            uploadData((float) result);
                        }
                    }

                }
            }

            @Override
            public void onResponse(int i) {

            }
        });
    }

    //detectionType (string, optional)检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
    private void uploadData(float temperature) {

        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData temperatureData = new DetectionData();
        temperatureData.setDetectionType("4");
        temperatureData.setTemperAture(temperature);
        datas.add(temperatureData);

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
}
