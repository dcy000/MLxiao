package com.example.module_weight.presenter;

import android.arch.lifecycle.Lifecycle;
import android.text.TextUtils;

import com.example.module_weight.R;
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

public class WeightPresenter extends BaseBluetooth {
    private static final UUID targetServiceUUid = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");//主服务
    private static final UUID targetCharacteristicUUid = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    private boolean isMeasureEnd = false;
    private String targetName;
    private static final String[] TARGET_BLUETOOTH_NAMES = {"000FatScale01"};

    public WeightPresenter(IBluetoothView owner) {
        super(owner);

        DeviceBean entity = KVUtils.getEntity(BluetoothConstants.KEY_WEIGHING_SCALE, DeviceBean.class);
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

        DeviceBean deviceBean = new DeviceBean(targetName, address, DeviceType.DEVICE_WEIGHING_SCALE);
        KVUtils.putEntity(BluetoothConstants.KEY_WEIGHING_SCALE, deviceBean);

        if (targetName.contains("000FatScale01")) {
            self(address);
        } else {
            connectFailed();
        }
    }

    private void self(String address) {
        BluetoothStore.getClient().notify(address, targetServiceUUid, targetCharacteristicUUid, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] bytes) {
                if (bytes.length == 14 && (bytes[1] & 0xff) == 221) {
                    float weight = ((float) (bytes[2] << 8) + (float) (bytes[3] & 0xff)) / 10;
                    String result = String.format("%.2f", weight);
                    baseView.updateData("result", "result", result);
                    if (!isMeasureEnd) {
                        isMeasureEnd = true;
                        if (baseView.isUploadData()){
                            uploadData(result);
                        }
                    }
                }
            }

            @Override
            public void onResponse(int code) {

            }
        });
    }

    //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
    private void uploadData(String weight) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData data = new DetectionData();
        data.setDetectionType("3");
        data.setWeight(Float.parseFloat(weight));
        datas.add(data);

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
