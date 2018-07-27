package com.gzq.test_all_devices.health_record_bean;

/**
 * Created by gzq on 2017/12/2.
 */

public class BloodPressureHistory {
    public float low_pressure;
    public float high_pressure;
    public long time;

    public BloodPressureHistory(float low_pressure, float high_pressure, long time) {
        this.low_pressure = low_pressure;
        this.high_pressure = high_pressure;
        this.time = time;
    }
}
