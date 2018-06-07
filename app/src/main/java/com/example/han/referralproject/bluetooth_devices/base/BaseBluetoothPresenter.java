package com.example.han.referralproject.bluetooth_devices.base;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import com.example.han.referralproject.util.ToastTool;
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

public abstract class BaseBluetoothPresenter implements IPresenter, Comparator<BluetoothDevice> {
    /**
     * 搜索的基本配置（搜索方式和对应的目标）
     */
    protected static DiscoverDevicesSetting discoverSetting;
    /**
     * 搜索到的目标设备实体
     */
    protected static BluetoothDevice lockedDevice;
    private static int discoverType;
    private SearchRequest request;
    private boolean isOnSearching = false;
    private static String targetName;
    private static String targetAddress;
    protected List<BluetoothDevice> devices;
    private TimeCount timeCount;
    private boolean isDestroy = false;

    public BaseBluetoothPresenter(DiscoverDevicesSetting discoverSetting) {
        super();
        this.discoverSetting = discoverSetting;
        timeCount = new TimeCount(5000, 1000);
        discoverType = discoverSetting.getDiscoverType();
        if (discoverType == DISCOVER_WITH_ALL) {
            devices = new ArrayList<>();
        }
        checkBlueboothOpened();
    }

    @Override
    public void checkBlueboothOpened() {//蓝牙相关权限可在这里进行检查
        boolean bluetoothOpened = ClientManager.getClient().isBluetoothOpened();
        if (!bluetoothOpened) {
            ClientManager.getClient().openBluetooth();
        }
        setSearchRequest();
        searchDevices();
    }

    @Override
    public void searchDevices() {
        if (request == null) {
            ToastTool.showShort("请配置搜索参数");
            return;
        }
        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                targetAddress = discoverSetting.getTargetMac();
                targetName = discoverSetting.getTargetName();
                isOnSearching = true;
                startDiscoverDevices();
                if (discoverType == DISCOVER_WITH_MAC && timeCount != null) {
                    timeCount.start();
                }
            }

            @Override
            public void onDeviceFounded(SearchResult searchResult) {
                if (searchResult == null) {
                    return;
                }
                String name = searchResult.getName();
                String address = searchResult.getAddress();
                Logg.e(BaseBluetoothPresenter.class, "onDeviceFounded: " + name + "--" + address + "---信号强度：" + searchResult.rssi);
                if (discoverSetting != null) {
                    switch (discoverType) {
                        case DISCOVER_WITH_MAC:
                            Logg.e(BaseBluetoothPresenter.class, "MAC");
                            if (TextUtils.isEmpty(targetAddress)) {
                                ToastTool.showShort("请设置目标mac地址");
                                return;
                            }
                            if (address.equals(targetAddress)) {
                                ClientManager.getClient().stopSearch();
                                isOnSearching = false;
                                lockedDevice = new BluetoothDevice(DEVICE_INITIAL, searchResult);
                                discoveredTargetDevice(lockedDevice);
                            }
                            break;
                        case DISCOVER_WITH_NAME:
                            Logg.e(BaseBluetoothPresenter.class, "NAME");
                            if (TextUtils.isEmpty(targetName)) {
                                ToastTool.showShort("请设置目标蓝牙名称");
                                return;
                            }
                            if (!TextUtils.isEmpty(name) && name.equals(targetName)) {
                                ClientManager.getClient().stopSearch();
                                isOnSearching = false;
                                lockedDevice = new BluetoothDevice(DEVICE_INITIAL, searchResult);
                                discoveredTargetDevice(lockedDevice);
                            }
                            break;
                        case DISCOVER_WITH_ALL:
                            Logg.e(BaseBluetoothPresenter.class, "ALL");
                            if (TextUtils.isEmpty(discoverSetting.getTargetMac()) && TextUtils.isEmpty(discoverSetting.getTargetName())) {
                                ToastTool.showShort("请配置搜索参数");
                                return;
                            }
                            if (!TextUtils.isEmpty(name)) {
                                if (name.equals(targetName) || address.equals(targetAddress)) {
                                    discoverNewDevices(new BluetoothDevice(DEVICE_INITIAL, searchResult));
                                }
                            }

                            break;
                    }
                } else {
                    ToastTool.showShort("请配置搜索参数");
                }
            }

