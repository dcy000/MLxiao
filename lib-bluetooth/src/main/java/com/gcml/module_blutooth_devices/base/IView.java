package com.gcml.module_blutooth_devices.base;

import android.content.Context;

public interface IView {
    void updateData(String... datas);
    void updateState(String state);
    Context getThisContext();
}
