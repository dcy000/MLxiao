package com.gcml.health.measure.ecg;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.creative.base.InputStreamReader;
import com.creative.base.OutputStreamSender;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;

import java.io.IOException;


public class ReceiveService extends Service {

    private ECGBluetooth myBluetooth;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        init();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver();
        myBluetooth.disConnected();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void init() {
        registerReceiver();// ??????????
        // ?????????????, init bluetooth operation
        myBluetooth = new ECGBluetooth(this, mHandler);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ECGBluetooth.BLUETOOTH_MSG_OPENING: {
                    sendBroadcast(BLU_ACTION_STATE_CHANGE, "OPENING");
                }
                break;
                case ECGBluetooth.BLUETOOTH_MSG_OPENINGFILE: {
                    sendBroadcast(BLU_ACTION_STATE_CHANGE, "OPENINGFILE");
                }
                break;
                case ECGBluetooth.BLUETOOTH_MSG_DISCOVERYING: {
                    sendBroadcast(BLU_ACTION_STATE_CHANGE, "DISCOVERYING");
                }
                break;
                case ECGBluetooth.BLUETOOTH_MSG_CONNECTING: {
                    sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTING");
                }
                break;
                case ECGBluetooth.BLUETOOTH_MSG_CONNECTED: {
                    sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTED");
                    //?????????
                    BluetoothSocket bluSocket = ECGBluetooth.bluSocket;
                    if (bluSocket != null) {
                        BluetoothDevice remoteDevice = bluSocket.getRemoteDevice();
                        if (remoteDevice != null) {
                            String name = remoteDevice.getName();
                            String address = remoteDevice.getAddress();
                            SPUtil.put(BluetoothConstants.SP.SP_SAVE_ECG, name + "," + address);
                        }
                    }
                    startRece(true);
                }
                break;
                case ECGBluetooth.BLUETOOTH_MSG_CONNECTFILE: {
                    sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTFILE");
                }
                case ECGBluetooth.BLUETOOTH_MSG_DISCOVERYED: {
                    sendBroadcast(BLU_ACTION_STATE_CHANGE, "DISCOVERYED");
                }
                break;
                default:
                    break;
            }
        }
    };

    private void startRece(boolean start) {
        if (start) {
            try {
                if (ECGBluetooth.bluSocket != null) {
                    String conDeviceName = ECGBluetooth.bluSocket
                            .getRemoteDevice().getName();
                    InputStreamReader reader = new InputStreamReader(
                            ECGBluetooth.bluSocket.getInputStream());
                    OutputStreamSender sender = new OutputStreamSender(
                            ECGBluetooth.bluSocket.getOutputStream());
                    StaticReceive.startReceive(this, conDeviceName, reader,
                            sender, mHandler);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            StaticReceive.StopReceive();
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        this.registerReceiver(bluetoothReceiver, filter);
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addDataScheme("file");
        this.registerReceiver(bluetoothReceiver, filter);

        filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction(BLU_ACTION_STARTDISCOVERY);
        filter.addAction(BLU_ACTION_STOPDISCOVERY);
        filter.addAction(BLU_ACTION_DISCONNECT);
        filter.addAction(ACTION_BLU_DISCONNECT);
        filter.addAction(ACTION_USER_EXIT);
        this.registerReceiver(bluetoothReceiver, filter);
    }

    private void unregisterReceiver() {
        this.unregisterReceiver(bluetoothReceiver);
    }

    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getExtras().getInt(
                        BluetoothAdapter.EXTRA_STATE);
                if (state == BluetoothAdapter.STATE_OFF) {
                    sendBroadcast(ACTION_BLUETOOH_OFF);
                } else if (state == BluetoothAdapter.STATE_ON) {
                    // sendBroadcast(ACTION_BLUETOOH_ON);
                }
            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                sendBroadcast(ACTION_MEDIA_MOUNTED);
            } else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                sendBroadcast(ACTION_MEDIA_EJECT);
            } else if (action.equals(Intent.ACTION_MEDIA_REMOVED)) {
                sendBroadcast(ACTION_MEDIA_EJECT);
            } else if (action.equals(BLU_ACTION_STARTDISCOVERY)) {
                int deviceName = intent.getExtras().getInt("device");
                String nameAddress = ((String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_ECG, ""));
                if (TextUtils.isEmpty(nameAddress)) {
                    myBluetooth.startDiscovery(deviceName);
                } else {
                    String[] split = nameAddress.split(",");
                    if (split.length == 2) {
                        String address = split[1];
                        if (TextUtils.isEmpty(address)) {
                            myBluetooth.startDiscovery(deviceName);
                        } else {
                            myBluetooth.connect(address);
                        }
                    }
                }
            } else if (action.equals(BLU_ACTION_STOPDISCOVERY)) {
                // ????????  stop device discovery
                myBluetooth.stopDiscovery();
            } else if (action.equals(BLU_ACTION_DISCONNECT)
                    || action.equals(ACTION_BLU_DISCONNECT)
                    || action.equals(ACTION_USER_EXIT)) {
                //close receive data
                startRece(false);
                //bluetooth disconnect
                myBluetooth.disConnected();
            }
        }
    };

    public static final String ACTION_BLUETOOH_OFF = "bluetooth_off";

    public static final String ACTION_BLUETOOH_ON = "bluetooth_on";

    public static final String ACTION_MEDIA_EJECT = "media_eject";

    public static final String ACTION_BLU_DISCONNECT = "disconnect";

    public static final String ACTION_MEDIA_MOUNTED = "media_mounted";

    public static final String BLU_ACTION_STATE_CHANGE = "state_change";

    public static final String BLU_ACTION_STARTDISCOVERY = "startDiscovery";

    public static final String BLU_ACTION_STOPDISCOVERY = "stopDiscovery";

    public static final String BLU_ACTION_DISCONNECT = "disconnect";

    public static final String ACTION_USER_EXIT = "userexit";

    private void sendBroadcast(String action) {
        Intent intent = new Intent(action);
        this.sendBroadcast(intent);
    }

    private void sendBroadcast(String... arg) {
        Intent i = new Intent(arg[0]);
        for (int j = 1; j < arg.length; j++) {
            i.putExtra("arg" + j, arg[j]);
        }
        this.sendBroadcast(i);
    }

}
