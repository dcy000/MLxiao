package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2017/12/5.
 */

public class BUA extends BaseBean{
    public float uric_acid;

    public BUA(float uric_acid, long time) {
        this.uric_acid = uric_acid;
        this.time = time;
    }
}
