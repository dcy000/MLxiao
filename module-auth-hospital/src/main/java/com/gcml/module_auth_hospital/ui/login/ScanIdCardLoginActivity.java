package com.gcml.module_auth_hospital.ui.login;

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
import android.view.View;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.bt.BtReadClient;
import com.kaer.sdk.bt.OnBluetoothListener;

import java.lang.reflect.Method;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ScanIdCardLoginActivity extends AppCompatActivity {
    //    请把身份证放在身份证阅读器上
    private static final String TAG = "MyBluetooth";
    private static final String FILTER = "KT8000";
    private static final int PROTOCOL_TYPE = 0;

    private BluetoothAdapter bluetoothAdapter;
    private BtReadClient client;
    private volatile boolean isRegistered;
    private TranslucentToolBar authScanIdCardToobar;

    UserRepository userRepository = new UserRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_idcard);
        initView();
        registerReceiver();
        client = BtReadClient.getInstance();
        client.setBluetoothListener(onBluetoothListener);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null)
            bluetoothAdapter.enable();
        onTurnOn();
    }

    private void initView() {
        authScanIdCardToobar = findViewById(R.id.auth_scan_idcard_tb);
        authScanIdCardToobar.setData("身 份 证 扫 描 登 录",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_icon_bluetooth_break, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                        releaScan();
                    }

                    @Override
                    public void onRightClick() {
                        updateScanIdCard();
                    }
                });
    }

    private void releaScan() {
        targetDevice = null;
        initializing = false;
        removeBounds();
        btHandler().post(oneShutRunnable);
    }

    protected void updateScanIdCard() {
        UserSpHelper.setKTAddress("");
        targetDevice = null;
        initializing = false;
        removeBounds();
        btHandler().post(oneShutRunnable);
    }


    private void removeBounds() {
        if (isRegistered && receiver != null) {
            unregisterReceiver(receiver);
            isRegistered = false;
        }
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
                    removeBond(device);
                }
            }
        }
    }

    private void onTurnOn() {
        btHandler().post(oneShutRunnable);
    }

    private long startTime;

    private Runnable oneShutRunnable = new Runnable() {
        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            initDevice();
        }
    };

    private boolean initializing;

    private void initDevice() {
        if (initializing) {
            return;
        }
        registerReceiver();
        initializing = true;
        if (targetDevice == null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
                initializing = false;
                return;
            }
            String address = UserSpHelper.getKTAddress();
            if (!TextUtils.isEmpty(address) && BluetoothAdapter.checkBluetoothAddress(address)) {
                Log.d(TAG, "initDevice: LocalShared");
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                if (device != null) {
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        Log.d(TAG, "initDevice: Bound");
                        targetDevice = device;
                        onDeviceInitialized();
                        return;
                    }
                    Log.d(TAG, "initDevice: not Bound");
//                    onDeviceInitialized();
                    createBond(device);
                    return;
                }
            }
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
        Log.i(TAG, "initDevice: onDeviceInitialized" + address);
        if (targetDevice == null) {
            onDeviceNotFound();
            return;
        }
        btHandler().post(readRunnable);
    }


    private void findTargetDevice() {
        Log.d(TAG, "initDevice: findTargetDevice");
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }


    private boolean ensureDeviceConnected() {
        Log.i(TAG, "connectDevice: start");
        boolean success = false;
        if (client != null && targetDevice != null) {
            if (client.getBtState() == 0) {//0是断开状态，2是连接状态
                success = client.connectBt(targetDevice.getAddress());
            } else if (client.getBtState() == 2) {
                success = true;
            }
        }
        Log.i(TAG, "connectDevice: " + success);
        return success;
    }

    private boolean findBoundTargetDevice() {
        Log.d(TAG, "initDevice: findBoundTargetDevice");
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
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
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.e(TAG, "TURNING_ON");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            onTurnOn();
                            Log.e(TAG, "initDevice : STATE_ON");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.e(TAG, "STATE_TURNING_OFF");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            Log.e(TAG, "initDevice : STATE_OFF");
                            break;
                    }
                    break;

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
                            }
                        }
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    if (targetDevice == null) {
                        onDeviceInitialized();
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device == null) {
                        break;
                    }
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_NONE:
                            Log.d(TAG, "initDevice: BOND_NONE: ");
                            targetDevice = null;
                            onDeviceInitialized();
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            Log.d(TAG, "BOND_BONDING: ");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            targetDevice = device;
                            Log.d(TAG, "initDevice: BOND_BONDED: ");
                            onDeviceInitialized();
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    authScanIdCardToobar.setResIdRight(R.drawable.common_icon_bluetooth_connect);
                }
            });
        }

        @Override
        public void connectionLost() {
            Log.d(TAG, "connectionLost: ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    authScanIdCardToobar.setResIdRight(R.drawable.common_icon_bluetooth_break);
                }
            });
        }
    };

    private long readStartTime;

    private Runnable readRunnable = new Runnable() {
        public volatile boolean isRead;

        @Override
        public void run() {
            if (isRead) {
                return;
            }
            isRead = true;
            boolean connected = ensureDeviceConnected();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mlSpeak("请刷身份证");
                }
            });
            if (connected && client != null && !isFinishing() && !isDestroyed()) {
                readStartTime = System.currentTimeMillis();
                IDCardItem temp;
                try {
                    temp = client.readCert(PROTOCOL_TYPE);
                } catch (Throwable e) {
                    temp = null;
                    e.printStackTrace();
                }
                isRead = false;
                if (isFinishing() && isDestroyed()) {
                    return;
                }
                item = temp;
                long currentTimeMillis = System.currentTimeMillis();
                long totalTime = currentTimeMillis - startTime;
                long readTime = currentTimeMillis - readStartTime;
                Log.d(TAG, "onReadSuccess: totalTime = " + totalTime);
                Log.d(TAG, "onReadSuccess: readTime = " + readTime);
//                Log.d(TAG, "onReadSuccess: " + item.toString());
                if (item != null && item.retCode == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReadSuccess(item);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReadFailed();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onReadFailed();
                    }
                });
            }
        }
    };


    private void onReadFailed() {
        item = null;
        mlSpeak("请刷身份证");
        btHandler().postDelayed(readRunnable, 2000);
    }

    private void onDeviceNotFound() {
        item = null;
        mlSpeak("找不到设备");
        btHandler().postDelayed(oneShutRunnable, 1000);
    }

    private IDCardItem item;

    private void onReadSuccess(IDCardItem item) {
        this.item = item;
        if (item != null) {
            mlSpeak("读取成功");
            onCheckRegistered(item);
        }
    }


    private void onCheckRegistered(final IDCardItem item) {
        String deviceId = Utils.getDeviceId(getContentResolver());
        if (item == null) {
            return;
        }
        String idCardNumber = item.certNumber;
        userRepository
                .isIdCardNotExit(idCardNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        ToastUtils.showShort("未注册,请先去注册");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        signIn(deviceId, idCardNumber);
                    }
                });
    }

    private void signIn(String deviceId, String idCardNumber) {

        userRepository
                .signInByIdCard(deviceId, idCardNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("正在登录...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                                .addParam("userId", user.id)
                                .build()
                                .callAsync();
                        ToastUtils.showLong("登录成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
        if (isRegistered && receiver != null) {
            unregisterReceiver(receiver);
            isRegistered = false;
        }
        if (client != null) {
            client.disconnectBt();
            client.disconnect();
            client.setBluetoothListener(null);
        }
        if (targetDevice != null) {
            String address = targetDevice.getAddress();
            UserSpHelper.setKTAddress(address);
        }
//        removeBond(targetDevice);
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
//            if (bluetoothAdapter.isEnabled()) {
//                bluetoothAdapter.disable();
//            }
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
            synchronized (ScanIdCardLoginActivity.class) {
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

    public void onRootClick(View view) {
        onReadFailed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSpeaking();
    }


    private void stopSpeaking() {
        MLVoiceSynthetize.stop();
    }

    private void mlSpeak(String content) {
        MLVoiceSynthetize.startSynthesize(this, content);
    }

    private LoadingDialog mLoadingDialog;

    private void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }

}
