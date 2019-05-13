package com.gcml.module_blutooth_devices.base;


import android.bluetooth.BluetoothDevice;

import com.gcml.common.recommend.bean.post.DetectionData;

public interface IBluetoothView {
    /**
     * 这里传递的对象和上传服务器的数据结构不一致，只是使用了同一个数据模型
     * @param detectionData
     */
    void updateData(DetectionData detectionData);
    void updateState(String state);
    void discoveryNewDevice(BluetoothDevice device);
    void discoveryFinished();
}
