package com.gcml.module_detection.idcard;

import android.arch.lifecycle.LifecycleOwner;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.gcml.common.utils.Handlers;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothType;
import com.gcml.module_blutooth_devices.base.DeviceBrand;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.gcml.module_detection.R;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.bt.BtReadClient;
import com.kaer.sdk.bt.OnBluetoothListener;

import java.lang.reflect.Method;
import java.util.HashMap;

import timber.log.Timber;

public class IDCardPresenter extends BaseBluetooth {
    private BtReadClient client;
    private boolean initializing;
    private boolean isRegistered;
    private Handler btHandler;
    private static final int PROTOCOL_TYPE = 0;
    private boolean isDestroyed;
    private IDCardItem item;
    private String targetName;
    private String targetAddress;

    public IDCardPresenter(IBluetoothView owner) {
        this(owner, true);
    }

    public IDCardPresenter(IBluetoothView owner, boolean isAutoDiscovery) {
        super(owner);
        if (isAutoDiscovery) {
            startDiscovery(targetAddress, BluetoothType.BLUETOOTH_TYPE_CLASSIC);
        }
    }

    @Override
    protected void connectSuccessed(String name, String address) {

    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected(String address) {

    }

    @Override
    protected boolean isSelfConnect(String name, String address) {
        if (name.contains("KT8000")) {
            targetName = name;
            targetAddress = address;
            client = BtReadClient.getInstance();
            client.setBluetoothListener(onBluetoothListener);
            btHandler().post(oneShutRunnable);
            return true;
        }
        return super.isSelfConnect(name, address);
    }

    @Override
    protected void saveSP(String sp) {
        SPUtil.put(BluetoothConstants.SP.SP_SAVE_ID_CARD, sp);
    }

    @Override
    protected String obtainSP() {
        return (String) SPUtil.get(BluetoothConstants.SP.SP_SAVE_ID_CARD, "");
    }

    @Override
    protected HashMap<String, String> obtainBrands() {
        return DeviceBrand.ID_CARD;
    }

    private OnBluetoothListener onBluetoothListener = new OnBluetoothListener() {
        @Override
        public void connectResult(boolean success) {
            Handlers.ui().post(new Runnable() {
                @Override
                public void run() {
                    Timber.i("IDCard设备已连接");
                    updateState(R.string.bluetooth_device_connected);
                    baseView.connectSuccess(BluetoothUtils.getRemoteDevice(targetAddress), targetName);
                }
            });
        }

        @Override
        public void connectionLost() {
            Handlers.ui().post(new Runnable() {
                @Override
                public void run() {
                    Timber.i("IDCard设备已断开");
                    updateState(R.string.bluetooth_device_disconnected);
                    baseView.disConnected();
                }
            });
        }
    };

    private void initDevice() {
        if (initializing) {
            return;
        }
        registerReceiver();
        initializing = true;
        onDeviceInitialized();
    }

    private void registerReceiver() {
        if (isRegistered) {
            return;
        }
        isRegistered = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        getActivity().registerReceiver(receiver, filter);
    }

    private void onDeviceInitialized() {
        initializing = false;
        btHandler().post(readRunnable);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            BluetoothDevice device;
            switch (action) {
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device == null) {
                        break;
                    }
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_NONE:
                            break;
                        case BluetoothDevice.BOND_BONDING:

                            break;
                        case BluetoothDevice.BOND_BONDED:
                            saveSP(device.getName() + "," + device.getAddress());
                            onDeviceInitialized();
                            break;
                    }
                    break;
            }
        }
    };
    private Runnable oneShutRunnable = new Runnable() {
        @Override
        public void run() {
            String sp = obtainSP();
            if (!TextUtils.isEmpty(sp)) {
                String[] split = sp.split(",");
                if (split.length == 2) {
                    targetName = split[0];
                    targetAddress = split[1];
                }
            }
            if (!TextUtils.isEmpty(targetAddress) && BluetoothAdapter.checkBluetoothAddress(targetAddress)) {
                BluetoothDevice device = BluetoothUtils.getRemoteDevice(targetAddress);
                if (device != null) {
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        onDeviceInitialized();
                        return;
                    }
                    createBond(device);
                    return;
                }
            }
            initDevice();
        }
    };
    private Runnable readRunnable = new Runnable() {
        public volatile boolean isRead;

        @Override
        public void run() {
            if (isRead) {
                return;
            }
            isRead = true;
            boolean connected = ensureDeviceConnected();
            updateState("请刷身份证");
            if (connected && client != null && !isDestroyed) {
                IDCardItem temp;
                try {
                    temp = client.readCert(PROTOCOL_TYPE);
                } catch (Throwable e) {
                    temp = null;
                    e.printStackTrace();
                }
                isRead = false;
                if (isDestroyed) {
                    return;
                }
                item = temp;
                if (item != null && item.retCode == 1) {
                    Handlers.ui().post(new Runnable() {
                        @Override
                        public void run() {
                            if (cardRead != null) {
                                cardRead.onReadSuccess(item);
                            }
                        }
                    });
                } else {
                    readFailed();
                }
            } else {
                readFailed();
            }
        }
    };

    public void readFailed() {
        //读取失败3秒之后重新读取
        btHandler().postDelayed(readRunnable, 3000);
        Handlers.ui().post(new Runnable() {
            @Override
            public void run() {
                if (cardRead != null) {
                    cardRead.onReadFailed();
                }
            }
        });
    }

    private boolean ensureDeviceConnected() {
        boolean success = false;
        if (client != null) {
            if (client.getBtState() == 0) {//0是断开状态，2是连接状态
                if (!TextUtils.isEmpty(targetAddress)) {
                    success = client.connectBt(targetAddress);
                }
            } else if (client.getBtState() == 2) {
                success = true;
            }
        }
        return success;
    }

    private Handler btHandler() {
        if (btHandler == null) {
            synchronized (IDCardPresenter.class) {
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

    public static boolean createBond(BluetoothDevice device) {
        boolean success = false;
        try {
            Method createBond_Method = BluetoothDevice.class.getMethod("createBond");
            createBond_Method.setAccessible(true);
            success = (Boolean) createBond_Method.invoke(device);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public void onResume(LifecycleOwner owner) {
        super.onResume(owner);
        isDestroyed = false;
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        super.onDestroy(owner);
        isDestroyed = true;
        if (isRegistered && receiver != null) {
            getActivity().unregisterReceiver(receiver);
            isRegistered = false;
        }
        if (client != null) {
            client.disconnectBt();
            client.disconnect();
            client.setBluetoothListener(null);
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

    private IDCardRead cardRead;

    public void setCardOnReadListener(IDCardRead cardRead) {
        this.cardRead = cardRead;
    }

    public interface IDCardRead {
        void onReadSuccess(IDCardItem idCardItem);

        void onReadFailed();
    }
}
