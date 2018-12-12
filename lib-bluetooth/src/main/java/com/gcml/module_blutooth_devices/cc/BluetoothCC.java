package com.gcml.module_blutooth_devices.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/29 10:42
 * created by: gzq
 * description: TODO
 */
public class BluetoothCC implements IComponent{
    @Override
    public String getName() {
        return "bluetooth";
    }

    @Override
    public boolean onCall(CC cc) {

        return false;
    }
}
