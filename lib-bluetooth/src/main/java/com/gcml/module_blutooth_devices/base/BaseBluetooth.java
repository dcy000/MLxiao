package com.gcml.module_blutooth_devices.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;

import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.handler.WeakHandler;
import com.gcml.module_blutooth_devices.R;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public abstract class BaseBluetooth implements LifecycleObserver {
    private boolean isOnSearching = false;
    private boolean isOnDestroy = false;
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
        if (isConnected) {
            if (baseView != null && baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_connected));
            }
            return;
        }
        Set<String> strings = obtainBrands().keySet();
        start(BluetoothType.BLUETOOTH_TYPE_BLE, address, strings.toArray(new String[strings.size()]));
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
            if (baseView != null && baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_connected));
            }
            return;
        }
        Set<String> strings = obtainBrands().keySet();
        start(BluetoothType.BLUETOOTH_TYPE_BLE, address, strings.toArray(new String[strings.size()]));
    }

    /**
     * 直接连接
     *
     * @param device
     */
    public void startConnect(BluetoothDevice device) {
        if (isOnSearching()) {
            stopSearch();
        }
        if (isConnected && !TextUtils.isEmpty(targetAddress)) {
            //如果是已经和其他设备连接，则先断开已有连接，1秒以后再和该设备连接
            BluetoothStore.getClient().disconnect(targetAddress);
            new WeakHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startDiscovery(device.getAddress());
                }
            }, 1000);
        } else {
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
            BluetoothUtils.openBluetooth();
            SystemClock.sleep(2000);
        }
        if (!BluetoothUtils.isBluetoothEnabled()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showLong("蓝牙未打开或者不支持蓝牙");
                }
            });
            return;
        }
        if (TextUtils.isEmpty(mac) && (names == null || names.length == 0)) {
            throw new IllegalArgumentException("params is abnormal");
        }
        if (connectHelper == null) {
            connectHelper = new BluetoothConnectHelper();
        }
        if (!TextUtils.isEmpty(mac)) {
            if (isSelfConnect(targetName, mac)) {
                return;
            }
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
        if (connectListener == null) {
            connectListener = new MyConnectListener();
        }
        if (!isOnDestroy && connectHelper != null) {
            connectHelper.connect(mac, connectListener);
        }
    }

    protected void stopSearch() {
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
                return;
            }
            if (isAutoConnect) {
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
            //存入全局变量
            BindDeviceBean bindDeviceBean = new BindDeviceBean();
            bindDeviceBean.setBluetoothName(targetName);
            bindDeviceBean.setBluetoothMac(targetAddress);
            bindDeviceBean.setBluetoothBrand(obtainBrands().get(targetName));
            BluetoothStore.instance.bindDevice.postValue(bindDeviceBean);
            if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_connected));
            }
            baseView.connectSuccess(device);
            connectSuccessed(targetName, targetAddress);
        }

        @Override
        public void failed() {
            isConnected = false;
            if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_connect_fail));
            }
            baseView.connectFailed();
            connectFailed();
        }

        @Override
        public void disConnect(String address) {
            isConnected = false;
            if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
                baseView.updateState(UM.getApp().getString(R.string.bluetooth_device_disconnected));
            }
            baseView.disConnected();
            disConnected(address);
            //3秒之后尝试重连
            new WeakHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isOnDestroy && targetAddress != null) {
                        connect(targetAddress);
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
    public void onStop(LifecycleOwner owner) {
        Timber.i("BaseBluetooth>>>>" + BaseBluetooth.this + "======>>>>onStop");
        if (isOnSearching) {
            Timber.i("BaseBluetooth>>>>====>>>isOnSearching==" + isOnSearching);
            isOnSearching = false;
            if (searchHelper != null) {
                Timber.i("BaseBluetooth>>>>==searchHelper:" + searchHelper + "==>>>searchHelper.clear();");
                searchHelper.clear();
            } else {
                BluetoothStore.getClient().stopSearch();
                Timber.e("BaseBluetooth>>>>======>>>>searchHelper.clear() has not carry out");
            }
        }
        //Fragment中使用需要提前释放部分资源，因为Fragment走到onDestroy的时机很晚
        if (owner instanceof Fragment) {
            if (connectHelper != null) {
                connectHelper.clear();
            }
            connectHelper = null;
            connectListener = null;
            searchListener = null;
        }
    }

    @SuppressLint("RestrictedApi")
    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        Timber.i("BaseBluetooth" + BaseBluetooth.this + ">>>>======>>>>onDestroy");
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
        if (baseView instanceof Fragment && ((Fragment) baseView).isAdded()) {
            baseView.updateState(UM.getApp().getString(R.string.unfind_devices));
        }
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
