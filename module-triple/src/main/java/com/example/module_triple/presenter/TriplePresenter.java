package com.example.module_triple.presenter;

import android.arch.lifecycle.Lifecycle;
import android.text.TextUtils;

import com.example.module_triple.R;
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

public class TriplePresenter extends BaseBluetooth {
    private static final UUID targetServiceUUid = UUID.fromString("00001808-0000-1000-8000-00805f9b34fb");//主服务
    private static final UUID targetCharacteristicUUid = UUID.fromString("00002a18-0000-1000-8000-00805f9b34fb");
    private static final String[] TARGET_BLUETOOTH_NAMES = {"BeneCheck"};
    private String targetName;
    private boolean isMeasureBloodsugarFinished;
    private boolean isMeasureBUAFinished;
    private boolean isMeasureCholesterolFinished;
    private int sugarTime;

    public TriplePresenter(IBluetoothView owner, int sugarTime) {
        super(owner);
        this.sugarTime = sugarTime;
        DeviceBean entity = KVUtils.getEntity(BluetoothConstants.KEY_TRIPLE, DeviceBean.class);
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
        baseView.updateData("initialization");
        isMeasureBloodsugarFinished = false;
        isMeasureBUAFinished = false;
        isMeasureCholesterolFinished = false;

        DeviceBean deviceBean = new DeviceBean(targetName, address, DeviceType.DEVICE_TRIPLE);
        KVUtils.putEntity(BluetoothConstants.KEY_TRIPLE, deviceBean);

        //根据不同的蓝牙名称处理不同的数据
        if (targetName.contains("BeneCheck")) {
            self(address);
        } else {
            connectFailed();
        }
    }

    private void self(String address) {
        BluetoothStore.getClient().notify(address, targetServiceUUid, targetCharacteristicUUid, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                parseData(value);
            }

            @Override
            public void onResponse(int code) {

            }
        });
    }

    protected void parseData(byte[] bytes) {
        if (bytes.length < 13) {
            return;
        }
        int temp = ((bytes[11] & 0xff) << 8) + (bytes[10] & 0xff);
        int basic = (int) Math.pow(16, 3);
        int flag = temp / basic;
        int number = temp % basic;
        float result = (float) (number / Math.pow(10, 13 - flag));
        if (bytes[1] == 65) {//血糖
            String bloodsugar = String.format("%.1f", result);
            baseView.updateData("bloodsugar", bloodsugar);
            if (!isMeasureBloodsugarFinished) {
                isMeasureBloodsugarFinished = true;
                ArrayList<DetectionData> datas = new ArrayList<>();
                DetectionData sugarData = new DetectionData();
                sugarData.setDetectionType("1");
                sugarData.setSugarTime(sugarTime);
                sugarData.setBloodSugar(Float.parseFloat(bloodsugar));
                datas.add(sugarData);
                if (baseView.isUploadData()){
                    uploadData(datas);
                }
            }
        } else if (bytes[1] == 81) {//尿酸
            String bua = String.format("%.2f", result);
            baseView.updateData("bua", bua);
            if (!isMeasureBUAFinished) {
                isMeasureBUAFinished = true;
                ArrayList<DetectionData> datas = new ArrayList<>();
                DetectionData lithicAcidData = new DetectionData();
                lithicAcidData.setDetectionType("8");
                lithicAcidData.setUricAcid(Float.parseFloat(bua));
                datas.add(lithicAcidData);
                if (baseView.isUploadData()){
                    uploadData(datas);
                }
            }
        } else if (bytes[1] == 97) {//胆固醇
            String cholesterol = String.format("%.2f", result);
            baseView.updateData("cholesterol", cholesterol);
            if (!isMeasureCholesterolFinished) {
                isMeasureCholesterolFinished = true;
                ArrayList<DetectionData> datas = new ArrayList<>();
                DetectionData cholesterolData = new DetectionData();
                cholesterolData.setDetectionType("7");
                cholesterolData.setCholesterol(Float.parseFloat(cholesterol));
                datas.add(cholesterolData);
                if (baseView.isUploadData()){
                    uploadData(datas);
                }
            }
        }
    }

    private void uploadData(ArrayList<DetectionData> datas) {
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
