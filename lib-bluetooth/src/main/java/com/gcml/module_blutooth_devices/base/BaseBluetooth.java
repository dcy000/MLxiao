package com.gcml.module_blutooth_devices.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;

import com.gcml.common.utils.Handlers;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Set;

import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public abstract class BaseBluetooth implements LifecycleObserver {
    private boolean isOnSearching = false;
    protected boolean isOnDestroy = false;
    private boolean isConnected = false;
    private SupportActivity activity;
    private ConnectListener connectListener;
    private SearchListener searchListener;
    protected IBluetoothView baseView;
    /**
     * 每次搜索的时间
     */
    private static final int PER_SEARCH_TIME = 5000;
    /**
     * 总共搜索的次数
     */
    private static final String TAG = "BaseBluetooth";
    private static final int NUMBER_SEARCHED = 6;
    private BluetoothSearchHelper searchHelper;
    private BluetoothConnectHelper connectHelper;
    protected String targetName = null;
    protected String targetAddress = null;
    private boolean isAutoConnect = true;

    @SuppressLint("RestrictedApi")
    public BaseBluetooth(IBluetoothView owner) {
        this.baseView = owner;
        if (owner instanceof Activity) {
            activity = ((SupportActivity) owner);
        } else if (owner instanceof Fragment) {
            activity = ((Fragment) owner).getActivity();
        }
        this.activity.getLifecycle().addObserver(this);

        String sp = obtainSP();
        if (!TextUtils.isEmpty(sp)) {
            String[] split = sp.split(",");
            if (split.length == 2) {
                targetName = split[0];
                targetAddress = split[1];
            }
        }
    }

    /**
     * 进行搜索
     *
     * @param address
     */
    public void startDiscovery(String address) {
        if (isOnSearching) {
            return;
        }
//        if (isConnected) {
//            if (baseView != null && baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
//                baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_connected));
//            }
//            return;
//        }
        Set<String> strings = obtainBrands().keySet();
        start(BluetoothType.BLUETOOTH_TYPE_BLE, address, strings.toArray(new String[strings.size()]));
    }

    public void startDiscovery(String address, BluetoothType bluetoothType) {
        if (isOnSearching) {
            return;
        }
//        if (isConnected) {
//            if (baseView != null && baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
//                baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_connected));
//            }
//            return;
//        }
        Set<String> strings = obtainBrands().keySet();
        start(bluetoothType, address, strings.toArray(new String[strings.size()]));
    }

    /**
     * 进行搜索
     *
     * @param address
     */
    public void startDiscovery(String address, boolean isAutoConnect) {
        this.isAutoConnect = isAutoConnect;
        if (isOnSearching) {
            return;
        }
        if (isConnected) {
            updateState(R.string.bluetooth_device_connected);
            return;
        }
        Set<String> strings = obtainBrands().keySet();
        start(BluetoothType.BLUETOOTH_TYPE_BLE, address, strings.toArray(new String[strings.size()]));
    }

    /**
     * 直接连接
     *
     * @param device
     * @param isFirstDisConne 是否先断开原来的连接
     */
    public void startConnect(BluetoothDevice device, boolean isFirstDisConne) {
        if (isOnSearching()) {
            stopSearch();
        }
        if (isConnected && !TextUtils.isEmpty(targetAddress)) {
            if (isFirstDisConne) {
                //如果是已经和其他设备连接，则先断开已有连接，1秒以后再和该设备连接
                Timber.w("bt ---> disconnect: address = %s", targetAddress);
                BluetoothStore.getClient().disconnect(targetAddress);
                Timber.w("bt ---> startScan: address = %s delay 1000ms", device.getAddress());
                Handlers.bg().postDelayed(() -> startDiscovery(device.getAddress()), 1000);
            } else {
                Timber.w("bt ---> connect directly: address = %s", device.getAddress());
                connect(device.getAddress());
            }
        } else {
            Timber.w("bt ---> startScan: address = %s", device.getAddress());
            startDiscovery(device.getAddress());
        }
    }

    /**
     * 停止搜索
     */
    public void stopDiscovery() {
        if (isOnSearching && searchHelper != null) {
            searchHelper.stop();
        }
    }

    public void start(final BluetoothType type, final String mac, final String... names) {
        if (activity == null) {
            throw new IllegalArgumentException("activity==null");
        }
        RxPermissions rxPermissions = new RxPermissions(activity);
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            rxPermissions
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .observeOn(Schedulers.newThread())
                    .as(RxUtils.<Boolean>autoDisposeConverter(activity, Lifecycle.Event.ON_STOP))
                    .subscribe(new DefaultObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                doAccept(type, mac, names);
                            } else {
                                doRefuse();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            doAccept(type, mac, names);
        }

    }

    private void doRefuse() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLong("操作蓝牙需要打开蓝牙权限");
            }
        });
    }

    @WorkerThread
    private void doAccept(BluetoothType type, String mac, String[] names) {
        if (!BluetoothUtils.isBluetoothEnabled()) {
            Timber.w("bt ---> isBluetoothEnabled:  %s", false);
            Timber.w("bt ---> openBluetooth...");
            BluetoothUtils.openBluetooth();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Timber.w("bt ---> isBluetoothEnabled 2:  %s", BluetoothUtils.isBluetoothEnabled());
                if (!BluetoothUtils.isBluetoothEnabled()) {
                    ToastUtils.showLong("蓝牙未打开, 请重试");
                    return;
                }
                connectDirectlyOrScan(type, mac, names);
            }
        }, 1800);

    }

    private Handler handler = new Handler(Looper.getMainLooper());

    private void connectDirectlyOrScan(BluetoothType type, String mac, String[] names) {
        if (TextUtils.isEmpty(mac) && (names == null || names.length == 0)) {
            throw new IllegalArgumentException("params is abnormal");
        }
        if (connectHelper == null) {
            connectHelper = new BluetoothConnectHelper();
        }
        if (!TextUtils.isEmpty(mac)) {
            if (isSelfConnect(targetName, mac)) {
                Timber.w("bt ---> connect directly: isSelfConnect = %s address = %s", true, mac);
                return;
            }
            Timber.w("bt ---> connect directly: address = %s", mac);
            connect(mac);
            return;
        }
        if (searchHelper == null) {
            searchHelper = new BluetoothSearchHelper();
        }

        if (searchListener == null) {
            searchListener = new MySearchListener();
        }

        if (type == BluetoothType.BLUETOOTH_TYPE_CLASSIC) {
            searchHelper.searchClassic(PER_SEARCH_TIME, NUMBER_SEARCHED, searchListener, names);
        } else {
            searchHelper.searchBle(PER_SEARCH_TIME, NUMBER_SEARCHED, searchListener, names);
        }
    }

    protected void connect(String mac) {
        Timber.w("bt ---> connect: isOnDestroy = %s, address = %s, connectListener = %s", isOnDestroy, mac, connectListener);
        if (connectListener == null) {
            connectListener = new MyConnectListener();
        }
        if (!isOnDestroy && connectHelper != null) {
            connectHelper.connect(mac, connectListener);
        }
    }

    protected void stopSearch() {
        Timber.w("bt ---> stopScan");
        if (searchHelper != null) {
            searchHelper.stop();
        }
    }

    class MySearchListener implements SearchListener {
        private BluetoothDevice device;

        @Override
        public void onSearching(boolean isOn) {
            isOnSearching = isOn;
            if (!isOn) {
                //结束搜索
                baseView.discoveryFinished(isConnected);
            } else {
                baseView.discoveryStarted();
            }
        }

        @Override
        public void onNewDeviceFinded(BluetoothDevice newDevice) {
            baseView.discoveryNewDevice(newDevice);
            newDeviceFinded(newDevice);
        }

        @Override
        public void obtainDevice(BluetoothDevice device) {
            this.device = device;
            //自己实现连接流程
            if (isSelfConnect(device.getName(), device.getAddress())) {
                Timber.w("bt ---> connect: isSelfConnect = %s, address = %s", true, this.device.getAddress());
                return;
            }
            if (isAutoConnect) {
                Timber.w("bt ---> connect: isAutoConnect = %s, address = %s", isAutoConnect, this.device.getAddress());
                connect(this.device.getAddress());
            }
        }

        @Override
        public void noneFind() {
            BaseBluetooth.this.noneFind();
            baseView.unFindTargetDevice();
        }
    }

    class MyConnectListener implements ConnectListener {


        @Override
        public void success(BluetoothDevice device) {
            isConnected = true;
            if (!TextUtils.isEmpty(device.getName())) {
                targetName = device.getName();
            }
            targetAddress = device.getAddress();
            //本地缓存
            saveSP(targetName + "," + targetAddress);
            updateState(R.string.bluetooth_device_connected);
            baseView.connectSuccess(device, targetName);
            connectSuccessed(targetName, targetAddress);
        }

        @Override
        public void failed() {
            isConnected = false;
            updateState(R.string.bluetooth_device_connect_fail);
            baseView.connectFailed();
            connectFailed();
        }

        @Override
        public void disConnect(String address) {
            isConnected = false;
            updateState(R.string.bluetooth_device_disconnected);
            baseView.disConnected();
            disConnected(address);
            //3秒之后尝试重连
            Timber.w("bt ---> connect reconnect: address = %s delay 3000ms", address);
            Handlers.bg().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Timber.w("bt ---> connect reconnect: address = %s", address);
                    if (!isConnected && !isOnDestroy && targetAddress != null) {
                        Timber.w("bt ---> connect reconnect: address = %s", address);
                        connect(targetAddress);
                    } else {
                        Timber.w("bt ---> connect reconnect cancel: address = %s", address);
                    }
                }
            }, 3000);
        }

    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(LifecycleOwner owner) {

    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Timber.w("bt ---> onStop: isOnSearching = %s, searchHelper = %s", isOnSearching, searchHelper);
        if (isOnSearching) {
            isOnSearching = false;
            if (searchHelper != null) {
                searchHelper.clear();
            } else {
                BluetoothStore.getClient().stopSearch();
            }
        }
        //Fragment中使用需要提前释放部分资源，因为Fragment走到onDestroy的时机很晚
        Timber.w("bt ---> onStop: connectHelper = %s", connectHelper);

        if (connectHelper != null) {
            connectHelper.clear();
        }
        connectHelper = null;
        connectListener = null;
        searchListener = null;

    }

    @SuppressLint("RestrictedApi")
    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        Timber.w("bt ---> onDestroy: connectHelper = %s", connectHelper);
        isOnDestroy = true;
        BluetoothStore.getClient().stopSearch();
        searchHelper = null;
        if (connectHelper != null) {
            connectHelper.clear();
        }
        connectHelper = null;
        if (activity != null) {
            activity.getLifecycle().removeObserver(this);
        }
        activity = null;
        connectListener = null;
        searchListener = null;
        BluetoothStore.instance.detection.postValue(null);
        handler.removeCallbacksAndMessages(null);
        saveSP("");
    }

    public boolean isOnSearching() {
        return isOnSearching;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public SupportActivity getActivity() {
        return activity;
    }

    @CallSuper
    protected synchronized void newDeviceFinded(BluetoothDevice device) {

    }

    @CallSuper
    protected void noneFind() {
        updateState(R.string.unfind_devices);
    }

    protected void updateState(int msg) {
        Handlers.ui().post(new Runnable() {
            @Override
            public void run() {
                if (baseView != null && baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                    baseView.updateState(UM.getApp().getString(msg));
                }
                if (baseView != null && baseView instanceof SupportActivity && !isOnDestroy) {
                    baseView.updateState(UM.getApp().getString(msg));
                }
            }
        });
    }

    protected void updateState(String msg) {
        Timber.w("bt ---> updateState: msg = %s", msg);
        Handlers.ui().post(new Runnable() {
            @Override
            public void run() {
                if (baseView != null && baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                    baseView.updateState(msg);
                }
                if (baseView != null && baseView instanceof SupportActivity && !isOnDestroy) {
                    baseView.updateState(msg);
                }
            }
        });
    }

    protected boolean isSelfConnect(String name, String address) {
        return false;
    }

    protected abstract void connectSuccessed(String name, String address);

    protected abstract void connectFailed();

    protected abstract void disConnected(String address);

    protected abstract void saveSP(String sp);

    protected abstract String obtainSP();


    protected abstract HashMap<String, String> obtainBrands();
}
