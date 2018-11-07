package com.example.han.referralproject.single_measure;

import com.gcml.module_blutooth_devices.others.HandRing_Fragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/11/5 15:28
 * created by: gzq
 * description: TODO
 */
public class SingleMeasureHandRingFragment extends HandRing_Fragment{

    @Override
    protected void clickHeartRateHistory() {
//        CCHealthRecordActions.jump2HealthRecordActivity(4);
    }

    @Override
    protected void clickSleepHistory() {

    }

    @Override
    protected void clickSportsHistory() {

    }

    @Override
    protected void syncHeartRateFinish(String current, String high, String low) {

    }

    @Override
    protected void syncSleepFinished(String deep, String low, String awake) {

    }

    @Override
    protected void syncStepFinished(String step, String km, String ka) {

    }
}
