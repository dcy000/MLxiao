package com.gzq.test_all_devices.health_record_bean;

/**
 * Created by gzq on 2017/12/1.
 */

public class TemperatureHistory {
    public float temper_ature;
    public long time;

    public TemperatureHistory(float temper_ature, long time) {
        this.temper_ature = temper_ature;
        this.time = time;
    }
}
