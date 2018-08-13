package com.gcml.module_blutooth_devices.bloodoxygen_devices;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.contec.cms50dj_jar.DeviceCommand;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.handler.WeakHandler;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.base.classic_bluetooth.ClassicBluetoothService;
import com.gcml.module_blutooth_devices.base.classic_bluetooth.IBluetoothDataCallback;
import com.gcml.module_blutooth_devices.base.classic_bluetooth.IClassicBluetoothCallBack;
import com.gcml.module_blutooth_devices.base.classic_bluetooth.MtBuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.gcml.module_blutooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_CAN_READ_WRITE;
import static com.gcml.module_blutooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_CONNECTED;
import static com.gcml.module_blutooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_CONNECTING;
import static com.gcml.module_blutooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_CONNECT_FAIL;
import static com.gcml.module_blutooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_DISCONNECTED;
import static com.gcml.module_blutooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_LISTEN;
import static com.gcml.module_blutooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_NONE;

/**
 * 康泰血氧仪（只有在记步几面蓝牙是打开的,目前思路，直接测 测完最后同步数据）
 * name:SpO2080971
 * mac:8C:DE:52:82:40:1C
 */
@SuppressLint("MissingPermission")
public class Bloodoxygen_Kangtai_PresenterImp extends BaseBluetoothPresenter {
    //    private IView fragment;
    private BluetoothAdapter bluetoothAdapter;
    private MyBtReceiver btReceiver;
    private static BluetoothDevice targetDevice;
    private ClassicBluetoothService classicBluetoothService;
    private MtBuf mtBuf;
    private WeakHandler handler;
//    private Context context;

    public Bloodoxygen_Kangtai_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
        btReceiver = new MyBtReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        fragment.getThisContext().registerReceiver(btReceiver, intentFilter);
        classicBluetoothService = new ClassicBluetoothService(serviceCallBack);
        mtBuf = new MtBuf(dataCall);
        handler = new WeakHandler();
        searchDevices();
    }

    @Override
    protected boolean isSelfDefined() {
        return true;
    }


    @Override
    public void searchDevices() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
        Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "开始搜索");
    }


    /**
     * 广播接受器
     */
    private class MyBtReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                ToastUtils.showShort("开始搜索 ...");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                ToastUtils.showShort("搜索结束");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, device.getAddress() + "--------------" + device.getName());
                boolean addressEquals = device.getAddress().equals(discoverSetting.getTargetMac());
                String name = device.getName();
                boolean nameEquals = !TextUtils.isEmpty(name) && name.equals(discoverSetting.getTargetName());
                if (addressEquals || nameEquals) {
                    targetDevice = device;
                    if (classicBluetoothService != null) {
                        classicBluetoothService.stop();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    classicBluetoothService.start();
                    classicBluetoothService.connect(targetDevice);
                }
            }
        }
    }

    private final IBluetoothDataCallback dataCall = new IBluetoothDataCallback() {
        @Override
        public void call(final String bloodoxygen, final String pluse, String date) {
            Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "STATE_CONNECTED++++++call: 血氧：" + bloodoxygen + "脉搏：" + pluse + date);
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        baseView.updateData(bloodoxygen, pluse);
                    }
                });
            }
        }
    };
    private final IClassicBluetoothCallBack serviceCallBack = new IClassicBluetoothCallBack() {
        @Override
        public void call(int state) {
            switch (state) {
                case STATE_NONE:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_NONE");
                    break;
                case STATE_LISTEN:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_LISTEN");
                    break;
                case STATE_CONNECTING:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_CONNECTING");
                    break;
                case STATE_CONNECTED:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_CONNECTED");
                    baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
                    SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN, targetDevice.getName() + "," + targetDevice.getAddress());
                    break;
                case STATE_CONNECT_FAIL:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_CONNECT_FAIL");
                    baseView.updateState("连接失败");
                    break;
                case STATE_DISCONNECTED:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_DISCONNECTED");
                    if (handler != null) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bluetoothAdapter.startDiscovery();
                            }
                        }, 5000);
                    }
                    break;
                case STATE_CAN_READ_WRITE:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_CAN_READ_WRITE");
                    classicBluetoothService.write(DeviceCommand.deviceConfirmCommand());
                    break;
            }
        }

        @Override
        public void writeData(int result, byte[] buffer, OutputStream outputStream) {
            try {
                mtBuf.write(buffer, result, outputStream);
            } catch (Exception e) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public void readData(InputStream inputStream) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        classicBluetoothService.stop();
        targetDevice = null;
        baseContext.unregisterReceiver(btReceiver);
    }
}
