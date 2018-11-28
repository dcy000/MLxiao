package com.example.module_blood_pressure.presenter;

import android.arch.lifecycle.Lifecycle;
import android.text.TextUtils;

import com.example.module_blood_pressure.R;
import com.example.module_blood_pressure.ui.BloodpressureFragment;
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
 * name:eBlood-Pressure
 * mac:F4:5E:AB:0D:81:26
 */
public class BloodpressurePresenter extends BaseBluetooth {
    private static final UUID targetServiceUUid = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    private static final UUID targetCharacteristicUUid = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");
    private static final String[] TARGET_BLUETOOTH_NAMES = {"eBlood-Pressure"};
    private boolean isGetResult = false;
    private boolean isMeasureEnd = false;
    private String targetName;
    private ArrayList<DetectionData> datas;
    private int highPressure;
    private int lowPressure;
    private int pulse;

    public BloodpressurePresenter(IBluetoothView owner) {
        super(owner);

        DeviceBean entity = KVUtils.getEntity(BluetoothConstants.KEY_SPHYGMOMANOMETER, DeviceBean.class);
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
        baseView.updateData("0", "0", "0");
        isMeasureEnd = false;

        DeviceBean deviceBean = new DeviceBean(targetName, address, DeviceType.DEVICE_SPHYGMOMANOMETER);
        KVUtils.putEntity(BluetoothConstants.KEY_SPHYGMOMANOMETER, deviceBean);

        if (targetName.contains("eBlood")) {
            self(address);
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

    /**
     * 小E机器人自带的血压计
     *
     * @param address
     */
    private void self(String address) {
        BluetoothStore.getClient().notify(address, targetServiceUUid, targetCharacteristicUUid, new BleNotifyResponse() {
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
                            highPressure = (bytes[2] & 0xff);
                            lowPressure = (bytes[4] & 0xff);
                            pulse = (bytes[8] & 0xff);
                            baseView.updateData(highPressure + "", lowPressure + "", pulse + "");
                            if (!isMeasureEnd) {
                                isMeasureEnd = true;
                                if (baseView.isUploadData()) {
                                    checkData();
                                }
                            }
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

    private void checkData() {
        datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData dataPulse = new DetectionData();
        pressureData.setDetectionType("0");
        pressureData.setHighPressure(highPressure);
        pressureData.setLowPressure(lowPressure);
        dataPulse.setDetectionType("9");
        dataPulse.setPulse(pulse);
        datas.add(pressureData);
        datas.add(dataPulse);
        Box.getRetrofit(BluetoothAPI.class)
                .checkIsNormalData(Box.getUserId(), datas)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(owner, Lifecycle.Event.ON_STOP))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        uploadData();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ((BloodpressureFragment) baseView).abnormalData();
                    }
                });


    }

    //detectionType (string, optional)检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
    public void uploadData() {
        if (datas == null) {
            return;
        }
        Box.getRetrofit(BluetoothAPI.class)
                .postMeasureData(Box.getUserId(), datas)
                .compose(RxUtils.<List<DetectionResult>>httpResponseTransformer())
                .as(RxUtils.<List<DetectionResult>>autoDisposeConverter(owner, Lifecycle.Event.ON_STOP))
                .subscribe(new CommonObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> detectionResults) {
                        ToastUtils.showShort(Box.getString(R.string.upload_data_success));
                        DetectionResult detectionResult = detectionResults.get(0);
                        if (detectionResult != null) {
                            ((BloodpressureFragment) baseView).normalData(detectionResult.getDiagnose(),
                                    detectionResult.getScore(), highPressure, lowPressure, detectionResult.getResult());
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        ToastUtils.showShort(ex.message);
                    }
                });
    }
}
