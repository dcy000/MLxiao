package com.gcml.module_blutooth_devices.ecg;

import android.arch.lifecycle.MutableLiveData;

import com.borsam.ble.BorsamConfig;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.DeviceBrand;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ECGPresenter extends BaseBluetooth {
    public MutableLiveData<String> ecgBrand = new MutableLiveData<>();
    private List<byte[]> bytesResult = new ArrayList<>();
    DetectionData detectionData = new DetectionData();

    public ECGPresenter(IBluetoothView owner) {
        this(owner, true);
    }

    public ECGPresenter(IBluetoothView owner, boolean isAutoDiscovery) {
        super(owner);
        if (isAutoDiscovery) {
            startDiscovery(targetAddress);
        }
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        if (name.startsWith("A12-B")) {
            ecgBrand.postValue("A12-B");
            new ChaosiECGPresenter(getActivity(), baseView, name, address);
            return;
        }
        if (name.startsWith("WeCardio")) {
            handleBosheng(address);
            return;
        }
        baseView.updateState("未兼容该设备:" + name + ":::" + address);
    }

//    @Override
//    protected boolean isSelfConnect(String name, String address) {
//        if (name.startsWith("WeCardio")) {
//            ecgBrand.postValue("WeCardio");
//            new BoShengECGPresenter(getActivity(), baseView, name, address);
//            return true;
//        }
//        return super.isSelfConnect(name, address);
//    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected(String address) {

    }

    @Override
    protected void saveSP(String sp) {
        SPUtil.put(BluetoothConstants.SP.SP_SAVE_ECG, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_ECG, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.ECG;
    }

    private void handleBosheng(String address) {
        BluetoothStore.getClient().notify(address, BorsamConfig.COMMON_RECEIVE_ECG_SUUID,
                BorsamConfig.COMMON_RECEIVE_ECG_CUUID, new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        bytesResult.add(value);
                        detectionData.setInit(false);
                        detectionData.setEcgData(value);
                        baseView.updateData(detectionData);
                        BluetoothStore.instance.detection.postValue(detectionData);
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
    }
}
