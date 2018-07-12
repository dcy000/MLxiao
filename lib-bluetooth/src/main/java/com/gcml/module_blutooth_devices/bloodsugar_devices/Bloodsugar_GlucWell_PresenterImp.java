package com.gcml.module_blutooth_devices.bloodsugar_devices;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.inuker.bluetooth.library.search.SearchResult;
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
    public Bloodsugar_GlucWell_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
        if (discoverType == IPresenter.DISCOVER_WITH_MAC) {
            BleManager.getInstance().connect(fragment.getThisContext(), targetAddress);
            BleManager.getInstance().setOnBleListener(this);
        }
    }

    @Override
    protected boolean isSelfDefined() {
        if (discoverType == IPresenter.DISCOVER_WITH_MAC) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean discoveredTargetDevice(SearchResult device) {
        Logg.e(Bloodsugar_GlucWell_PresenterImp.class, "发现目标设备");
        BleManager.getInstance().connect(baseContext, device.getAddress());
        BleManager.getInstance().setOnBleListener(this);
        return false;
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
                baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
                baseView.updateData("0.00");
                SPUtil.put(baseContext, SPUtil.SP_SAVE_BLOODSUGAR, targetName + "," + targetAddress);
                BleManager.getInstance().sendGetSnCommond();
                break;

            case BleConnectState.DISCONNECTED:// 已断开设备的蓝牙连接
                if (((Fragment) baseView).isAdded()) {
                    baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
                }
                if (!isDestroy) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    BleManager.getInstance().connect(baseContext, targetAddress);
                }
                break;

            case BleConnectState.CONNECT_FAILURE:// 蓝牙连接设备失败

                break;
        }
    }

    @Override
    public void onBleSnCallback(String s, String s1) {
        BleManager.getInstance().sendGetUnitCommond();
    }

    @Override
    public void onBleUnitCallback(String s, String s1) {
        BleManager.getInstance().sendSetTimeCommond();
    }

    @Override
    public void onBleTimeCallback(String s, int i) {
        BleManager.getInstance().sendRealtimeMeasureCommond();
    }

    @Override
    public void onBleRealTimeMeasureToastCallback(String s, int measureToastType) {
        switch (measureToastType) {
            case BleMeasureToastType.LAST_TIME_MEASURE:
                Logg.e(Bloodsugar_GlucWell_PresenterImp.class, "上次测量的记录: ");
                break;

            case BleMeasureToastType.INSERTED_TEST_STRIP:
                Logg.e(Bloodsugar_GlucWell_PresenterImp.class, "已插入试纸: ");
                break;

            case BleMeasureToastType.WAITING_COLLECTION_BLOOD:
                Logg.e(Bloodsugar_GlucWell_PresenterImp.class, "等待加血: ");
                break;

            case BleMeasureToastType.FINISH_COLLECTION_BLOOD:
                Logg.e(Bloodsugar_GlucWell_PresenterImp.class, "完成加血: ");
                break;

            case BleMeasureToastType.MEASURE_ERROR:
                Logg.e(Bloodsugar_GlucWell_PresenterImp.class, "测量异常: ");
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
        float result = baseGlucoseEntity.getValue();//+ baseGlucoseEntity.getMeasureUnit();
        Logg.e(Bloodsugar_GlucWell_PresenterImp.class, "onBleGlucoseDataCallback: 本次血糖：" + result);
        baseView.updateData(String.format("%.2f", result));

    }

    @Override
    public void onDestroy() {
        BleManager.getInstance().destroy(baseContext);
    }
}
