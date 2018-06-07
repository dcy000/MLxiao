package com.example.han.referralproject.bluetooth_devices.bloodoxygen_devices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.contec.cms50dj_jar.DeviceCommand;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.base.Logg;
import com.example.han.referralproject.bluetooth_devices.base.WeakHandler;
import com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.ClassicBluetoothService;
import com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.IBluetoothDataCallback;
import com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.IClassicBluetoothCallBack;
import com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.MtBuf;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_CAN_READ_WRITE;
import static com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_CONNECTED;
import static com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_CONNECTING;
import static com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_CONNECT_FAIL;
import static com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_DISCONNECTED;
import static com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_LISTEN;
import static com.example.han.referralproject.bluetooth_devices.base.classic_bluetooth.ClassicBluetoothService.STATE_NONE;

/**
 * 康泰血氧仪（只有在记步几面蓝牙是打开的）
 * name:SpO2080971
 * mac:8C:DE:52:82:40:1C
 */
public class Bloodoxygen_Kangtai_PresenterImp extends BaseBluetoothPresenter {
    private Bloodoxygen_Fragment fragment;
    private BluetoothAdapter bluetoothAdapter;
    private MyBtReceiver btReceiver;
    private static BluetoothDevice targetDevice;
    private ClassicBluetoothService classicBluetoothService;
    private MtBuf mtBuf;
    private WeakHandler handler;

    public Bloodoxygen_Kangtai_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Bloodoxygen_Fragment) fragment;

        btReceiver = new MyBtReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.fragment.getActivity().registerReceiver(btReceiver, intentFilter);
        classicBluetoothService = new ClassicBluetoothService(serviceCallBack);
        mtBuf = new MtBuf(dataCall);
        handler = new WeakHandler();
    }

    @Override
    public void searchDevices() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
    }


    /**
     * 广播接受器
     */
    private class MyBtReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                ToastTool.showShort("开始搜索 ...");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                ToastTool.showShort("搜索结束");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                boolean addressEquals = device.getAddress().equals(discoverSetting.getTargetMac());
                String name = device.getName();
                boolean nameEquals = !TextUtils.isEmpty(name) && name.equals(discoverSetting.getTargetName());
                if (addressEquals || nameEquals) {
                    targetDevice = device;
                    if (classicBluetoothService != null)
                        classicBluetoothService.stop();
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
        public void call(String... datas) {
            Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "STATE_CONNECTED++++++call: 血氧：" + datas[0] + "脉搏：" + datas[1] + datas[2]);
            fragment.updateData(datas[0],datas[1]);
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
                    fragment.updateState(fragment.getString(R.string.bluetooth_device_connected));
                    fragment.updateData("0", "0");
                    LocalShared.getInstance(fragment.getContext()).setXueyangMac(lockedDevice.getSearchResult().getAddress());
                    break;
                case STATE_CONNECT_FAIL:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_CONNECT_FAIL");
                    break;
                case STATE_DISCONNECTED:
                    Logg.e(Bloodoxygen_Kangtai_PresenterImp.class, "call: STATE_DISCONNECTED");
                    classicBluetoothService.stop();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bluetoothAdapter.startDiscovery();
                        }
                    }, 5000);
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
        handler.removeCallbacksAndMessages(null);
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        classicBluetoothService.stop();
        fragment.getActivity().unregisterReceiver(btReceiver);
    }
}
