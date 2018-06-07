package com.example.han.referralproject.bluetooth_devices.bloodsugar_devices;

import android.text.TextUtils;
import android.util.Log;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.base.Logg;
import com.example.han.referralproject.util.LocalShared;
import com.vivachek.ble.sdk.outer.BleManager;
import com.vivachek.ble.sdk.outer.constant.BleActionType;
import com.vivachek.ble.sdk.outer.constant.BleConnectState;
import com.vivachek.ble.sdk.outer.constant.BleMeasureToastType;
import com.vivachek.ble.sdk.outer.entity.BaseGlucoseEntity;
import com.vivachek.ble.sdk.outer.interfaces.OnBleListener;

import java.util.List;

/**
 * 好糖血糖仪
 * name:BLE-Glucowell
 * mac:69:9A:E2:51:12:B5
 */
public class Bloodsugar_GlucWell_PresenterImp extends BaseBluetoothPresenter implements OnBleListener {
    private Bloodsugar_Fragment fragment;

    public Bloodsugar_GlucWell_PresenterImp(IView activity, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        fragment = (Bloodsugar_Fragment) activity;
    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        BleManager.getInstance().connect(fragment.getContext(), device.getSearchResult().getAddress());
        BleManager.getInstance().setOnBleListener(this);
        fragment.updateData("发现设备成功");
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
                fragment.updateState(fragment.getString(R.string.bluetooth_device_connected));
                fragment.updateData("0.00");
                LocalShared.getInstance(fragment.getContext()).setXuetangMac(lockedDevice.getSearchResult().getAddress());
                break;

            case BleConnectState.DISCONNECTED:// 已断开设备的蓝牙连接
                if (fragment.isAdded()) {
                    fragment.updateState(fragment.getString(R.string.bluetooth_device_disconnected));
                }
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
                Logg.d(Bloodsugar_GlucWell_PresenterImp.class, "上次测量的记录: ");
                break;

            case BleMeasureToastType.INSERTED_TEST_STRIP:
                Logg.d(Bloodsugar_GlucWell_PresenterImp.class, "已插入试纸: ");
                break;

            case BleMeasureToastType.WAITING_COLLECTION_BLOOD:
                Logg.d(Bloodsugar_GlucWell_PresenterImp.class, "等待加血: ");
                break;

            case BleMeasureToastType.FINISH_COLLECTION_BLOOD:
                Logg.d(Bloodsugar_GlucWell_PresenterImp.class, "完成加血: ");
                break;

            case BleMeasureToastType.MEASURE_ERROR:
                Logg.d(Bloodsugar_GlucWell_PresenterImp.class, "测量异常: ");
                break;
        }
    }

    @Override
    public void onBleGlucoseDataCallback(String s, List<BaseGlucoseEntity> list, int actionType) {
        if (list == null || list.isEmpty()) {
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
        Logg.e(Bloodsugar_GlucWell_PresenterImp.class, "onBleGlucoseDataCallback: 本次血糖：" + result);
        fragment.updateData(result);

    }

    @Override
    public void onDestroy() {
        BleManager.getInstance().destroy(fragment.getContext());
    }
}
