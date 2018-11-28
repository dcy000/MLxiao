package com.gzq.lib_bluetooth;

import com.gzq.lib_core.base.ui.IView;

public interface IBluetoothView extends IView {
    /**
     * 同步数据
     *
     * @param datas
     */
    void updateData(String... datas);

    /**
     * 同步状态
     *
     * @param state
     */
    void updateState(String state);

    /**
     * 是否上传数据 true:上传;false:不上传
     *
     * @return
     */
    boolean isUploadData();
}
