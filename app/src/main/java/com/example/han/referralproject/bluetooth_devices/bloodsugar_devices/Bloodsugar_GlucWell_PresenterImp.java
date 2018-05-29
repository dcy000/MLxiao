package com.example.han.referralproject.bluetooth_devices.bloodsugar_devices;

import android.text.TextUtils;
import android.util.Log;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.vivachek.ble.sdk.outer.BleManager;
import com.vivachek.ble.sdk.outer.constant.BleActionType;
import com.vivachek.ble.sdk.outer.constant.BleConnectState;
import com.vivachek.ble.sdk.outer.constant.BleMeasureToastType;
import com.vivachek.ble.sdk.outer.entity.BaseGlucoseEntity;
import com.vivachek.ble.sdk.outer.interfaces.OnBleListener;

import java.util.List;

public class Bloodsugar_GlucWell_PresenterImp extends BaseBluetoothPresenter implements OnBleListener {
    private final String TAG = Bloodsugar_GlucWell_PresenterImp.this.getClass().getSimpleName();
    private MainActivity mainActivity;

    public Bloodsugar_GlucWell_PresenterImp(IView activity, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        mainActivity = (MainActivity) activity;
    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        BleManager.getInstance().connect(mainActivity, device.getSearchResult().getAddress());
        BleManager.getInstance().setOnBleListener(this);
//        mainActivity.updateData("发现设备成功");
    }

    @Override
    public void onCheckBleCallback(int i) {

    }

    @Override
    public void onStartScan() {

    }

    @Override
    public void onBleScan(android.bluetooth.BluetoothDevice bluetoothDevice, int i, byte[] bytes) {

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
            case BleConnectState.CONNECT_SUCCESS:// 蓝牙连接设备成功

                break;

            case BleConnectState.DISCONNECTED:// 已断开设备的蓝牙连接
                break;

            case BleConnectState.CONNECT_FAILURE:// 蓝牙连接设备失败

                break;
        }
    }

    @Override
    public void onBleSnCallback(String s, String s1) {

    }

    @Override
    public void onBleUnitCallback(String s, String s1) {

    }

    @Override
    public void onBleTimeCallback(String s, int i) {

    }

    @Override
    public void onBleRealTimeMeasureToastCallback(String s, int measureToastType) {
        switch (measureToastType) {
            case BleMeasureToastType.LAST_TIME_MEASURE:
                Log.d(TAG, "上次测量的记录: ");
                break;

            case BleMeasureToastType.INSERTED_TEST_STRIP:
                Log.d(TAG, "已插入试纸: ");
                break;

            case BleMeasureToastType.WAITING_COLLECTION_BLOOD:
                Log.d(TAG, "等待加血: ");
                break;

            case BleMeasureToastType.FINISH_COLLECTION_BLOOD:
                Log.d(TAG, "完成加血: ");
                break;

            case BleMeasureToastType.MEASURE_ERROR:
                Log.d(TAG, "测量异常: ");
                break;
        }
    }

    @Override
    public void onBleGlucoseDataCallback(String s, List<BaseGlucoseEntity> list, int actionType) {
        if (list == null||list.isEmpty()) {
            return;
        }
        switch (actionType) {
            case BleActionType.ACTION_REALTIME://实时数据
                break;

            case BleActionType.ACTION_HISTORY://历史数据
                break;
        }

        BaseGlucoseEntity baseGlucoseEntity = list.get(0);
        String result = baseGlucoseEntity.getValue() + baseGlucoseEntity.getMeasureUnit();
        Log.e(TAG, "onBleGlucoseDataCallback: 本次血糖："+result );

    }
}
