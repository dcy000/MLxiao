package com.gcml.lib_ecg.base;

import android.content.Context;

public interface IView {
    void updateData(String... datas);
    void updateState(String state);
    Context getThisContext();
}
