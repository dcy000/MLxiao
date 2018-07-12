package com.gcml.module_blutooth_devices.ecg_devices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.borsam.ble.BorsamConfig;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.SPUtil;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 博声心电仪
 * mac:50:65:83:8C:2C:A1
 * name:WeCardio STD
 */
@SuppressLint("LongLogTag")
public class ECG_BoSheng_PresenterImp extends BaseBluetoothPresenter {
    //    private IView fragment;
    private static final String TAG = "ECG_BoSheng_PresenterImp";
    private BleDevice lockedDevice;
    private List<Integer> points;
    private TimeCount timeCount;
    private static boolean isMeasureEnd = false;

    public ECG_BoSheng_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
//        this.fragment = fragment;
        points = new ArrayList<>();
        timeCount = new TimeCount(30000, 1000, fragment);
        BleManager.getInstance().init((Application) fragment.getThisContext().getApplicationContext());
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(20 * 1000)//设置扫描超时
                .setDeviceName(true, BorsamConfig.deviceNames)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        searchDevices();
    }

    @Override
    protected boolean isSelfDefined() {
        return true;
    }

    @Override
    public void searchDevices() {

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Log.e(TAG, "onScanFinished: ");
            }

            @Override
            public void onScanStarted(boolean success) {
                Log.e(TAG, "onScanStarted: " + success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                String mac = bleDevice.getMac();
                if (mac.equals(targetAddress)) {
                    BleManager.getInstance().cancelScan();
                    connectDevice(mac);
                }
            }
        });
    }

    @Override
    public void connectDevice(String macAddress) {
        BleManager.getInstance().connect(macAddress, bleGattCallback);
    }

    private final BleGattCallback bleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            Logg.e(ECG_BoSheng_PresenterImp.class, "开始连接");
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            Logg.e(ECG_BoSheng_PresenterImp.class, "连接失败");
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            Logg.e(ECG_BoSheng_PresenterImp.class, "连接成功");
            connectSuccessed(bleDevice);
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            Logg.e(ECG_BoSheng_PresenterImp.class, "连接中断");
            if (baseView instanceof Activity) {
                baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
            } else if (baseView instanceof Fragment) {
                if (((Fragment) baseView).isAdded()) {
                    baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
                }
            }
            isMeasureEnd = true;
            timeCount.cancel();
            Logg.e(ECG_BoSheng_PresenterImp.class, Thread.currentThread().getName());
            if (!isDestroy) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connectDevice(device.getMac());
            }
        }
    };

    protected void connectSuccessed(BleDevice address) {
        isMeasureEnd = false;
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        SPUtil.put(baseContext, SPUtil.SP_SAVE_ECG, address.getName() + "," + address.getMac());
        lockedDevice = address;
        BleManager.getInstance().notify(address, BorsamConfig.COMMON_RECEIVE_ECG_SUUID.toString(),
                BorsamConfig.COMMON_RECEIVE_ECG_CUUID.toString(), new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        Logg.e(ECG_BoSheng_PresenterImp.class, "打开通道成功");
                        timeCount.start();
                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        Logg.e(ECG_BoSheng_PresenterImp.class, "打开通道失败");
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
//                        for (int i = 0; i < data.length; i += 2) {
//                            points.add(getValue(data[i], data[i + 1]) - 1800);
//                        }
//                        Logg.e(ECG_BoSheng_PresenterImp.class,data.length+"---"+ByteUtils.byteToString(data));
                        if (!isMeasureEnd) {
                            baseView.updateData(ByteUtils.byteToString(data));
                        }
                    }
                });
    }

    private int getValue(byte lowByte, byte highByte) {
        int v1 = (highByte & 255) << 8;
        int v2 = lowByte & 255;
        return v1 + v2;
    }

    static class TimeCount extends CountDownTimer {
        private IView fragment;

        TimeCount(long millisInFuture, long countDownInterval, IView fragment) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.fragment = fragment;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
//            StringBuffer buffer = new StringBuffer();
//            for (int i = 0; i < points.size(); i++) {
//                buffer.append(points.get(i) + ",");
//
//            }
//            Logg.e(ECG_BoSheng_PresenterImp.class, buffer.toString());
            isMeasureEnd = true;
            fragment.updateData("tip", "测量结束");

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            Logg.e(ECG_BoSheng_PresenterImp.class, millisUntilFinished / 1000 + "");
            fragment.updateData("tip", "距离测量结束还有" + millisUntilFinished / 1000 + "s");
        }
    }

    @Override
    public void onDestroy() {
        if (lockedDevice != null) {
            BleManager.getInstance().stopNotify(lockedDevice, BorsamConfig.COMMON_RECEIVE_ECG_SUUID.toString(),
                    BorsamConfig.COMMON_RECEIVE_ECG_CUUID.toString());
        }
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        isMeasureEnd = false;
        super.onDestroy();
    }
}
