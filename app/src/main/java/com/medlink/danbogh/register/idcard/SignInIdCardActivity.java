package com.medlink.danbogh.register.idcard;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.han.referralproject.R;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.bt.BtReadClient;
import com.kaer.sdk.bt.OnBluetoothListener;

import java.lang.reflect.Method;
import java.util.Set;

/**
 *
 */
public class SignInIdCardActivity extends AppCompatActivity {

    private static final String TAG = "Bluetooth";
    private static final String FILTER = "KT8000";
    private static final int PROTOCOL_TYPE = 0;

    private BluetoothAdapter bluetoothAdapter;
    private BtReadClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity_id_card);
        registerReceiver();
        client = BtReadClient.getInstance();
        client.setBluetoothListener(onBluetoothListener);
        btHandler().postDelayed(oneShutRunnable, 1000);
    }

    private long startTime;

    private Runnable oneShutRunnable = new Runnable() {
        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            if (bluetoothAdapter == null) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
            initDevice();
        }
    };

    private boolean initializing;

    private void initDevice() {
        if (initializing) {
            return;
        }
        initializing = true;
        if (targetDevice == null) {
            if (!findBoundTargetDevice()) {
                findTargetDevice();
            } else {
                Log.i(TAG, "Target Device Bound: named start with " + FILTER);
                onDeviceInitialized();
            }
        } else {
            Log.i(TAG, "Target Device Ready: named start with " + FILTER);
            onDeviceInitialized();
        }
    }

    private void onDeviceInitialized() {
        initializing = false;
        String address = targetDevice == null ? "targetDevice == null" : targetDevice.getAddress();
        Log.i(TAG, "Found Device: " + address);
        if (targetDevice == null) {
            onDeviceNotFound();
            return;
        }
        btHandler().post(readRunnable);
    }


    private void findTargetDevice() {
        if (bluetoothAdapter == null) {
            return;
        }
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }


    private boolean ensureDeviceConnected() {
        boolean success = false;
        if (client != null && targetDevice != null) {
            if (client.getBtState() == 0) {//0是断开状态，2是连接状态
                success = client.connectBt(targetDevice.getAddress());
            } else if (client.getBtState() == 2) {
                success = true;
            }
        }
        Log.i(TAG, "ensureDeviceConnected: " + success);
        return success;
    }


    private boolean findBoundTargetDevice() {
        if (bluetoothAdapter == null) {
            return false;
        }
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        Log.i(TAG, "bondedDevices: " + devices);
        if (devices != null && devices.size() > 0) {
            String name;
            for (BluetoothDevice device : devices) {
                if (device == null) {
                    continue;
                }
                name = device.getName();
                if (TextUtils.isEmpty(name)) {
                    continue;
                }
                if (name.toUpperCase().startsWith(FILTER)) {
                    targetDevice = device;
                    return true;
                }
            }
        }
        return false;
    }

    private volatile BluetoothDevice targetDevice;


    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            Log.i(TAG, "onReceive: " + action);
            if (TextUtils.isEmpty(action)) {
                return;
            }
            BluetoothDevice device;
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null) {
                        String name = device.getName();
                        if (!TextUtils.isEmpty(name)) {
                            if (name.toUpperCase().startsWith(FILTER)) {
                                if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                                    bluetoothAdapter.cancelDiscovery();
                                }
                                targetDevice = device;
                                createBond(device);
                                onDeviceInitialized();
                            }
                        }
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    onDeviceInitialized();
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device == null) {
                        break;
                    }
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_NONE:
                            Log.d(TAG, "BOND_NONE: ");
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            Log.d(TAG, "BOND_BONDING: ");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Log.d(TAG, "BOND_BONDED: ");
                            break;
                    }
                    break;
            }
        }
    };

    public static boolean createBond(BluetoothDevice device) {
        boolean success = false;
        try {
            Method createBond_Method = BluetoothDevice.class.getMethod("createBond");
            createBond_Method.setAccessible(true);
            success = (Boolean) createBond_Method.invoke(device);
            Log.d(TAG, "createBond: " + success);
        } catch (Throwable e) {
            Log.e(TAG, "createBond: " + success, e);
        }
        return success;
    }

    private OnBluetoothListener onBluetoothListener = new OnBluetoothListener() {
        @Override
        public void connectResult(boolean success) {
            Log.d(TAG, "connectResult: " + success);

        }

        @Override
        public void connectionLost() {
            Log.d(TAG, "connectionLost: ");
        }
    };

    private long readStartTime;

    private Runnable readRunnable = new Runnable() {
        @Override
        public void run() {
            boolean connected = ensureDeviceConnected();
            if (connected && client != null) {
                readStartTime = System.currentTimeMillis();
                IDCardItem item = client.readCert(PROTOCOL_TYPE);
                if (item != null && item.retCode == 1) {
                    onReadSuccess(item);
                } else {
                    onReadFailed();
                }
            } else {
                onReadFailed();
            }
        }
    };

    private void onReadFailed() {
        btHandler().postDelayed(oneShutRunnable, 1000);
    }

    private void onDeviceNotFound() {
        btHandler().postDelayed(oneShutRunnable, 1000);
    }

    private void onReadSuccess(IDCardItem item) {
        long currentTimeMillis = System.currentTimeMillis();
        long totalTime = currentTimeMillis - startTime;
        long readTime = currentTimeMillis - readStartTime;
        Log.d(TAG, "onReadSuccess: totalTime = " + totalTime);
        Log.d(TAG, "onReadSuccess: readTime = " + readTime);
        Log.d(TAG, "onReadSuccess: " + item.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (client != null) {
            client.disconnectBt();
            client.disconnect();
            client.setBluetoothListener(null);
        }
        removeBond(targetDevice);
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
        if (btHandler != null) {
            btHandler.removeCallbacksAndMessages(null);
            Thread thread = btHandler.getLooper().getThread();
            btHandler = null;
            if (thread instanceof HandlerThread) {
                ((HandlerThread) thread).quit();
            }
        }
    }

    public static boolean removeBond(BluetoothDevice device) {
        boolean success = false;
        try {
            Method removeBond_Method = BluetoothDevice.class.getMethod("removeBond");
            removeBond_Method.setAccessible(true);
            success = (Boolean) removeBond_Method.invoke(device);
            Log.d(TAG, "removeBond: " + success);
        } catch (Throwable e) {
            Log.e(TAG, "removeBond: " + success, e);
        }
        return success;
    }

    private Handler btHandler;

    private Handler btHandler() {
        if (btHandler == null) {
            synchronized (SignInIdCardActivity.class) {
                if (btHandler == null) {
                    HandlerThread thread = new HandlerThread("bt");
                    thread.start();
                    btHandler = new Handler(thread.getLooper());
//                    btHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return btHandler;
    }
}
