package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2017/12/2.
 */

public class BloodOxygenHistory extends BaseBean{
    public float blood_oxygen;

    public BloodOxygenHistory( float blood_oxygen,long time) {
        this.time = time;
        this.blood_oxygen = blood_oxygen;
    }
}
