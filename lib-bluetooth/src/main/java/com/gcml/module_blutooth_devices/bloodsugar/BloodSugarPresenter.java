package com.gcml.module_blutooth_devices.bloodsugar;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.DeviceBrand;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.HashMap;
import java.util.UUID;

import timber.log.Timber;

public class BloodSugarPresenter extends BaseBluetooth {
    private static final String SELF_SERVICE = "00001000-0000-1000-8000-00805f9b34fb";
    private static final String SELF_NOTIFY = "00001002-0000-1000-8000-00805f9b34fb";
    private static final String SELF_WRITE = "00001001-0000-1000-8000-00805f9b34fb";
    private static final byte[] SELF_DATA_SUGAR_TO_WRITE = {0x5A, 0x0A, 0x03, 0x10, 0x05, 0x02, 0x0F, 0x21, 0x3B, (byte) 0xEB};

    private static final String THREE_SELF_SERVICE = "00001808-0000-1000-8000-00805f9b34fb";//主服务
    private static final String THREE_SELF_NOTIFY = "00002a18-0000-1000-8000-00805f9b34fb";
    DetectionData detectionData = new DetectionData();

    private BloodsugarGlucWellPresenter bloodsugarGlucWellPresenter;

    public BloodSugarPresenter(IBluetoothView owner) {
        this(owner, true);
    }

    public BloodSugarPresenter(IBluetoothView owner, boolean isAutoDiscovery) {
        super(owner);
        if (isAutoDiscovery) {
            startDiscovery(targetAddress);
        }
    }

    @Override
    protected void connectSuccessed(String name, String address) {

        detectionData.setInit(true);
        detectionData.setBloodSugar(0.0f);
        baseView.updateData(detectionData);
        BluetoothStore.instance.detection.setValue(detectionData);
        BluetoothStore.instance.detection.postValue(detectionData);
        if (name.startsWith("BLE-Glucowell")) {
            return;
        }
        if (name.startsWith("Bioland-BGM")) {
            handleSelf(address);
            return;
        }
        if (name.startsWith("BeneCheck")) {
            handleThreeInOne(address);
            return;
        }
        baseView.updateState("未兼容该设备:" + name + ":::" + address);
    }


    @Override
    protected boolean isSelfConnect(String name, String address) {
        if (name.startsWith("BLE-Glucowell")) {
            Timber.w("bt ---> connect isSelfConnect: name = %s, address = %s", name, address);
            bloodsugarGlucWellPresenter = new BloodsugarGlucWellPresenter(getActivity(), baseView, name, address);
            return true;
        }
        if (name.startsWith("Bioland-BGM")) {
            return false;
        }
        if (name.startsWith("BeneCheck")) {
            return false;
        }
        return super.isSelfConnect(name, address);
    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected(String address) {

    }

    @Override
    protected void saveSP(String sp) {
        SPUtil.put(BluetoothConstants.SP.SP_SAVE_BLOODSUGAR, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_BLOODSUGAR, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.BLOODSUGAR;
    }

    private void handleSelf(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_NOTIFY), new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] bytes) {
                        if (bytes.length >= 12) {
                            float sugar = ((float) (bytes[10] << 8) + (float) (bytes[9] & 0xff)) / 18;
                            detectionData.setInit(false);
                            detectionData.setBloodSugar(sugar);
                            Timber.w("bt ---> connect data: detectionData = %s", detectionData.getBloodSugar());
                            baseView.updateData(detectionData);
                            BluetoothStore.instance.detection.postValue(detectionData);
                        }
                    }

                    @Override
                    public void onResponse(int code) {

                    }
                });
        BluetoothStore.getClient().write(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_WRITE), SELF_DATA_SUGAR_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
        BluetoothStore.getClient().write(address, UUID.fromString(SELF_SERVICE),
                UUID.fromString(SELF_WRITE), SELF_DATA_SUGAR_TO_WRITE, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }

    private void handleThreeInOne(String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(THREE_SELF_SERVICE),
                UUID.fromString(THREE_SELF_NOTIFY), new BleNotifyResponse() {
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
            detectionData.setInit(false);
            detectionData.setBloodSugar(result);
            baseView.updateData(detectionData);
            Timber.w("bt ---> connect data: detectionData = %s", detectionData.getBloodSugar());
            BluetoothStore.instance.detection.postValue(detectionData);
        } else if (bytes[1] == 81) {//尿酸
            detectionData.setInit(false);
            detectionData.setBloodSugar(0.0f);
            detectionData.setUricAcid(result);
            detectionData.setCholesterol(0.0f);
            baseView.updateData(detectionData);
            Timber.w("bt ---> connect data: detectionData = %s", detectionData.getBloodSugar());
            BluetoothStore.instance.detection.postValue(detectionData);
        } else if (bytes[1] == 97) {//胆固醇
            detectionData.setInit(false);
            detectionData.setBloodSugar(0.0f);
            detectionData.setUricAcid(0.0f);
            detectionData.setCholesterol(result);
            baseView.updateData(detectionData);
            Timber.w("bt ---> connect data: detectionData = %s", detectionData.getBloodSugar());
            BluetoothStore.instance.detection.postValue(detectionData);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bloodsugarGlucWellPresenter != null) {
            bloodsugarGlucWellPresenter.onStop();
        }
    }
}
