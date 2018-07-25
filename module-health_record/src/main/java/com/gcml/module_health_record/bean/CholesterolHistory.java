package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2017/12/5.
 */

public class CholesterolHistory {
    public float cholesterol;
    public long time;

    public CholesterolHistory(float cholesterol, long time) {
        this.cholesterol = cholesterol;
        this.time = time;
    }
}
