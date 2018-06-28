package com.example.han.referralproject.bluetooth_devices.base;

import android.bluetooth.BluetoothGattCharacteristic;
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
import java.util.List;

public abstract class BaseBluetoothPresenter implements IPresenter {

    private static DiscoverDevicesSetting discoverSetting;
    private static BluetoothDevice lockedDevice;

    public BaseBluetoothPresenter(DiscoverDevicesSetting discoverSetting) {
        super();
        this.discoverSetting = discoverSetting;
        searchDevices();
    }

    @Override
    public void searchDevices() {
        if (!ClientManager.getClient().isBluetoothOpened()) {
            ClientManager.getClient().openBluetooth();
        }
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2).build();
        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                stateChanged(START_SEARCH_DEVICE);
            }

            @Override
            public void onDeviceFounded(SearchResult searchResult) {
                if (searchResult == null) {
                    return;
                }
                Log.v("发现设备", "onDeviceFounded: " + searchResult.getName() + "--" + searchResult.getAddress() + "---信号强度：" + searchResult.rssi);
                if (discoverSetting != null) {
                    switch (discoverSetting.getDiscoverType()) {
                        case DISCOVER_WITH_MAC:
                            String targetMac = discoverSetting.getTargetMac();
                            if (TextUtils.isEmpty(targetMac)) {
                                ToastTool.showShort("请设置目标mac地址");
                                return;
                            }
                            if (searchResult.getAddress().equals(targetMac)) {
                                ClientManager.getClient().stopSearch();
                                lockedDevice = new BluetoothDevice(DEVICE_INITIAL, searchResult);
                                stateChanged(DEVICE_FOUNDED);
                                discoverTargetDevice(lockedDevice);
                            }
                            break;
                        case DISCOVER_WITH_NAME:
                            String targetName = discoverSetting.getTargetName();
                            if (TextUtils.isEmpty(targetName)) {
                                ToastTool.showShort("请设置目标蓝牙名称");
                            }
                            if (searchResult.getName().equals(targetName)) {
                                ClientManager.getClient().stopSearch();
                                lockedDevice = new BluetoothDevice(DEVICE_INITIAL, searchResult);
                                stateChanged(DEVICE_FOUNDED);
                                discoverTargetDevice(lockedDevice);
                            }
                            break;
                        case DISCOVER_WITH_ALL:
                            stateChanged(DEVICE_FOUNDED);
                            discoverNewDevices(searchResult);
                            break;
                    }
                } else {
                    ToastTool.showShort("请配置搜索参数");
                }
            }

            @Override
            public void onSearchStopped() {
                if (lockedDevice == null) {
                    stateChanged(DEVICE_UNFOUNDED);
                }
                stateChanged(Constants.SEARCH_STOP);
            }

            @Override
            public void onSearchCanceled() {
                stateChanged(CANCEL_SEARCH_DEVICE);
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
                    stateChanged(DEVICE_CONNECT_FAIL);
                    connectFailed();
                }
            }
        });

        ClientManager.getClient().registerConnectStatusListener(macAddress, new BleConnectStatusListener() {
            @Override
            public void onConnectStatusChanged(String s, int i) {
                switch (i) {
                    case Constants.STATUS_CONNECTED:
                        if (lockedDevice != null) {
                            lockedDevice.setCurrentState(DEVICE_CONNECTED);
                        }
                        stateChanged(DEVICE_CONNECTED);
                        break;
                    case Constants.STATUS_DISCONNECTED:
                        lockedDevice = null;
                        stateChanged(DEVICE_DISCONNECTED);
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
        searchDevices();
    }

    @Override
    public void onDestroy() {
        if (lockedDevice != null) {
            ClientManager.getClient().disconnect(lockedDevice.getSearchResult().getAddress());
        }
    }

    /**
     * 连接目标设备成功
     *
     * @param serviceDetails
     */
    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
    }

    /**
     * 连接目标设备失败
     */
    protected void connectFailed() {

    }

    /**
     * 发现目标设备
     *
     * @param device
     */
    protected void discoverTargetDevice(BluetoothDevice device) {
    }

    /**
     * 在设置时间内每新发现一个设备回掉该方法一次,设备可能被重复发现
     *
     * @param newDevice
     */
    protected void discoverNewDevices(SearchResult newDevice) {
    }

}

