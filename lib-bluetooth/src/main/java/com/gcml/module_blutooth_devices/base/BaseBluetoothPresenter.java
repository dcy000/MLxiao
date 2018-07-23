package com.gcml.module_blutooth_devices.base;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.gcml.lib_utils.handler.WeakHandler;
import com.gcml.lib_utils.permission.PermissionsManager;
import com.gcml.lib_utils.permission.PermissionsResultAction;
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
import java.util.List;


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
    private static boolean isConnected = false;
    protected static android.bluetooth.BluetoothDevice lockedDevice;
    private WeakHandler weakHandler;
    private static final int SEARCH_MAC2NAME = 1;
    private static final int DEVICE_DISCONNECTED = 2;
    private boolean isSetConnectListenter = false;
    private MyConnectListener connectListener;
    private static long currenMillisecond = 0;


    private final Handler.Callback weakRunnable = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCH_MAC2NAME://搜索不到mac转搜name
                    Logg.e(BaseBluetoothPresenter.class, "物理地址转名字搜索handleMessage: ");
                    discoverType = DISCOVER_WITH_NAME;
                    BluetoothClientManager.getClient().refreshCache(discoverSetting.getTargetMac());
                    request = setSearchRequest();
                    searchDevices();
                    break;
                case DEVICE_DISCONNECTED://蓝牙断开连接
                    disConnected();
                    break;
            }
            return false;
        }
    };


    public BaseBluetoothPresenter(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super();
        this.baseView = fragment;
        this.baseContext = fragment.getThisContext();
        this.discoverSetting = discoverSetting;
        weakHandler = new WeakHandler(weakRunnable);
        //如果物理地址连接8秒之后还没有连接成功 则改为以蓝牙名称匹配
        timeCount = new TimeCount(8000, 1000, weakHandler);
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
            }

        }
    }

    @Override
    public void searchDevices() {
        if (request == null) {
            Logg.e(BaseBluetoothPresenter.class, "请配置搜索参数 ");
            return;
        }
        BluetoothClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                currenMillisecond = System.currentTimeMillis();
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
                if (discoverSetting != null) {
                    switch (discoverType) {
                        case DISCOVER_WITH_NAME:
                            if (TextUtils.isEmpty(targetName)) {
                                throw new NullPointerException("连接的设备为NULL");
                            }
                            if (!TextUtils.isEmpty(name) && name.equals(targetName)) {
                                BluetoothClientManager.getClient().stopSearch();
                                isOnSearching = false;
//                                lockedDevice = new BluetoothDevice(DEVICE_INITIAL, searchResult);
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
//                                    lockedDevice = new BluetoothDevice(DEVICE_INITIAL, searchResult);
                                    discoveredTargetDevice(searchResult);
                                    return;
                                }
                                if (name.equals(targetName)) {
                                    discoverNewDevices(searchResult);
                                }
                            }

                            break;
                    }
                } else {
                    Logg.e(BaseBluetoothPresenter.class, "请配置搜索参数");
                }
            }

            @Override
            public void onSearchStopped() {
                Logg.e(BaseBluetoothPresenter.class, "耗时：" + (System.currentTimeMillis() - currenMillisecond));
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
                }
                isOnSearching = false;
                stopDiscoverDevices();
            }

            @Override
            public void onSearchCanceled() {

            }
        });

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
        BluetoothClientManager.getClient().connect(macAddress, options, new BleConnectResponse() {
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
        });
        //因为此方法会重复调用，多次调用会注册多个广播，故设置isSetConnectListenter进行控制
        if (!isSetConnectListenter) {
            isSetConnectListenter = true;
            connectListener = new MyConnectListener(weakHandler);
            BluetoothClientManager.getClient().registerConnectStatusListener(macAddress, connectListener);
        }
    }

    static class MyConnectListener extends BleConnectStatusListener {
        private WeakHandler weakHandler;

        public MyConnectListener(WeakHandler weakHandler) {
            this.weakHandler = weakHandler;
        }

        @Override
        public void onConnectStatusChanged(String s, int i) {
            switch (i) {
                case Constants.STATUS_CONNECTED:
                    break;
                case Constants.STATUS_DISCONNECTED:
                    if (System.currentTimeMillis() - currenMillisecond > 2000) {
                        currenMillisecond = System.currentTimeMillis();
                        isConnected = false;
                        weakHandler.sendEmptyMessage(DEVICE_DISCONNECTED);
                    }
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
        this.discoverSetting = setting;
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
        if (!TextUtils.isEmpty(address)) {
            connectDevice(address);
        }
    }

    static class TimeCount extends CountDownTimer {
        private WeakHandler weakHandler;

        TimeCount(long millisInFuture, long countDownInterval, WeakHandler weakHandler) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.weakHandler = weakHandler;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (!isConnected) {
                Logg.e(BaseBluetoothPresenter.class, "onFinish: 计时器执行结束");
                weakHandler.sendEmptyMessage(SEARCH_MAC2NAME);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

        }
    }
    @Override
    public void onResume(){}
    @Override
    public void onDestroy() {
        if (weakHandler != null) {
            weakHandler.removeCallbacksAndMessages(null);
            weakHandler = null;
            Logg.e(BaseBluetoothPresenter.class, "onDestroy+weakHandler=null");
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
        Logg.e(BaseBluetoothPresenter.class, "被注销的mac地址" + address);
        if (!TextUtils.isEmpty(address) && connectListener != null) {
            BluetoothClientManager.getClient().unregisterConnectStatusListener(address, connectListener);
            connectListener = null;
            BluetoothClientManager.getClient().disconnect(address);
            Logg.e(BaseBluetoothPresenter.class, "connectListener=null");
        }
        currenMillisecond = 0;
        lockedDevice = null;
        isConnected = false;
        isDestroy = true;
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
        if (discoverType != IPresenter.DISCOVER_WITH_MAC && TextUtils.isEmpty(targetAddress)) {
            throw new NullPointerException("连接的设备为NULL");
        }
        isSearchedTargetDevice = true;
        connectDevice(targetAddress);
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
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturnServiceAndCharacteristic) {

    }

    /**
     * 连接失败
     */
    protected void connectFailed() {
        Logg.e(BaseBluetoothPresenter.class,"连接失败");
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
        if (!isDestroy) {
            retryConnect();
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
    /**采集指纹*/
    public void collectFingers(){}
    /**验证指纹*/
    public void validateFinger(){}
}