            @Override
            public void onSearchStopped() {
                if (lockedDevice == null) {
                    unDiscoveredTargetDevice();
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
    public void connectDevice() {
        if (lockedDevice == null) {
            ToastTool.showShort("尝试连接的设备不存在");
            return;
        }
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)//重连次数
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)//发现服务重试次数
                .setServiceDiscoverTimeout(10000)
                .build();

        final String macAddress = lockedDevice.getSearchResult().getAddress();
        ClientManager.getClient().connect(macAddress, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == Constants.REQUEST_SUCCESS) {
                    lockedDevice.setCurrentState(DEVICE_CONNECTED);
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
                                ToastTool.showShort("该服务下未发现特征");
                            }
                            details.add(detail);
                        }
                        connectSuccessed(details);
                    } else {
                        ToastTool.showShort("获取服务失败");
                    }

                } else {
                    lockedDevice.setCurrentState(DEVICE_CONNECT_FAIL);
                    connectFailed();
                }
            }
        });

        ClientManager.getClient().registerConnectStatusListener(macAddress, new BleConnectStatusListener() {
            @Override
            public void onConnectStatusChanged(String s, int i) {
                switch (i) {
                    case Constants.STATUS_CONNECTED:
                        break;
                    case Constants.STATUS_DISCONNECTED:
                        disConnected();
                        break;

                }
            }
        });
    }

    @Override
    public void connectOtherDevice(DiscoverDevicesSetting setting) {
        if (lockedDevice != null && lockedDevice.getCurrentState() == DEVICE_CONNECTED) {
            ClientManager.getClient().disconnect(lockedDevice.getSearchResult().getAddress());
            ClientManager.getClient().refreshCache(lockedDevice.getSearchResult().getAddress());
            lockedDevice = null;
        }
        this.discoverSetting = setting;
        setSearchRequest();
        searchDevices();
    }

    @Override
    public int compare(BluetoothDevice o1, BluetoothDevice o2) {
        return o2.getSearchResult().rssi - o1.getSearchResult().rssi;
    }

    /**
     * @param
     */
    protected void retryConnect() {
        setRetrySearchRequest();
        searchDevices();
    }

    static class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (lockedDevice == null) {
                discoverType = DISCOVER_WITH_NAME;
                ClientManager.getClient().refreshCache(discoverSetting.getTargetMac());
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

        }
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }
        if (isOnSearching) {
            ClientManager.getClient().stopSearch();
        }
        if (lockedDevice != null && lockedDevice.getCurrentState() == DEVICE_CONNECTED) {
            ClientManager.getClient().disconnect(lockedDevice.getSearchResult().getAddress());
            lockedDevice = null;
        }
    }


    protected void setSearchRequest() {
        request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2)
                .searchBluetoothClassicDevice(5000, 2)
                .searchBluetoothLeDevice(5000, 3)
                .build();
    }

    protected void setRetrySearchRequest() {
        request = new SearchRequest.Builder().searchBluetoothLeDevice(5 * 60 * 1000).build();
    }

    protected void startDiscoverDevices() {
    }

    protected void discoveredTargetDevice(BluetoothDevice device) {
        connectDevice();
    }

    protected void unDiscoveredTargetDevice() {

    }

    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
    }

    protected void connectFailed() {
    }

    protected void stopDiscoverDevices() {
        if (discoverType == DISCOVER_WITH_ALL) {
            if (devices.size() > 0) {
                Collections.sort(devices, this);
                discoveredTargetDevice(devices.get(0));
            }
        }
    }

    protected void disConnected() {
        if (!isDestroy) {
            retryConnect();
        }
    }

    protected void discoverNewDevices(BluetoothDevice newDevice) {
        devices.add(newDevice);
    }
}

