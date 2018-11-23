package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2017/12/1.
 */

public class TemperatureHistory extends BaseBean{
    public float temper_ature;

    public TemperatureHistory(float temper_ature, long time) {
        this.temper_ature = temper_ature;
        this.time = time;
    }
}
