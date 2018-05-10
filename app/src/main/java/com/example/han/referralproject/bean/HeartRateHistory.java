package com.example.han.referralproject.bean;

/**
 * Created by gzq on 2017/12/4.
 */

public class HeartRateHistory {
    public long time;
    public float heart_rate;

    public HeartRateHistory(float heart_rate,long time) {
        this.time = time;
        this.heart_rate = heart_rate;
    }
}
