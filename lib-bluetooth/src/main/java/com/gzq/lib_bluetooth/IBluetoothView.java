package com.gzq.lib_bluetooth;

import com.gzq.lib_core.base.ui.IView;

public interface IBluetoothView extends IView{
    void updateData(String... datas);
    void updateState(String state);
}
