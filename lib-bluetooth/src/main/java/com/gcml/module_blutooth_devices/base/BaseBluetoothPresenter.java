package com.gcml.module_blutooth_devices.base;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.gcml.common.utils.handler.WeakHandler;
import com.gcml.common.utils.permission.PermissionsManager;
import com.gcml.common.utils.permission.PermissionsResultAction;
import com.gcml.module_blutooth_devices.R;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseBluetoothPresenter implements IPresenter, Comparator<SearchResult> {
    /**
     * 搜索设备的配置对象
     */
    protected static DiscoverDevicesSetting discoverSetting;
    /**
     * 搜索的类型 MAC:直接连接相应物理地址的设备；NAME:搜索对应名称的设备；MIX:物理地址和名字混合搜索
     */
    protected int discoverType;
    /**
     * 蓝牙连接的敏感权限
     */
    private static final String DANGET_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    protected Context baseContext;
    protected IView baseView;
    private SearchRequest request;
    private boolean isOnSearching = false;
    protected String targetName;
    protected String targetAddress;
    protected List<SearchResult> devices;
    private TimeCount timeCount;
    protected boolean isDestroy = false;
    private boolean isReturnServiceAndCharacteristic = false;
    private boolean isSearchedTargetDevice = false;
    private boolean isConnected = false;
    protected static android.bluetooth.BluetoothDevice lockedDevice;
    private WeakHandler weakHandler;
    private WeakHandler weakHandler2;
    private static final int SEARCH_MAC2NAME = 1;
    private static final int DEVICE_DISCONNECTED = 2;
    private MyConnectStateListener connectStateListener;
    private MySearchListener searchListener;
    private MyConnectListener connectListener;
    private long reConnectTimeTag = 0;
    private final Handler.Callback weakRunnable = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCH_MAC2NAME:
                    //搜索不到mac转搜name
                    Logg.e(BaseBluetoothPresenter.class, "物理地址转名字搜索handleMessage: ");
                    discoverType = DISCOVER_WITH_NAME;
                    request = setSearchRequest();
                    searchDevices();
                    break;
                case DEVICE_DISCONNECTED:
                    //蓝牙断开连接
                    if (!isDestroy) {
                        disConnected();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };


    public BaseBluetoothPresenter(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super();
        this.baseView = fragment;
        this.baseContext = fragment.getThisContext();
        BaseBluetoothPresenter.discoverSetting = discoverSetting;
        weakHandler = new WeakHandler(weakRunnable);
        weakHandler2 = new WeakHandler();
        //如果物理地址连接15秒之后还没有连接成功 则改为以蓝牙名称匹配
        timeCount = new TimeCount(15000, 1000, weakHandler);
        discoverType = discoverSetting.getDiscoverType();
        targetAddress = discoverSetting.getTargetMac();
        targetName = discoverSetting.getTargetName();
        if (discoverType == DISCOVER_WITH_MIX) {
            devices = new ArrayList<>();
        }
        checkBlueboothOpened();
    }

    private final PermissionsResultAction permissionsResultAction = new PermissionsResultAction() {
        @Override
        public void onGranted() {
            Logg.e(BaseBluetoothPresenter.class, "用户同意权限请求");
        }

        @Override
        public void onDenied(String permission) {
            Logg.e(BaseBluetoothPresenter.class, "拒绝了用户请求");
        }
    };

    @Override
    public void checkBlueboothOpened() {//蓝牙相关权限可在这里进行检查
        if (!PermissionsManager.getInstance().hasPermission(baseContext, DANGET_PERMISSION)) {
            if (baseView instanceof Activity) {
                PermissionsManager.getInstance()
                        .requestPermissionsIfNecessaryForResult(((Activity) baseView),
                                new String[]{DANGET_PERMISSION}, permissionsResultAction);
            } else if (baseView instanceof Fragment) {
                PermissionsManager.getInstance()
                        .requestPermissionsIfNecessaryForResult(((Fragment) baseView),
                                new String[]{DANGET_PERMISSION}, permissionsResultAction);
            }
        }
        boolean bluetoothOpened = BluetoothClientManager.getClient().isBluetoothOpened();
        if (!bluetoothOpened) {
            BluetoothClientManager.getClient().openBluetooth();
        }
        if (!isSelfDefined()) {
            switch (discoverType) {
                case IPresenter.DISCOVER_WITH_MAC:
                    if (TextUtils.isEmpty(targetAddress)) {
                        throw new NullPointerException("连接的设备为NULL");
                    }
                    connectDevice(targetAddress);
                    break;
                case IPresenter.DISCOVER_WITH_NAME:
                    request = setSearchRequest();
                    searchDevices();
                    break;
                case IPresenter.DISCOVER_WITH_MIX:
                    request = setSearchRequest();
                    searchDevices();
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void searchDevices() {
        if (request == null) {
            Logg.e(BaseBluetoothPresenter.class, "请配置搜索参数:request==null ");
            return;
        }
        if (searchListener == null) {
            Logg.e(BaseBluetoothPresenter.class, "searchListener==" + searchListener);
            searchListener = new MySearchListener();
        }
        BluetoothClientManager.getClient().search(request, searchListener);
    }


    class MySearchListener implements SearchResponse {

        @Override
        public void onSearchStarted() {
            isOnSearching = true;
            startDiscoverDevices();
        }

        @Override
        public void onDeviceFounded(SearchResult searchResult) {
            if (searchResult == null) {
                return;
            }
            String name = searchResult.getName();
            String address = searchResult.getAddress();
            switch (discoverType) {
                case DISCOVER_WITH_NAME:
                    if (TextUtils.isEmpty(targetName)) {
                        throw new NullPointerException("连接的设备为NULL");
                    }
                    if (!TextUtils.isEmpty(name) && name.startsWith(targetName)) {
                        BluetoothClientManager.getClient().stopSearch();
                        isOnSearching = false;
                        isReturnServiceAndCharacteristic = discoveredTargetDevice(searchResult);
                    }
                    break;
                case DISCOVER_WITH_MIX:
                    if (TextUtils.isEmpty(targetAddress) && TextUtils.isEmpty(targetName)) {
                        throw new NullPointerException("混合扫描需要配置mac和name");
                    }
                    if (!TextUtils.isEmpty(name)) {
                        if (address.equals(targetAddress)) {
                            BluetoothClientManager.getClient().stopSearch();
                            isOnSearching = false;
                            discoveredTargetDevice(searchResult);
                            return;
                        }
                        if (name.startsWith(targetName)) {
                            discoverNewDevices(searchResult);
                        }
                    }

                    break;
                default:
                    break;
            }
        }

        @Override
        public void onSearchStopped() {
            switch (discoverType) {
                case IPresenter.DISCOVER_WITH_MAC:
                case IPresenter.DISCOVER_WITH_NAME:
                    if (!isSearchedTargetDevice) {
                        unDiscoveredTargetDevice();
                    }
                    break;
                case IPresenter.DISCOVER_WITH_MIX:
                    if (!isSearchedTargetDevice && devices.size() > 0) {
                        Collections.sort(devices, BaseBluetoothPresenter.this);
                        discoveredTargetDevice(devices.get(0));
                    } else {
                        unDiscoveredTargetDevice();
                    }
                    break;
                default:
                    break;
            }
            isOnSearching = false;
            stopDiscoverDevices();
        }

        @Override
        public void onSearchCanceled() {

        }
    }

    @Override
    public void connectDevice(final String macAddress) {
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)//重连次数
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)//发现服务重试次数
                .setServiceDiscoverTimeout(10000)
                .build();
        if (discoverType == DISCOVER_WITH_MAC && timeCount != null) {
            timeCount.start();
        }
        if (connectListener == null) {
            Logg.e(BaseBluetoothPresenter.class, "connectListener==" + connectListener);
            connectListener = new MyConnectListener(macAddress);
        }
        BluetoothClientManager.getClient().connect(macAddress, options, connectListener);
        if (connectStateListener == null) {
            connectStateListener = new MyConnectStateListener();
        }
        Logg.e(BaseBluetoothPresenter.class, "connectStateListener==" + connectStateListener);

        if (!TextUtils.isEmpty(macAddress)) {
            BluetoothClientManager.getClient().registerConnectStatusListener(macAddress, connectStateListener);
        }
    }

    class MyConnectListener implements BleConnectResponse {
        private String macAddress;

        public MyConnectListener(String macAddress) {
            this.macAddress = macAddress;
        }

        @Override
        public void onResponse(int code, BleGattProfile profile) {
            if (code == Constants.REQUEST_SUCCESS) {
                if (discoverType == DISCOVER_WITH_MAC && timeCount != null) {
                    timeCount.cancel();
                }
                isConnected = true;
                if (!isReturnServiceAndCharacteristic) {
                    connectSuccessed(macAddress, null, false);
                    return;
                }
                List<BleGattService> services = profile.getServices();
                if (services != null && services.size() > 0) {
                    List<BluetoothServiceDetail> details = new ArrayList<>();
                    for (BleGattService service : services) {
                        BluetoothServiceDetail detail = new BluetoothServiceDetail();
                        detail.setService(service.getUUID());
                        detail.setMacAddress(macAddress);
                        List<BleGattCharacter> characters = service.getCharacters();
                        if (characters != null && characters.size() > 0) {
                            List<BluetoothServiceDetail.CharacteristicBean> characteristicBeans = new ArrayList<>();
                            for (BleGattCharacter character : characters) {
                                BluetoothServiceDetail.CharacteristicBean characteristicBean = new BluetoothServiceDetail.CharacteristicBean();
                                characteristicBean.setUuid(character.getUuid());
                                int charaProp = character.getProperty();
                                List<Integer> types = new ArrayList<>();
                                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                                    types.add(BluetoothServiceDetail.READ);
                                }
                                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                                    types.add(BluetoothServiceDetail.WRITE);
                                }
                                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                                    types.add(BluetoothServiceDetail.WRITE_NO_RESPONSE);
                                }
                                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                    types.add(BluetoothServiceDetail.NOTIFY);
                                }
                                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                                    types.add(BluetoothServiceDetail.INDICATE);
                                }
                                characteristicBean.setType(types);
                                characteristicBeans.add(characteristicBean);
                            }
                            detail.setCharacteristics(characteristicBeans);
                        } else {
                            Logg.e(BaseBluetoothPresenter.class, "该服务下未发现特征");
                        }
                        details.add(detail);
                    }
                    connectSuccessed(macAddress, details, isReturnServiceAndCharacteristic);
                } else {
                    Logg.e(BaseBluetoothPresenter.class, "获取服务失败");
                }

            } else {
                connectFailed();
            }
        }
    }

    class MyConnectStateListener extends BleConnectStatusListener {

        @Override
        public void onConnectStatusChanged(String s, int i) {
            switch (i) {
                case Constants.STATUS_CONNECTED:
                    break;
                case Constants.STATUS_DISCONNECTED:
                    isConnected = false;
                    if (weakHandler != null) {
                        weakHandler.sendEmptyMessage(DEVICE_DISCONNECTED);
                    }
                    Logg.e(BaseBluetoothPresenter.class, "onConnectStatusChanged:" + i);
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public void connectOtherDevice(DiscoverDevicesSetting setting) {
        if (!TextUtils.isEmpty(targetAddress) && isConnected) {
            BluetoothClientManager.getClient().disconnect(targetAddress);
            BluetoothClientManager.getClient().refreshCache(targetAddress);
        }
        discoverSetting = setting;
        setSearchRequest();
        searchDevices();
    }

    @Override
    public int compare(SearchResult o1, SearchResult o2) {
        return o2.rssi - o1.rssi;
    }

    /**
     * 重连之前的设备
     *
     * @param
     */
    private void retryConnect() {
        String address = null;
        if (discoverType == IPresenter.DISCOVER_WITH_MAC && !TextUtils.isEmpty(targetAddress)) {
            address = targetAddress;
        } else {
            if (lockedDevice != null) {
                address = lockedDevice.getAddress();
            }
        }
        Log.e("重连设备物理地址：", "retryConnect: " + address);
        if (!TextUtils.isEmpty(address)) {
            connectDevice(address);
        } else {
            if (!TextUtils.isEmpty(targetName)) {
                discoverType = DISCOVER_WITH_NAME;
                request = setSearchRequest();
                searchDevices();
            }
        }
    }

    class TimeCount extends CountDownTimer {
        private WeakHandler weakHandler;

        TimeCount(long millisInFuture, long countDownInterval, WeakHandler weakHandler) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.weakHandler = weakHandler;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (!isConnected) {
                weakHandler.sendEmptyMessage(SEARCH_MAC2NAME);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        if (weakHandler != null) {
            weakHandler.removeCallbacksAndMessages(null);
            weakHandler = null;
        }
        if (weakHandler2 != null) {
            weakHandler2.removeCallbacksAndMessages(null);
            weakHandler2 = null;
        }
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        if (isOnSearching) {
            BluetoothClientManager.getClient().stopSearch();
        }
        String address = null;
        if (discoverType == IPresenter.DISCOVER_WITH_MAC) {
            if (!TextUtils.isEmpty(targetAddress)) {
                address = targetAddress;
            }
        } else {
            if (lockedDevice != null) {
                address = lockedDevice.getAddress();
            }
        }
        if (isConnected && !TextUtils.isEmpty(address)) {
            BluetoothClientManager.getClient().disconnect(address);
        }
        Logg.e(BaseBluetoothPresenter.class, "被注销的mac地址" + address);
        if (!TextUtils.isEmpty(address) && connectStateListener != null) {
            BluetoothClientManager.getClient().unregisterConnectStatusListener(address, connectStateListener);
            connectStateListener = null;
        }
        searchListener = null;
        connectListener = null;
        lockedDevice = null;
        isConnected = false;
        discoverSetting = null;
    }


    protected SearchRequest setSearchRequest() {
        return new SearchRequest.Builder()
                .searchBluetoothLeDevice(8000, 3)
                .build();
    }

    protected SearchRequest setRetrySearchRequest() {
        return new SearchRequest.Builder().searchBluetoothLeDevice(3 * 60 * 1000).build();
    }

    protected void startDiscoverDevices() {
    }

    /**
     * 找到了目标设备 默认规则是找到目标则尝试连接
     *
     * @param device
     * @return 是否需要返回服务和特征通道 默认false
     */
    protected boolean discoveredTargetDevice(SearchResult device) {
        lockedDevice = device.device;
        if (discoverType == IPresenter.DISCOVER_WITH_MAC && TextUtils.isEmpty(targetAddress)) {
            throw new NullPointerException("连接的设备为NULL");
        }
        isSearchedTargetDevice = true;
        connectDevice(device.getAddress());
        return false;
    }

    /**
     * 在规则内未搜索到指定设备则回调该方法
     */
    protected void unDiscoveredTargetDevice() {

    }

    /**
     * 连接成功后会将搜索到的服务和特征以集合的形式返回
     *
     * @param serviceDetails
     */
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails,
                                    boolean isReturnServiceAndCharacteristic) {

    }

    /**
     * 连接失败
     */
    protected void connectFailed() {
        Logg.e(BaseBluetoothPresenter.class, "连接失败");
        if (!TextUtils.isEmpty(targetAddress)) {
            connectDevice(targetAddress);
            return;
        }
    }

    /**
     * 停止搜索
     */
    protected void stopDiscoverDevices() {
        if (discoverType == DISCOVER_WITH_MIX) {
            if (devices.size() > 0) {
                Collections.sort(devices, this);
                discoveredTargetDevice(devices.get(0));
            }
        }
    }

    /**
     * 断开连接
     */
    protected void disConnected() {
        //更新当前状态
        if (baseView instanceof Activity) {
            baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
        } else if (baseView instanceof Fragment) {
            if (((Fragment) baseView).isAdded()) {
                baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
            }
        }
        if (weakHandler2 != null) {
            if (System.currentTimeMillis() - reConnectTimeTag < 3000) {
                reConnectTimeTag = System.currentTimeMillis();
                //解决蓝牙连接状态的监听有时候会回调两次的bug
                return;
            }
            weakHandler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //尝试重连
                    if (!isDestroy) {
                        retryConnect();
                    }
                }
            }, 3000);
        }
    }

    /**
     * 搜索到的设备都会回调该方法
     *
     * @param newDevice
     */
    protected void discoverNewDevices(SearchResult newDevice) {
        if (!devices.contains(newDevice)) {
            devices.add(newDevice);
        }
    }

    /**
     * 如果是自带jar包或者SDK的蓝牙设备 则需要重写该方法并返回true
     *
     * @return
     */
    protected boolean isSelfDefined() {
        return false;
    }

    /**
     * 采集指纹
     */
    public void collectFingers() {
    }

    /**
     * 验证指纹
     */
    public void validateFinger() {
    }
}

