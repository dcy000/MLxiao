package com.gcml.module_health_record.bean;

/**
 * Created by gzq on 2017/12/22.
 */

public class ECGHistory extends BaseBean{
    public String ecg;

    @Override
    public String toString() {
        return "ECGHistory{" +
                "ecg='" + ecg + '\'' +
                ", time=" + time +
                '}';
    }
}
