package com.gzq.lib_bluetooth;

import android.Manifest;
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
import android.text.TextUtils;

import com.gzq.lib_bluetooth.utils.RxUtils;
import com.gzq.lib_bluetooth.utils.ToastTool;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;


import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseBluetooth implements LifecycleObserver {
    private boolean isOnSearching = true;
    private boolean isOnDestroy = false;
    private Activity activity;
    private LifecycleOwner owner;
    private ConnectListener connectListener;
    private SearchListener searchListener;
    /**
     * 每次搜索的时间
     */
    private static final int PER_SEARCH_TIME = 8000;
    /**
     * 总共搜索的次数
     */
    private static final String TAG = "BaseBluetooth";
    private static final int NUMBER_SEARCHED = 5;
    private BluetoothSearchHelper searchHelper;
    private BluetoothConnectHelper connectHelper;

    public BaseBluetooth(LifecycleOwner owner) {
        super();
        this.owner = owner;
        this.owner.getLifecycle().addObserver(this);
        if (owner instanceof Activity) {
            activity = ((Activity) owner);
        } else if (owner instanceof Fragment) {
            activity = ((Fragment) owner).getActivity();
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
                    .as(RxUtils.<Boolean>autoDisposeConverter(owner, Lifecycle.Event.ON_STOP))
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
        }else{
            doAccept(type, mac, names);
        }

    }

    private void doRefuse() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastTool.showLong("操作蓝牙需要打开蓝牙权限");
            }
        });
    }

    @WorkerThread
    private void doAccept(BluetoothType type, String mac, String[] names) {
        if (!BluetoothUtils.isBluetoothEnabled()) {
            BluetoothUtils.openBluetooth();
        }
        SystemClock.sleep(2000);
        if (!BluetoothUtils.isBluetoothEnabled()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastTool.showLong("蓝牙未打开或者不支持蓝牙");
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

    class MySearchListener implements SearchListener {
        private BluetoothDevice device;

        @Override
        public void onSearching(boolean isOn) {
            isOnSearching = isOn;
        }

        @Override
        public void obtainDevice(BluetoothDevice device) {
            this.device = device;
            if (searchHelper != null) {
                searchHelper.clear();
            }
            connect(this.device.getAddress());
        }

        @Override
        public void noneFind() {
            BaseBluetooth.this.noneFind();
        }
    }

    class MyConnectListener implements ConnectListener {

        @Override
        public void success(String mac) {
            connectSuccessed(mac);
        }

        @Override
        public void failed() {
            connectFailed();
        }

        @Override
        public void disConnect() {
            disConnected();
        }
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(LifecycleOwner owner) {

    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(LifecycleOwner owner) {
        if (isOnSearching) {
            isOnSearching = false;
            if (searchHelper != null) {
                searchHelper.clear();
            } else {
                BluetoothStore.getInstance().getClient().stopSearch();
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

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        isOnDestroy = true;
        BluetoothStore.getInstance().getClient().stopSearch();
        searchHelper = null;
        if (connectHelper != null) {
            connectHelper.clear();
        }
        connectHelper = null;
        activity = null;
        owner = null;
        connectListener = null;
        searchListener = null;
        if (owner != null) {
            owner.getLifecycle().removeObserver(this);
        }
    }

    protected abstract void noneFind();

    protected abstract void connectSuccessed(String address);

    protected abstract void connectFailed();

    protected abstract void disConnected();
}
