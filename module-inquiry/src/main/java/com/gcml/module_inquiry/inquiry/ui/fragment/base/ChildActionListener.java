package com.gcml.module_inquiry.inquiry.ui.fragment.base;

import com.gcml.module_blutooth_devices.bloodpressure.BloodPressurePresenter;

/**
 * Created by lenovo on 2019/3/21.
 */

interface ChildActionListener {
    void onBack(String... data);

    void onNext(String... data);

    void onStartBack(String... data);

    void onFinalNext(String... data);

    //右上角图标
    void onNormalICon();

    void onBluetoothConnect(BloodPressurePresenter presenter);

    void onBluetoothBreak(BloodPressurePresenter presenter);

}
