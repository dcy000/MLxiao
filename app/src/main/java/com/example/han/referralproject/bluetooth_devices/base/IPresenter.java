package com.example.han.referralproject.bluetooth_devices.base;

public interface IPresenter {
    /**
     * 品牌
     */
    String BRAND = "brand";
    /**
     * 表示当前所处的状态 该状态和BluetoothKit的Constants中的变量是相对应的
     */
    int CURRENT_STATE = -1;
    /**
     * 搜索制定mac地址的设备
     */
    int DISCOVER_WITH_MAC = 100;
    /**
     * 搜索指定名称的设备
     */
    int DISCOVER_WITH_NAME = 101;
    /**
     * 搜索所有可见设备地址
     */
    int DISCOVER_WITH_ALL = 102;
    /**
     * 设备最初始状态
     */
    int DEVICE_INITIAL = 0;
    /**
     * 开始搜索设备
     */
    int START_SEARCH_DEVICE = 10;
    /**
     * 发现设备
     */
    int DEVICE_FOUNDED = 11;
    /**
     * 未找到指定设备
     */
    int DEVICE_UNFOUNDED = 12;
    /**
     * 停止搜索设备
     */
    int STOP_SEARCH_DEVICE = 13;
    /**
     * 取消搜索
     */
    int CANCEL_SEARCH_DEVICE = 14;
    /**
     * 设备连接成功
     */
    int DEVICE_CONNECTED = 15;
    /**
     * 设备连接断开
     */
    int DEVICE_DISCONNECTED = 16;
    /**
     * 设备连接失败
     */
    int DEVICE_CONNECT_FAIL=17;
    /**
     * 测量类型
     */
    String MEASURE_TYPE="measure_type";
    /**
     * 测量体温
     */
    int MEASURE_TEMPERATURE = 21;
    /**
     * 测量血压
     */
    int MEASURE_BLOOD_PRESSURE=22;
    /**
     * 测量血糖
     */
    int MEASURE_BLOOD_SUGAR=23;
    /**
     * 测量血氧
     */
    int MEASURE_BLOOD_OXYGEN=24;
    /**
     * 测量耳温
     */
    int MEASURE_WEIGHT=25;
    /**
     * 测量三合一
     */
    int MEASURE_OTHERS=26;

    /**
     * 检查蓝牙是否已开
     */
    void checkBlueboothOpened();
    /**
     * 搜索设备
     */
    void searchDevices();

    /**
     * 连接设备
     */
    void connectDevice();

    /**
     * 连接其他设备
     * @param setting
     */
    void connectOtherDevice(DiscoverDevicesSetting setting);
    void onDestroy();
}

