package com.gcml.module_inquiry.inquiry.ui.fragment.base;

import com.gcml.module_blutooth_devices.bloodpressure.BloodPressurePresenter;

/**
 * Created by lenovo on 2019/3/21.
 */

public class ChildActionListenerAdapter implements ChildActionListener {

    /**
     * 中间部分的framgent返回
     */
    @Override
    public void onBack(String... data) {

    }

    @Override
    public void onNext(String... data) {

    }

    /**
     * 添加的第一个fragment 返回
     */
    @Override
    public void onStartBack(String... data) {

    }

    /**
     * 最后一个fragment 的next
     *
     * @param data
     */
    @Override
    public void onFinalNext(String... data) {

    }

    @Override
    public void onNormalICon() {

    }

    @Override
    public void onBluetoothConnect(BloodPressurePresenter presenter) {

    }

    @Override
    public void onBluetoothBreak(BloodPressurePresenter presenter) {

    }

}