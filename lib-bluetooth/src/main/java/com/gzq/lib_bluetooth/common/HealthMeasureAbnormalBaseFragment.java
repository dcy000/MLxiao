package com.gzq.lib_bluetooth.common;


/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/10 15:06
 * created by:gzq
 * description:TODO
 */
public abstract class HealthMeasureAbnormalBaseFragment extends BaseBluetoothFragment{
    protected HealthMeasureChooseAbnormalReason chooseReason;

    public void setOnChooseReason(HealthMeasureChooseAbnormalReason chooseReason) {
        this.chooseReason = chooseReason;
    }
}
