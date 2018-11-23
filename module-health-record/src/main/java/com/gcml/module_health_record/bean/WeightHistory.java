package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2018/1/18.
 */

public class WeightHistory extends BaseBean{
    public float weight;

    public WeightHistory(float weight, long time) {
        this.weight = weight;
        this.time = time;
    }
}
