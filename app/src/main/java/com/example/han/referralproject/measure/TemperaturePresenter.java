package com.example.han.referralproject.measure;

import android.arch.lifecycle.LifecycleOwner;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.text.TextUtils;

import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.gzq.lib_bluetooth.BaseBluetooth;
import com.gzq.lib_bluetooth.BluetoothStore;
import com.gzq.lib_bluetooth.BluetoothType;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.util.List;
import java.util.UUID;

public class TemperaturePresenter extends BaseBluetooth {

    private TemperatureMeasureActivity context;
    private Handler handler;

    public TemperaturePresenter(LifecycleOwner owner) {
        super(owner);
        context = ((TemperatureMeasureActivity) owner);
        startDiscover();
    }

    public void startDiscover() {
        String wenduMac = LocalShared.getInstance(context).getWenduMac();
        start(BluetoothType.BLUETOOTH_TYPE_BLE, wenduMac, "FSRKB-EWQ01", "MEDXING-IRT");
    }

    @Override
    protected void noneFind() {

    }

    @Override
    protected void connectSuccessed(String address) {
        LocalShared.getInstance(context).setWenduMac(address);
        MLVoiceSynthetize.startSynthesize(context, "设备已连接", false);
        List<BluetoothDevice> connectedBluetoothLeDevices = BluetoothUtils.getConnectedBluetoothLeDevices();
        if (connectedBluetoothLeDevices != null && connectedBluetoothLeDevices.size() > 0) {
            BluetoothDevice bluetoothDevice = connectedBluetoothLeDevices.get(0);
            if (bluetoothDevice != null) {
                String name = bluetoothDevice.getName();
                if (!TextUtils.isEmpty(name)) {
                    switch (name) {
                        case "FSRKB-EWQ01":
                            BluetoothStore.getInstance().getClient().notify(address, UUID.fromString("00001910-0000-1000-8000-00805f9b34fb"),
                                    UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb"), new BleNotifyResponse() {
                                        @Override
                                        public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                                            if (bytes.length > 6) {
                                                int data = bytes[6] & 0xff;
                                                if (data < 44) {
                                                    ToastTool.showShort("测量温度不正常");
                                                    return;
                                                }
                                                double v = (data - 44.0) % 10;
                                                double result = (30.0 + (data - 44) / 10 + v / 10);
                                                context.updateData(result + "");
                                            }
                                        }

                                        @Override
                                        public void onResponse(int i) {

                                        }
                                    });
                            break;
                        case "MEDXING-IRT":

                            BluetoothStore.getInstance().getClient().notify(address,
                                    UUID.fromString("0000ffb0-0000-1000-8000-00805f9b34fb"),
                                    UUID.fromString("0000ffb2-0000-1000-8000-00805f9b34fb"), new BleNotifyResponse() {
                                        @Override
                                        public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                                            if (bytes.length == 4) {
                                                float result = ((float) (bytes[3] << 8) + (float) (bytes[2] & 0xff)) / 10;
                                                if (result < 50) {
                                                    context.updateData(result + "");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onResponse(int i) {

                                        }
                                    });

                            break;
                    }
                }
            }
        }

    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected() {
        handler = new Handler();
        if (context != null) {
            MLVoiceSynthetize.startSynthesize(context, "设备已断开", false);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isOnDestroy) {
                    connect(LocalShared.getInstance(context).getWenduMac());
                }
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        handler = null;
        context = null;
    }
}
