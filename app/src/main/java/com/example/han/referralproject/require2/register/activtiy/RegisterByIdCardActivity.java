package com.example.han.referralproject.require2.register.activtiy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.dialog.DialogTypeEnum;
import com.example.han.referralproject.require2.dialog.SomeCommonDialog;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.activity.InquiryAndFileActivity;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.bt.BtReadClient;
import com.kaer.sdk.bt.OnBluetoothListener;
import com.medlink.danbogh.utils.JpushAliasUtils;

import java.lang.reflect.Method;
import java.util.Set;

import static com.example.han.referralproject.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_IDCARD_NUMBER;
import static com.example.han.referralproject.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_SEX;
import static com.example.han.referralproject.require2.register.activtiy.InputFaceActivity.REGISTER_ADDRESS;
import static com.example.han.referralproject.require2.register.activtiy.InputFaceActivity.REGISTER_REAL_NAME;

public class RegisterByIdCardActivity extends BaseActivity implements SomeCommonDialog.OnDialogClickListener {
    //    请把身份证放在身份证阅读器上
    private static final String TAG = "MyBluetooth";
    private static final String FILTER = "KT8000";
    private static final int PROTOCOL_TYPE = 0;

    private BluetoothAdapter bluetoothAdapter;
    private BtReadClient client;
    private volatile boolean isRegistered;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_by_id_card);
        initTitle();
        registerReceiver();
        client = BtReadClient.getInstance();
        client.setBluetoothListener(onBluetoothListener);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.enable();
        onTurnOn();
        ActivityHelper.addActivity(this);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 扫 描 注 册");
        isLogin = getIntent().getBooleanExtra("login", false);
        if (isLogin) {
            mTitleText.setText("身 份 证 扫 描 登 录");
        }

        mLeftText.setVisibility(View.VISIBLE);
        mLeftView.setVisibility(View.VISIBLE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterByIdCardActivity.this, WifiConnectActivity.class));
            }
        });
    }

    @Override
    protected void backMainActivity() {
        LocalShared.getInstance(this).setString(FILTER, "");
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
            String address = LocalShared.getInstance(this).getString(FILTER);
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

        }

        @Override
        public void connectionLost() {
            Log.d(TAG, "connectionLost: ");
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
                    speak("请刷身份证");
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
        speak("请刷身份证");
        btHandler().postDelayed(readRunnable, 2000);
    }

    private void onDeviceNotFound() {
        item = null;
        speak("找不到设备");
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
        if (item == null) {
            return;
        }
//        showLoadingDialog("加载中");

        NetworkApi.isRegisteredByIdCard(item.certNumber, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (isLogin) {
                    LocalShared.getInstance(mContext).setUserInfo(response);
                    LocalShared.getInstance(mContext).setSex(response.sex);
                    LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                    LocalShared.getInstance(mContext).setUserAge(response.age);
                    LocalShared.getInstance(mContext).setUserHeight(response.height);
                    new JpushAliasUtils(RegisterByIdCardActivity.this).setAlias("user_" + response.bid);
                    startActivity(new Intent(RegisterByIdCardActivity.this, InquiryAndFileActivity.class));
                    return;
                }
                //注册场景
                mlSpeak("身份证已注册");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
//                hideLoadingDialog();
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (isLogin) {
                    registerNoticeDialog();
                    return;
                }
                //注册场景
                toPhoneAndCode(item);

            }
        });
    }

    private void registerNoticeDialog() {
        SomeCommonDialog dialog = new SomeCommonDialog(DialogTypeEnum.idCardUnregistered);
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onClickConfirm(DialogTypeEnum type) {
        startActivity(new Intent(this, ChoiceIDCardRegisterTypeActivity.class));
        finish();
    }

    private void toPhoneAndCode(IDCardItem item) {
        if (item == null) {
            return;
        }
        startActivity(new Intent(this, PhoneAndCodeActivity.class)
                .putExtra(PhoneAndCodeActivity.FROM_WHERE, PhoneAndCodeActivity.FROM_REGISTER_BY_IDCARD)
                .putExtra(REGISTER_IDCARD_NUMBER, item.certNumber)
                .putExtra(REGISTER_REAL_NAME, item.partyName)
                .putExtra(REGISTER_SEX, item.gender)
                .putExtra(REGISTER_ADDRESS, item.certAddress));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            LocalShared.getInstance(this).setString(FILTER, address);
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
            synchronized (RegisterByIdCardActivity.class) {
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

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
    }

    public void onRootClick(View view) {
        onReadFailed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSpeaking();
    }


}
