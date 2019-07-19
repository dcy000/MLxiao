package com.gcml.module_blutooth_devices.weight;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.math.BigDecimal;

import senssun.blelib.device.scale.cloudblelib.BleCloudProtocolUtils;
import senssun.blelib.scan.BleScan;
import timber.log.Timber;

public class WeightXiangshanPresenter implements LifecycleObserver {
    private SupportActivity activity;
    private IBluetoothView baseView;
    private String name;
    private String address;
    private final BleScan bleScan;
    private final BleCloudProtocolUtils bleCloudProtocolUtils;
    DetectionData detectionData = new DetectionData();

    @SuppressLint("RestrictedApi")
    public WeightXiangshanPresenter(SupportActivity activity, IBluetoothView baseView, String name, String address) {
        this.activity = activity;
        this.baseView = baseView;
        this.name = name;
        this.address = address;
        this.activity.getLifecycle().addObserver(this);
        bleCloudProtocolUtils = BleCloudProtocolUtils.getInstance(activity);
        bleScan = new BleScan();
        bleScan.Create(activity);
        connect();
    }

    private void connect() {
        Timber.w("bt ---> connect isSelfConnect: name = %s, address = %s", name, address);
        bleCloudProtocolUtils.setOnConnectState(new BleCloudProtocolUtils.OnConnectState() {
            @Override
            public void OnState(boolean b) {
                if (b) {
                    baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_connected));
                    baseView.connectSuccess(BluetoothUtils.getRemoteDevice(address), name);
                    detectionData.setInit(true);
                    detectionData.setWeightOver(false);
                    detectionData.setWeight(0.0f);
                    baseView.updateData(detectionData);
                    Timber.w("bt ---> connect isSelfConnect data: detectionData = %s", detectionData.getWeight());
                    BluetoothStore.instance.detection.setValue(detectionData);
                    BluetoothStore.instance.detection.postValue(detectionData);
                    SPUtil.put(BluetoothConstants.SP.SP_SAVE_WEIGHT, name + "," + address);
                } else {
                    Timber.w("bt ---> connect isSelfConnect disconnected");
                    if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                        baseView.disConnected();
                        baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_disconnected));
                    } else if (baseView instanceof SupportActivity) {
                        baseView.disConnected();
                        baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_disconnected));
                    }
                }
            }
        });

        bleCloudProtocolUtils.setOnDisplayDATA(new BleCloudProtocolUtils.OnDisplayDATA() {
            @Override
            public void OnDATA(String data) {
                Timber.w("bt ---> connect isSelfConnect data: data = %s", data);
                String[] strdata = data.split("-");
                switch (strdata[5]) {
                    case "03":
                        switch (strdata[6]) {
                            case "80":
                                if (strdata[12].equals("A0")) {
                                    String tmpNum = strdata[7] + strdata[8];
                                    detectionData.setInit(false);
                                    detectionData.setWeightOver(false);
                                    detectionData.setWeight(Integer.valueOf(tmpNum, 16) / 10f);
                                    Timber.w("bt ---> connect isSelfConnect data: detectionData = %s", detectionData.getWeight());
                                    baseView.updateData(detectionData);
                                    BluetoothStore.instance.detection.postValue(detectionData);
                                } else {
                                    String tmpNum = strdata[7] + strdata[8];
                                    detectionData.setInit(false);
                                    detectionData.setWeightOver(true);
                                    detectionData.setWeight(Integer.valueOf(tmpNum, 16) / 10f);
                                    Timber.w("bt ---> connect isSelfConnect data: detectionData = %s, data[12] = %s", detectionData.getWeight(), strdata[12]);
                                    baseView.updateData(detectionData);
                                    BluetoothStore.instance.detection.postValue(detectionData);
                                }
                                break;
                            case "82":
                                switch (strdata[7]) {
                                    case "00": {
                                        Timber.w("bt ---> connect isSelfConnect data: no handle");
                                        float eigenvalue = new BigDecimal(Integer.valueOf(strdata[14] + strdata[15], 16)).floatValue();
                                        float weight = new BigDecimal(Integer.valueOf(strdata[10] + strdata[11], 16) / 10f).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                                        //TODO:请求香山的接口 获取更加详细的健康分析数据
                                    }
                                    break;
                                }

                        }
                        break;
                }
            }
        });
        bleCloudProtocolUtils.Connect(address);
    }

    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (bleCloudProtocolUtils != null) {
            Timber.w("bt ---> connect isSelfConnect disconnect");
            try {
                bleCloudProtocolUtils.Disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (activity != null) {
            activity.getLifecycle().removeObserver(this);
        }
        activity = null;
    }

}
