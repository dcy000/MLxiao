package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2017/12/2.
 */

public class BloodSugarHistory extends BaseBean{
    public float blood_sugar;
    public int sugar_time;
    public BloodSugarHistory( float blood_sugar,long time) {
        this.time = time;
        this.blood_sugar = blood_sugar;
    }
}
